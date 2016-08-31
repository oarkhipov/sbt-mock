/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ru.sbt.bpm.mock.spring.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.integration.gateway.TestGatewayService;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.GroovyService;
import ru.sbt.bpm.mock.spring.service.XmlGeneratorService;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;
import ru.sbt.bpm.mock.spring.service.message.validation.ValidationUtils;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.JmsMessageValidationException;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.MessageValidationException;
import ru.sbt.bpm.mock.utils.AjaxObject;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by sbt-bochev-as on 13.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
@Controller
public class MockController {

    @Autowired
    private MockConfigContainer configContainer;
    @Autowired
    private MessageValidationService messageValidationService;
    @Autowired
    private DataFileService dataFileService;
    @Autowired
    private GroovyService groovyService;
    @Autowired
    private XmlGeneratorService generatorService;
    @Autowired
    private TestGatewayService testGatewayService;
    @Autowired
    private DriverController driverController;

    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    public String getDefaultMessage(@PathVariable String systemName,
                                    @PathVariable String integrationPointName,
                                    Model model) throws IOException, TransformerException {
        return getMessageTemplate(systemName, integrationPointName, null, model);
    }

    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/{templateId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    public String getMessageTemplate(@PathVariable String systemName,
                                     @PathVariable String integrationPointName,
                                     @PathVariable String templateId,
                                     Model model) throws IOException, TransformerException {

        System system = configContainer.getSystemByName(systemName);
        model.addAttribute("systemName", systemName);
        model.addAttribute("name", integrationPointName);
        model.addAttribute("link", "mock");
        model.addAttribute("protocol", system.getProtocol().toString());
        model.addAttribute("xpath",
                system.getProtocol().equals(Protocol.JMS) ?
                        system.getIntegrationPoints().getIntegrationPointByName(integrationPointName)
                                .getXpathString() : null);
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
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/validate/", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
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
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/save/", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    public String save(
            @PathVariable String systemName,
            @PathVariable String integrationPointName,
            @RequestParam String xml,
            @RequestParam String script,
            @RequestParam String test,
            @RequestParam(required = false) String templateId) throws IOException {
        Tuple2<AjaxObject, String> ajaxObjectWithCompiledXml = validateMockMessages(xml, test, script, systemName, integrationPointName);
        AjaxObject ajaxObject = ajaxObjectWithCompiledXml.getT1();
//        String compiledXml = ajaxObjectWithCompiledXml.getT2();
        ajaxObject = driverController.saveState(systemName, integrationPointName, xml, script, test, templateId, ajaxObject);
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
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/resetToDefault/", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    public String resetToDefaultFull(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName) throws Exception {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(systemName, integrationPointName, MessageType.RS, false));
        } catch (Exception e) {
            ajaxObject.setError(e);
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/resetToDefault/filtered", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    public String resetToDefaultFiltered(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName) throws Exception {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(systemName, integrationPointName, MessageType.RS, true));
        } catch (Exception e) {
            ajaxObject.setError(e);
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/resetToDefault/test/", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    public String resetTestToDefaultFull(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName) throws Exception {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(systemName, integrationPointName, MessageType.RQ, false));
        } catch (Exception e) {
            ajaxObject.setError(e);
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/resetToDefault/test/filtered", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    public String resetTestToDefaultFiltered(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName) throws Exception {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(systemName, integrationPointName, MessageType.RQ, true));
        } catch (Exception e) {
            ajaxObject.setError(e);
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/validate/test/", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    public String validateTestMessage(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName,
            @RequestParam String xml) {
        AjaxObject ajaxObject = validateTest(xml, systemName, integrationPointName);
        if (ajaxObject.getError() == null || ajaxObject.getError().length() == 0) {
            ajaxObject.setInfo("Valid!");
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/mock/{systemName}/{integrationPointName}/test/", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
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
        AjaxObject ajaxObject = validateTest(test, systemName, integrationPointName);
        String compiledXml = "";

        System system = configContainer.getSystemByName(systemName);
        IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);

        try {
            //check if Test validation was OK
            if (ajaxObject.getError() == null || ajaxObject.getError().length() == 0) {
                compiledXml = groovyService.execute(test, xml, script);
                if (driverController.validationNeeded(systemName, integrationPointName)) {
                    if (messageValidationService.assertMessageElementName(compiledXml, system, integrationPoint, MessageType.RS)) {
                        final List<String> validationErrors = messageValidationService.validate(compiledXml, systemName);
                        if (validationErrors.size() != 0) {
                            ajaxObject.setError("Message validation:\n" + ValidationUtils.getSolidErrorMessage(validationErrors));
                        }
                    } else {
                        ajaxObject.setError("Message assertion fail");
                    }
                }
            }
        } catch (JmsMessageValidationException e) {
            ajaxObject.setError(e.getMessage());
        } catch (MessageValidationException e) {
            ajaxObject.setError(e.getMessage());
        } catch (Exception e) {
            ajaxObject.setError(e);
        }
        return Tuple.of(ajaxObject, compiledXml);
    }

    private AjaxObject validateTest(String test, String systemName, String integrationPointName) {
        AjaxObject ajaxObject = new AjaxObject();
        if (driverController.validationNeeded(systemName, integrationPointName)) {
            if (test != null && test.length() > 0) {
                System system = configContainer.getSystemByName(systemName);
                IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);
                try {
                    if (messageValidationService.assertMessageElementName(test, system, integrationPoint, MessageType.RQ)) {
                        final List<String> validationErrors = messageValidationService.validate(test, systemName);
                        if (validationErrors.size() != 0) {
                            ajaxObject.setError("Test message validation:\n" + ValidationUtils.getSolidErrorMessage(validationErrors));
                        }
                    } else {
                        //assertion fault
                        ajaxObject.setError("Test message assertion fail");
                    }
                } catch (JmsMessageValidationException e) {
                    ajaxObject.setError(e.getMessage());
                } catch (MessageValidationException e) {
                    ajaxObject.setError(e.getMessage());
                } catch (Exception e) {
                    ajaxObject.setError(e);
                }
            }
        }
        return ajaxObject;
    }
}