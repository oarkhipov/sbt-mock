package ru.sbt.bpm.mock.spring.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;
import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;
import ru.sbt.bpm.mock.spring.integration.gateway.TestGatewayService;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.GroovyService;
import ru.sbt.bpm.mock.spring.service.MessageSendingService;
import ru.sbt.bpm.mock.spring.service.XmlGeneratorService;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;
import ru.sbt.bpm.mock.spring.service.message.validation.ValidationUtils;
import ru.sbt.bpm.mock.spring.utils.AjaxObject;
import ru.sbt.bpm.mock.spring.utils.SaveFile;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by sbt-bochev-as on 15.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
@Controller
public class DriverController {

    @Autowired
    ApplicationContext appContext;

    @Autowired
    MessageSendingService sendingService;

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    MessageValidationService messageValidationService;

    @Autowired
    DataFileService dataFileService;

    @Autowired
    GroovyService groovyService;

    @Autowired
    TestGatewayService testGatewayService;

    @Autowired
    XmlGeneratorService generatorService;


    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    public String getDefaultMessage(@PathVariable String systemName,
                      @PathVariable String integrationPointName,
                      Model model) throws IOException, TransformerException {
        getMessageTemplate(systemName, integrationPointName, null, model);
        return "editor";
    }

    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/{templateId}/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    public String getMessageTemplate(@PathVariable String systemName,
                                     @PathVariable String integrationPointName,
                                     @PathVariable String templateId,
                                     Model model) throws IOException, TransformerException {
        System system = configContainer.getSystemByName(systemName);
        model.addAttribute("systemName", systemName);
        model.addAttribute("name", integrationPointName);
        model.addAttribute("link", "driver");
        model.addAttribute("protocol", system.getProtocol().toString());
        IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);
        model.addAttribute("xpath",
                system.getProtocol().equals(Protocol.JMS) ?
                        integrationPoint.getXpathString() : null);
        model.addAttribute("enabled", system.getEnabled() && integrationPoint.getEnabled());

