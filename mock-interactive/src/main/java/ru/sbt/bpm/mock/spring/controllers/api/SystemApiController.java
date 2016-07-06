package ru.sbt.bpm.mock.spring.controllers.api;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        System systemObject = configContainer.getConfig().getSystems().getSystemByName(systemName);

        if (!systemObject.getSystemName().equals(systemName)) {
            systemObject.setSystemName(newSystemName);
            dataFileService.moveSystemDir(systemName, newSystemName);
        }

        if (!systemObject.getProtocol().equals(protocol)) {
            systemObject.setProtocol(protocol);
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
            }
        }

        if (mockIncomeQueue != null) {
            if (systemObject.getMockIncomeQueue() != null && !systemObject.getMockIncomeQueue().equals(mockIncomeQueue)) {
                systemObject.setMockIncomeQueue(mockIncomeQueue);
            }
        }

        if (mockOutcomeQueue != null) {
            if (systemObject.getMockOutcomeQueue() != null && !systemObject.getMockOutcomeQueue().equals(mockOutcomeQueue)) {
                systemObject.setMockOutcomeQueue(mockOutcomeQueue);
            }
        }

        if (driverOutcomeQueue != null) {
            if (systemObject.getDriverOutcomeQueue() != null && !systemObject.getDriverOutcomeQueue().equals(driverOutcomeQueue)) {
                systemObject.setDriverOutcomeQueue(driverOutcomeQueue);
            }
        }

        if (driverIncomeQueue != null) {
            if (systemObject.getDriverIncomeQueue() != null && !systemObject.getDriverIncomeQueue().equals(driverIncomeQueue)) {
                systemObject.setDriverIncomeQueue(driverIncomeQueue);
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
        return "redirect:/";
    }

    @RequestMapping(value = "/api/system/delete/{systemName}/", method = RequestMethod.POST)
    public String delete(@PathVariable String systemName) throws IOException {
        Systems systems = configContainer.getConfig().getSystems();
        System system = systems.getSystemByName(systemName);
        dataFileService.deleteSystemDir(systemName);
        systems.getSystems().remove(system);
        configurationService.saveConfig();
        return "redirect:/";
    }

    @ResponseBody
    @RequestMapping(value = "/api/system/reinitValidator/{systemName}/")
    public String reinitValidator(@PathVariable String systemName) throws IOException, SAXException {
        validationService.reInitValidator(systemName);
        return "OK!";
    }

    @ResponseBody
    @RequestMapping(value = "/api/system/reinitValidator/")
    public String reinitValidator() {
        List<System> systems = configContainer.getConfig().getSystems().getSystems();
        for (System system : systems) {
            try {
                validationService.reInitValidator(system.getSystemName());
            } catch (Exception e) {
                return ExceptionUtils.getExceptionStackTrace(e);
            }
        }
        return "OK!";
    }

    @ResponseBody
    @RequestMapping(value = "/api/system/{systemName}/namespaces/")
    public String getNamespaceList(@PathVariable String systemName) {
        return ArrayUtils.toString(xsdAnalysisService.getNamespaceFromXsd(systemName));
    }

    @ResponseBody
    @RequestMapping(value = "/api/system/{systemName}/elements/")
    public String getElementsList(@PathVariable String systemName) {
        //TODO
        return "";
    }
}
