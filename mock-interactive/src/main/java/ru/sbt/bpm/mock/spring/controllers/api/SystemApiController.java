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

import net.sf.saxon.s9api.SaxonApiException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import reactor.tuple.Tuple2;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.ElementSelector;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.entities.Systems;
import ru.sbt.bpm.mock.config.entities.XpathSelector;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.service.ConfigurationService;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.XsdAnalysisService;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;
import ru.sbt.bpm.mock.spring.service.message.validation.ValidationUtils;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.MockMessageValidationException;
import ru.sbt.bpm.mock.utils.AjaxObject;
import ru.sbt.bpm.mock.utils.ExceptionUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author sbt-bochev-as on 23.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class SystemApiController {

    @Autowired
    private MockConfigContainer configContainer;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private DataFileService dataFileService;
    @Autowired
    private MessageValidationService validationService;
    @Autowired
    private XsdAnalysisService xsdAnalysisService;

    @RequestMapping(value = "/api/system/add/", method = RequestMethod.POST)
    public String add(@RequestParam(required = false) boolean enabled,
                      @RequestParam String name,
                      @RequestParam Protocol protocol,
                      @RequestParam String rootSchema,
                      @RequestParam(value = "integrationPointSelectorNamespace[]", required = false) String[] integrationPointSelectorNamespace,
                      @RequestParam(value = "integrationPointSelectorElementName[]", required = false) String[] integrationPointSelectorElementName,
                      @RequestParam(required = false) String queueConnectionFactory,
                      @RequestParam(required = false) String mockIncomeQueue,
                      @RequestParam(required = false) String mockOutcomeQueue,
                      @RequestParam(required = false) String driverOutcomeQueue,
                      @RequestParam(required = false) String driverIncomeQueue,
                      @RequestParam(required = false) String driverWebServiceEndpoint,
                      @RequestParam(required = false) boolean validationEnabled,
                      @RequestParam(value = "rootElementNamespace", required = false) String rootElementNamespace,
                      @RequestParam(value = "rootElementName", required = false) String rootElementName) throws IOException, SAXException, JAXBException {
        Systems systems = configContainer.getConfig().getSystems();
        XpathSelector xpathSelector = null;
        if (integrationPointSelectorNamespace != null && integrationPointSelectorElementName != null) {
            xpathSelector = new XpathSelector(integrationPointSelectorNamespace, integrationPointSelectorElementName);
        }
        ElementSelector rootElement = new ElementSelector(rootElementNamespace, rootElementName);

        String localRootSchema = null;
        String rootSchemaLowerCase = rootSchema.toLowerCase();
        if (!rootSchemaLowerCase.startsWith("http://") && !rootSchemaLowerCase.startsWith("ftp://")) {
            localRootSchema = rootSchema;
        }

        System system = new System(name, rootSchema, localRootSchema, xpathSelector, protocol, queueConnectionFactory, mockIncomeQueue,
                mockOutcomeQueue, driverOutcomeQueue, driverIncomeQueue, driverWebServiceEndpoint, rootElement, null);
        system.setEnabled(enabled);
        system.setValidationEnabled(validationEnabled);

        systems.getSystems().add(system);
        validationService.reInitValidator(name);
        configurationService.saveConfig();
        if (protocol == Protocol.JMS) {
            configurationService.reInitSpringContext();
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/api/system/update/{systemName}/", method = RequestMethod.POST)
    public String update(@RequestParam(required = false) boolean enabled,
                         @PathVariable String systemName,
                         @RequestParam(value = "name") String newSystemName,
                         @RequestParam Protocol protocol,
                         @RequestParam String rootSchema,
                         @RequestParam(value = "integrationPointSelectorNamespace[]", required = false) String[] integrationPointSelectorNamespace,
                         @RequestParam(value = "integrationPointSelectorElementName[]", required = false) String[] integrationPointSelectorElementName,
                         @RequestParam(required = false) String queueConnectionFactory,
                         @RequestParam(required = false) String mockIncomeQueue,
                         @RequestParam(required = false) String mockOutcomeQueue,
                         @RequestParam(required = false) String driverOutcomeQueue,
                         @RequestParam(required = false) String driverIncomeQueue,
                         @RequestParam(required = false) String driverWebServiceEndpoint,
                         @RequestParam(required = false) boolean validationEnabled,
                         @RequestParam(value = "rootElementNamespace", required = false) String rootElementNamespace,
                         @RequestParam(value = "rootElementName", required = false) String rootElementName) throws IOException, SAXException, JAXBException {

        boolean needToReInitSpringContext = false;
        System systemObject = configContainer.getConfig().getSystems().getSystemByName(systemName);

        if (!systemObject.getEnabled().equals(enabled)) {
            systemObject.setEnabled(enabled);
            if (protocol == Protocol.JMS) {
                needToReInitSpringContext = true;
            }
        }

        if (!systemObject.getSystemName().equals(newSystemName)) {
            systemObject.setSystemName(newSystemName);
            dataFileService.moveSystemDir(systemName, newSystemName);
        }

        if (!systemObject.getProtocol().equals(protocol)) {
            systemObject.setProtocol(protocol);
            if (protocol == Protocol.JMS) {
                needToReInitSpringContext = true;
            }
        }

        systemObject.setRemoteRootSchema(rootSchema);

        String localRootSchema = null;
        if (rootSchema != null) {
            String rootSchemaLowerCase = rootSchema.toLowerCase();
            if (!rootSchemaLowerCase.startsWith("http://") && !rootSchemaLowerCase.startsWith("ftp://")) {
                localRootSchema = rootSchema;
            }
        }

        if (localRootSchema != null) {
            if (systemObject.getLocalRootSchema() == null || !systemObject.getLocalRootSchema().equals(localRootSchema)) {
                systemObject.setLocalRootSchema(localRootSchema);
            }
        }

        XpathSelector xpathSelector = null;
        if (integrationPointSelectorNamespace != null) {
            xpathSelector = new XpathSelector(integrationPointSelectorNamespace, integrationPointSelectorElementName);
        }

        if (systemObject.getIntegrationPointSelector() == null || !systemObject.getIntegrationPointSelector().equals(xpathSelector)) {
            systemObject.setIntegrationPointSelector(xpathSelector);
        }

        if (queueConnectionFactory != null) {
            if (systemObject.getQueueConnectionFactory() == null || !systemObject.getQueueConnectionFactory().equals(queueConnectionFactory)) {
                systemObject.setQueueConnectionFactory(queueConnectionFactory);
                needToReInitSpringContext = true;
            }
        }

        if (mockIncomeQueue != null) {
            if (systemObject.getMockIncomeQueue() == null || !systemObject.getMockIncomeQueue().equals(mockIncomeQueue)) {
                systemObject.setMockIncomeQueue(mockIncomeQueue);
                needToReInitSpringContext = true;
            }
        }

        if (mockOutcomeQueue != null) {
            if (systemObject.getMockOutcomeQueue() == null || !systemObject.getMockOutcomeQueue().equals(mockOutcomeQueue)) {
                systemObject.setMockOutcomeQueue(mockOutcomeQueue);
                needToReInitSpringContext = true;
            }
        }

        if (driverOutcomeQueue != null) {
            if (systemObject.getDriverOutcomeQueue() == null || !systemObject.getDriverOutcomeQueue().equals(driverOutcomeQueue)) {
                systemObject.setDriverOutcomeQueue(driverOutcomeQueue);
                needToReInitSpringContext = true;
            }
        }

        if (driverIncomeQueue != null) {
            if (systemObject.getDriverIncomeQueue() == null || !systemObject.getDriverIncomeQueue().equals(driverIncomeQueue)) {
                systemObject.setDriverIncomeQueue(driverIncomeQueue);
                needToReInitSpringContext = true;
            }
        }

        if (driverWebServiceEndpoint != null) {
            if (systemObject.getDriverWebServiceEndpoint() == null || !systemObject.getDriverWebServiceEndpoint().equals(driverWebServiceEndpoint)) {
                systemObject.setDriverWebServiceEndpoint(driverWebServiceEndpoint);
            }
        }

        if (rootElementNamespace != null && rootElementName != null) {
            ElementSelector rootElement = new ElementSelector(rootElementNamespace, rootElementName);
            if (!systemObject.getRootElement().equals(rootElement)) {
                systemObject.setRootElement(rootElement);
            }
        }

        systemObject.setValidationEnabled(validationEnabled);

        configurationService.saveConfig();
        validationService.reInitValidator(systemName);
        if (needToReInitSpringContext) {
            configurationService.reInitSpringContext();
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/api/system/delete/{systemName}/", method = RequestMethod.POST)
    public String delete(@PathVariable String systemName) throws IOException, JAXBException {
        Systems systems = configContainer.getConfig().getSystems();
        System system = systems.getSystemByName(systemName);
        Protocol protocol = system.getProtocol();
        validationService.removeValidator(systemName);
        dataFileService.deleteSystemDir(systemName);
        systems.getSystems().remove(system);
        configurationService.saveConfig();
        if (protocol == Protocol.JMS) {
            configurationService.reInitSpringContext();
        }
        return "redirect:/";
    }

    @ResponseBody
    @RequestMapping(value = "/api/system/reinitValidator/{systemName}/")
    public String reinitValidator(@PathVariable String systemName) throws IOException, SAXException, SaxonApiException {
        validationService.reInitValidator(systemName);
        xsdAnalysisService.reInit(systemName);
        return "OK!";
    }

    @ResponseBody
    @RequestMapping(value = "/api/system/reinitValidator/")
    public String reinitValidator() {
        Set<System> systems = configContainer.getConfig().getSystems().getSystems();
        for (System system : systems) {
            try {
                validationService.reInitValidator(system.getSystemName());
                xsdAnalysisService.reInit(system.getSystemName());
            } catch (Exception e) {
                return ExceptionUtils.getExceptionStackTrace(e);
            }
        }
        return "OK!";
    }

    @ResponseBody
    @RequestMapping(value = "/api/system/{systemName}/namespaces/")
    public String getNamespaceList(@PathVariable String systemName,
                                   @RequestParam(required = false, value = "namespace") String namespacePart) {
        Set<String> resultSet = new TreeSet<String>();
        Set<String> namespaceSet = xsdAnalysisService.getNamespaceFromXsd(systemName);
        for (String namespace : namespaceSet) {
            if (namespacePart != null && !namespacePart.isEmpty()) {
                if (namespace.toLowerCase().contains(namespacePart.toLowerCase())) {
                    resultSet.add(String.format("{\"value\": \"%s\"}", namespace));
                }
            } else {
                resultSet.add(String.format("{\"value\": \"%s\"}", namespace));
            }
        }
        return ArrayUtils.toString(resultSet);
    }

    @ResponseBody
    @RequestMapping(value = "/api/system/{systemName}/elements/")
    public String getElementsList(@PathVariable String systemName,
                                  @RequestParam(required = false) String namespace,
                                  @RequestParam(required = false) String elementName) {
        List<String> resultList = new ArrayList<String>();
        List<Tuple2<String, String>> tuple2List;
        if (namespace == null || namespace.isEmpty()) {
            tuple2List = xsdAnalysisService.getElementsForSystem(systemName);
        } else {
            tuple2List = xsdAnalysisService.getElementsForSystem(systemName, namespace);
        }
        for (Tuple2<String, String> tupleEntity : tuple2List) {
            if (elementName != null) {
                if (tupleEntity.getT2().toLowerCase().contains(elementName.toLowerCase())) {
                    resultList.add(String.format("{\"namespace\": \"%s\", \"elementName\": \"%s\"}", tupleEntity.getT1(), tupleEntity.getT2()));
                }
            } else {
                resultList.add(String.format("{\"namespace\": \"%s\", \"elementName\": \"%s\"}", tupleEntity.getT1(), tupleEntity.getT2()));
            }
        }
        return ArrayUtils.toString(resultList);
    }

    @ResponseBody
    @RequestMapping(value = "/api/system/schema/upload/{system}/", method = RequestMethod.POST)
    public String uploadSchema(@PathVariable String system,
                               @RequestParam MultipartFile file) throws IOException, SAXException, SaxonApiException {
        File tempFile = new File(file.getOriginalFilename() + "_" + java.lang.System.currentTimeMillis());
        try {
            file.transferTo(tempFile);
            dataFileService.uploadSchema(system, tempFile);
        } finally {
            if (!tempFile.delete()) {
                tempFile.deleteOnExit();
            }
        }
        validationService.reInitValidator(system);
        xsdAnalysisService.reInit(system);
        return "OK!";
    }

    @ResponseBody
    @RequestMapping(value = "/api/system/{systemName}/validate")
    public String validate(@PathVariable String systemName, @RequestParam String message) {
        AjaxObject ajaxObject = new AjaxObject();
        try {
        List<MockMessageValidationException> validationErrors = validationService.validate(message, systemName);
        if (validationErrors.isEmpty()) {
            ajaxObject.setInfo("Valid");
        } else {
            ajaxObject.setError(ValidationUtils.getSolidErrorMessage(validationErrors));
        }
        } catch (Exception e) {
            ajaxObject.setError(e);
        }
        return ajaxObject.toJSON();
    }
}