        //noinspection Duplicates
        if (templateId != null) {
            model.addAttribute("message", dataFileService.getMessage(systemName, integrationPointName, templateId));
            model.addAttribute("script", dataFileService.getScript(systemName, integrationPointName, templateId));
            model.addAttribute("test", dataFileService.getTest(systemName, integrationPointName, templateId));
            model.addAttribute("template", configContainer.getConfigEntitiesByMessageTemplateUUID(UUID.fromString(templateId)).getT3());

        } else {
            model.addAttribute("message", dataFileService.getDefaultMessage(systemName, integrationPointName));
            model.addAttribute("script", dataFileService.getDefaultScript(systemName, integrationPointName));
            model.addAttribute("test", dataFileService.getDefaultTest(systemName, integrationPointName));
            model.addAttribute("template", null);
        }
        return "editor";
    }

    @ResponseBody
    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/validate/", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    public String validate(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName,
            @RequestParam String xml,
            @RequestParam String script,
            @RequestParam String test) {
        Tuple2<AjaxObject, String> ajaxObjectWithCompiledXml = validateDriverMessages(xml, test, script, systemName, integrationPointName);
        AjaxObject ajaxObject = ajaxObjectWithCompiledXml.getT1();

        if (ajaxObject.getError() == null || ajaxObject.getError().length() == 0) {
            ajaxObject.setInfo("Valid!");
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/save/", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    public String save(
            @PathVariable String systemName,
            @PathVariable String integrationPointName,
            @RequestParam String xml,
            @RequestParam String script,
            @RequestParam String test,
            @RequestParam(required = false) String templateId) throws IOException {
        Tuple2<AjaxObject, String> ajaxObjectWithCompiledXml = validateDriverMessages(xml, test, script, systemName, integrationPointName);
        AjaxObject ajaxObject = ajaxObjectWithCompiledXml.getT1();
//        String compiledXml = ajaxObjectWithCompiledXml.getT2();
        ajaxObject = saveState(systemName, integrationPointName, xml, script, test, templateId, ajaxObject);
        return ajaxObject.toJSON();
    }

    AjaxObject saveState(String systemName, String integrationPointName, String xml, String script, String test, String templateId, AjaxObject ajaxObject) throws IOException {
        SaveFile saver = SaveFile.getInstance(appContext);
        if (ajaxObject.getError() == null || ajaxObject.getError().length() == 0) {
            // If Valid - then save
            File messageFile = null;
            File scriptFile = null;
            File testFile = null;
            try {
                String templateSuffix = templateId != null && !templateId.isEmpty() ? "__" + templateId : "";
                //message file
                messageFile = saver.getBackUpedDataFile(saver.TranslateNameToPath(systemName + "__" + integrationPointName + templateSuffix + "__" + "message.xml"));
                //script file
                scriptFile = saver.getBackUpedDataFile(saver.TranslateNameToPath(systemName + "__" + integrationPointName + templateSuffix + "__" + "script.groovy"));
                //test file
                testFile = saver.getBackUpedDataFile(saver.TranslateNameToPath(systemName + "__" + integrationPointName + templateSuffix + "__" + "test.xml"));
            } catch (Exception e) {
                ajaxObject.setError(e.getMessage());
            }
            if (messageFile != null && scriptFile != null && testFile != null) {
                try {
                    saver.writeStringToFile(messageFile, xml);
                    saver.writeStringToFile(scriptFile, script);
                    saver.writeStringToFile(testFile, test);
                    ajaxObject.setInfo("Saved!");
                } catch (IOException e) {
                    ajaxObject.setErrorFromException(e);
                } catch (Exception e) {
                    ajaxObject.setErrorFromException(e);
                }
            }
        }
        return ajaxObject;
    }

    //TODO
//    @RequestMapping(value="/driver/{name}/undo/", method=RequestMethod.POST)
//    public String rollback(
//            @PathVariable("name") String name,
//            ModelMap model) throws IOException  {
//        AjaxObject ajaxObject = new AjaxObject();
//        SaveFile saver = SaveFile.getInstance(appContext);
//        File dataFile;
//        try {
//            String path = saver.TranslateNameToPath(name);
//            dataFile = saver.rollbackNextBackupDataFile(path);
//            String datavalue = saver.getFileString(dataFile);
//            ajaxObject.setData(datavalue);
//            ajaxObject.setInfo("undo");
//        }catch (IndexOutOfBoundsException e) {
//            ajaxObject.setError(e.getMessage());
//        }
//        model.addAttribute("object", ajaxObject.toJSON());
//
//        return "blank";
//    }
//
//    @RequestMapping(value="/driver/{name}/redo/", method=RequestMethod.POST)
//    public String rollforward(
//            @PathVariable("name") String name,
//            ModelMap model) throws IOException  {
//        AjaxObject ajaxObject = new AjaxObject();
//        SaveFile saver = SaveFile.getInstance(appContext);
//        File dataFile;
//        try {
//            String path = saver.TranslateNameToPath(name);
//            dataFile = saver.rollbackPervBackUpedDataFile(path);
//            String datavalue = saver.getFileString(dataFile);
//            ajaxObject.setData(datavalue);
//            ajaxObject.setInfo("redo");
//        }catch (IndexOutOfBoundsException e) {
//            ajaxObject.setError(e.getMessage());
//        }
//        catch (IOException e) {
//            ajaxObject.setError(e.getMessage());
//        }
//        model.addAttribute("object", ajaxObject.toJSON());
//
//        return "blank";
//    }

    @ResponseBody
    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/resetToDefault/filtered", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    public String resetToDefaultFiltered(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName) throws Exception {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(systemName, integrationPointName, MessageType.RQ, true));
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/resetToDefault/", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    public String resetToDefaultFull(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName) throws Exception {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(systemName, integrationPointName, MessageType.RQ, false));
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }
        return ajaxObject.toJSON();
    }


    @ResponseBody
    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/send/", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    public String send(
            @PathVariable String systemName,
            @PathVariable String integrationPointName,
            @RequestParam(required = false) String xml,
            @RequestParam(required = false) String script,
            @RequestParam(required = false) String test) throws IOException {
        Tuple2<AjaxObject, String> ajaxObjectWithCompiledXml = validateDriverMessages(xml, test, script, systemName, integrationPointName);
        AjaxObject ajaxObject = ajaxObjectWithCompiledXml.getT1();

        if (ajaxObject.getError() == null || ajaxObject.getError().length() == 0) {
            String compiledXml = ajaxObjectWithCompiledXml.getT2();
            ru.sbt.bpm.mock.config.entities.System systemByName = configContainer.getSystemByName(systemName);
            IntegrationPoint integrationPointByName = systemByName.getIntegrationPoints().getIntegrationPointByName(integrationPointName);

            MockMessage mockMessage = new MockMessage(systemByName.getProtocol(),
                    systemByName.getProtocol() == Protocol.JMS ? systemByName.getQueueConnectionFactory() : null,
                    systemByName.getProtocol() == Protocol.JMS ? systemByName.getDriverOutcomeQueue() : null,
                    compiledXml);
            mockMessage.setSystem(systemByName);
            mockMessage.setIntegrationPoint(integrationPointByName);

            String response = sendingService.send(mockMessage);
            ajaxObject.setData(response);
            ajaxObject.setInfo("DONE!");
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/test/", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    public String test(
            @PathVariable String systemName,
            @PathVariable String integrationPointName,
            @RequestParam String xml,
            @RequestParam String script,
            @RequestParam String test) {
        Tuple2<AjaxObject, String> ajaxObjectWithCompiledXml = validateDriverMessages(xml, test, script, systemName, integrationPointName);
        AjaxObject ajaxObject = ajaxObjectWithCompiledXml.getT1();
        String compiledXml = ajaxObjectWithCompiledXml.getT2();
        if (ajaxObject.getError() == null || ajaxObject.getError().length() == 0) {
            String response = testGatewayService.test(compiledXml);
            ajaxObject.setData(response);
            ajaxObject.setInfo("DONE!");
        }
        return ajaxObject.toJSON();
    }

    /**
     * Returns ajaxObject with possible errors and compiled xml
     *
     * @param xml                  mockXml string
     * @param test                 test xml for driver -- not used
     * @param script               groovy script
     * @param systemName           name of system of Driver
     * @param integrationPointName name of integration point
     * @return Tuple of AjaxObject and compiledXml
     */
    private Tuple2<AjaxObject, String> validateDriverMessages(String xml, String test, String script, String systemName, String integrationPointName) {
        AjaxObject ajaxObject = new AjaxObject();
        String compiledXml = "";
        try {
            ru.sbt.bpm.mock.config.entities.System system = configContainer.getSystemByName(systemName);
            IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);

            compiledXml = groovyService.execute(test, xml, script);
            if (validationNeeded(systemName, integrationPointName)) {
                if (messageValidationService.assertMessageElementName(compiledXml, system, integrationPoint, MessageType.RQ)) {
                    final List<String> validationErrors = messageValidationService.validate(compiledXml, systemName);
                    if (validationErrors.size() != 0) {
                        ajaxObject.setError(ValidationUtils.getSolidErrorMessage(validationErrors));
                    }
                }
            }
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }
        return Tuple.of(ajaxObject, compiledXml);
    }

    boolean validationNeeded(String systemName, String integrationPointName) {
        MockConfig config = configContainer.getConfig();
        if (!config.getMainConfig().getValidationEnabled()) return false;
        System systemByName = config.getSystems().getSystemByName(systemName);
        if (!systemByName.getValidationEnabled()) return  false;
        IntegrationPoint integrationPointByName = systemByName.getIntegrationPoints().getIntegrationPointByName(integrationPointName);
        if (!integrationPointByName.getValidationEnabled()) return false;
        return true;
    }
}
