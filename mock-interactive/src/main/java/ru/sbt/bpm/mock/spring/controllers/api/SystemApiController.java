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
import ru.sbt.bpm.mock.spring.utils.ExceptionUtils;

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
    MockConfigContainer configContainer;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    DataFileService dataFileService;

    @Autowired
    MessageValidationService validationService;

    @Autowired
    XsdAnalysisService xsdAnalysisService;

    @RequestMapping(value = "/api/system/add/", method = RequestMethod.POST)
    public String add(@RequestParam String name,
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
                      @RequestParam(value = "rootElementNamespace", required = false) String rootElementNamespace,
                      @RequestParam(value = "rootElementName", required = false) String rootElementName) throws IOException, SAXException {
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

        if (systems.getSystems() == null) {
            systems.setSystems(new ArrayList<System>());
        }
        systems.getSystems().add(system);
        validationService.reInitValidator(name);
        configurationService.saveConfig();
        if (protocol == Protocol.JMS) {
            configurationService.reInitSpringContext();
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/api/system/update/{systemName}/", method = RequestMethod.POST)
    public String update(@PathVariable String systemName,
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
                         @RequestParam(value = "rootElementNamespace", required = false) String rootElementNamespace,
                         @RequestParam(value = "rootElementName", required = false) String rootElementName) throws IOException, SAXException {

        boolean needToReInitSpringContext = false;
        System systemObject = configContainer.getConfig().getSystems().getSystemByName(systemName);

        if (!systemObject.getSystemName().equals(systemName)) {
            systemObject.setSystemName(newSystemName);
            dataFileService.moveSystemDir(systemName, newSystemName);
        }

        if (!systemObject.getProtocol().equals(protocol)) {
            systemObject.setProtocol(protocol);
            if (protocol == Protocol.JMS) {
                needToReInitSpringContext = true;
            }
        }

        if (rootSchema != null) {
            if (systemObject.getRemoteRootSchema() != null && !systemObject.getRemoteRootSchema().equals(rootSchema)) {
                systemObject.setRemoteRootSchema(rootSchema);
            }
        }

        String localRootSchema = null;
        if (rootSchema != null) {
            String rootSchemaLowerCase = rootSchema.toLowerCase();
            if (!rootSchemaLowerCase.startsWith("http://") && !rootSchemaLowerCase.startsWith("ftp://")) {
                localRootSchema = rootSchema;
            }
        }

        if (localRootSchema != null) {
            if (systemObject.getLocalRootSchema() != null && !systemObject.getLocalRootSchema().equals(localRootSchema)) {
                systemObject.setLocalRootSchema(localRootSchema);
            }
        }

        XpathSelector xpathSelector = null;
        if (integrationPointSelectorNamespace != null && integrationPointSelectorElementName != null) {
            xpathSelector = new XpathSelector(integrationPointSelectorNamespace, integrationPointSelectorElementName);
        }

        if (systemObject.getIntegrationPointSelector() != null && !systemObject.getIntegrationPointSelector().equals(xpathSelector)) {
            systemObject.setIntegrationPointSelector(xpathSelector);
        }

        if (queueConnectionFactory != null) {
            if (systemObject.getQueueConnectionFactory() != null && !systemObject.getQueueConnectionFactory().equals(queueConnectionFactory)) {
                systemObject.setQueueConnectionFactory(queueConnectionFactory);
                needToReInitSpringContext = true;
            }
        }

        if (mockIncomeQueue != null) {
            if (systemObject.getMockIncomeQueue() != null && !systemObject.getMockIncomeQueue().equals(mockIncomeQueue)) {
                systemObject.setMockIncomeQueue(mockIncomeQueue);
                needToReInitSpringContext = true;
            }
        }

        if (mockOutcomeQueue != null) {
            if (systemObject.getMockOutcomeQueue() != null && !systemObject.getMockOutcomeQueue().equals(mockOutcomeQueue)) {
                systemObject.setMockOutcomeQueue(mockOutcomeQueue);
                needToReInitSpringContext = true;
            }
        }

        if (driverOutcomeQueue != null) {
            if (systemObject.getDriverOutcomeQueue() != null && !systemObject.getDriverOutcomeQueue().equals(driverOutcomeQueue)) {
                systemObject.setDriverOutcomeQueue(driverOutcomeQueue);
                needToReInitSpringContext = true;
            }
        }

        if (driverIncomeQueue != null) {
            if (systemObject.getDriverIncomeQueue() != null && !systemObject.getDriverIncomeQueue().equals(driverIncomeQueue)) {
                systemObject.setDriverIncomeQueue(driverIncomeQueue);
                needToReInitSpringContext = true;
            }
        }

        if (driverWebServiceEndpoint != null) {
            if (systemObject.getDriverWebServiceEndpoint() != null && !systemObject.getDriverWebServiceEndpoint().equals(driverWebServiceEndpoint)) {
                systemObject.setDriverWebServiceEndpoint(driverWebServiceEndpoint);
            }
        }

        if (rootElementNamespace != null && rootElementName != null) {
            ElementSelector rootElement = new ElementSelector(rootElementNamespace, rootElementName);
            if (!systemObject.getRootElement().equals(rootElement)) {
                systemObject.setRootElement(rootElement);
            }
        }

        validationService.reInitValidator(systemName);
        configurationService.saveConfig();
        if (needToReInitSpringContext) {
            configurationService.reInitSpringContext();
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/api/system/delete/{systemName}/", method = RequestMethod.POST)
    public String delete(@PathVariable String systemName) throws IOException {
        Systems systems = configContainer.getConfig().getSystems();
        System system = systems.getSystemByName(systemName);
        Protocol protocol = system.getProtocol();
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
        xsdAnalysisService.reInit();
        return "OK!";
    }

    @ResponseBody
    @RequestMapping(value = "/api/system/reinitValidator/")
    public String reinitValidator() {
        List<System> systems = configContainer.getConfig().getSystems().getSystems();
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
        file.transferTo(tempFile);
        dataFileService.uploadSchema(system, tempFile);
        if (!tempFile.delete()) {
            tempFile.deleteOnExit();
        }
        validationService.reInitValidator(system);
        xsdAnalysisService.reInit(system);
        return "OK!";
    }
}
