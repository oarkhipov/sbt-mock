package ru.sbt.bpm.mock.spring.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.*;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.integration.gateway.TestGatewayService;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.GroovyService;
import ru.sbt.bpm.mock.spring.service.XmlGeneratorService;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;
import ru.sbt.bpm.mock.spring.service.message.validation.ValidationUtils;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.MessageValidationException;
import ru.sbt.bpm.mock.spring.utils.AjaxObject;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

/**
 * Created by sbt-bochev-as on 13.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
@Controller
public class MockController {

    @Autowired
    ApplicationContext appContext;

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    MessageValidationService messageValidationService;

    @Autowired
    DataFileService dataFileService;

    @Autowired
    GroovyService groovyService;

    @Autowired
    XmlGeneratorService generatorService;

    @Autowired
    TestGatewayService testGatewayService;

    @Autowired
    DriverController driverController;

    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE+";charset=utf-8")
    public String get(@PathVariable String systemName,
                      @PathVariable String integrationPointName,
                      Model model) throws IOException, TransformerException {

        ru.sbt.bpm.mock.config.entities.System system = configContainer.getSystemByName(systemName);
        model.addAttribute("systemName", systemName);
        model.addAttribute("name", integrationPointName);
        model.addAttribute("link", "mock");
        model.addAttribute("protocol",system.getProtocol().toString());
        //TODO send "xpath with namespace" via js tooltip
        model.addAttribute("xpath",
                system.getProtocol().equals(Protocol.JMS)?
                    system.getIntegrationPoints().getIntegrationPointByName(integrationPointName)
                        .getXpathString():null);
        model.addAttribute("message", dataFileService.getCurrentMessage(systemName, integrationPointName));
        model.addAttribute("script", dataFileService.getCurrentScript(systemName, integrationPointName));
        model.addAttribute("test", dataFileService.getCurrentTest(systemName, integrationPointName));
        return "editor";
    }

    @ResponseBody
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/validate/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
    public String validate(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName,
            @RequestParam String xml,
            @RequestParam String script,
            @RequestParam String test) {
        Tuple2<AjaxObject, String> ajaxObjectWithCompiledXml = validateMockMessages(xml, test, script, systemName, integrationPointName);
        AjaxObject ajaxObject = ajaxObjectWithCompiledXml.getT1();
        if (ajaxObject.getError() == null || ajaxObject.getError().length() == 0) {
            ajaxObject.setInfo("Valid!");
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/save/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
    public String save(
            @PathVariable String systemName,
            @PathVariable String integrationPointName,
            @RequestParam String xml,
            @RequestParam String script,
            @RequestParam String test) throws IOException {
        Tuple2<AjaxObject, String> ajaxObjectWithCompiledXml = validateMockMessages(xml, test, script, systemName, integrationPointName);
        AjaxObject ajaxObject = ajaxObjectWithCompiledXml.getT1();
//        String compiledXml = ajaxObjectWithCompiledXml.getT2();
        ajaxObject = driverController.saveState(systemName, integrationPointName, xml, script, test, ajaxObject);
        return ajaxObject.toJSON();
    }

//    @RequestMapping(value="/mock/{name}/undo/", method=RequestMethod.POST)
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
//    @RequestMapping(value="/mock/{name}/redo/", method=RequestMethod.POST)
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
//            model.addAttribute("error", e.getMessage());
//        }
//        model.addAttribute("object", ajaxObject.toJSON());
//
//        return "blank";
//    }

    @ResponseBody
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/resetToDefault/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
    public String resetToDefaultFull(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName) throws Exception {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(systemName, integrationPointName, MessageType.RS, false));
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/resetToDefault/filtered", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
    public String resetToDefaultFiltered(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName) throws Exception {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(systemName, integrationPointName, MessageType.RS, true));
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/test/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
    public String test(
            @PathVariable String systemName,
            @PathVariable String integrationPointName,
            @RequestParam String xml,
            @RequestParam String script,
            @RequestParam String test) {

        Tuple2<AjaxObject, String> ajaxObjectWithCompiledXml = validateMockMessages(xml, test, script, systemName, integrationPointName);
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
     * @param test                 test xml for mock
     * @param script               groovy script
     * @param systemName           name of system of Mock
     * @param integrationPointName name of integration point
     * @return Tuple of AjaxObject and compiledXml
     */
    private Tuple2<AjaxObject, String> validateMockMessages(String xml, String test, String script, String systemName, String integrationPointName) {
        AjaxObject ajaxObject = new AjaxObject();
        String compiledXml = "";
        try {
            ru.sbt.bpm.mock.config.entities.System system = configContainer.getSystemByName(systemName);
            IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);
            try {
                //TODO remake this block
                if (test == null || test.length() == 0 || messageValidationService.assertMessageElementName(test, system, integrationPoint, MessageType.RQ)) {
                    compiledXml = groovyService.execute(test, xml, script);
                    try {
                        if (messageValidationService.assertMessageElementName(compiledXml, system, integrationPoint, MessageType.RS)) {
                            final List<String> validationErrors = messageValidationService.validate(compiledXml, systemName);
                            if (validationErrors.size() != 0) {
                                ajaxObject.setError(ValidationUtils.getSolidErrorMessage(validationErrors));
                            }
                        }
                    } catch (MessageValidationException e) {
                        //TODO make it somehow look better
                        ajaxObject.setErrorFromException(new RuntimeException("Test element name assertion Exception",e));
                    }
                }
            } catch (MessageValidationException e) {
                ajaxObject.setError("");
            }
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }
        return Tuple.of(ajaxObject, compiledXml);
    }
}
