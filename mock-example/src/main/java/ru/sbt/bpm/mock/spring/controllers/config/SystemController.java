package ru.sbt.bpm.mock.spring.controllers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.entities.Systems;
import ru.sbt.bpm.mock.config.entities.XpathSelector;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.service.DataFileService;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author sbt-bochev-as on 23.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class SystemController {

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    DataFileService dataFileService;

    @RequestMapping(value = "/system/add/", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("system", new System());
        return "system/form";
    }

    @ResponseBody
    @RequestMapping(value = "/system/add/", method = RequestMethod.POST)
    public String add(@RequestParam String name,
                      @RequestParam Protocol type,
                      @RequestParam String remoteRootSchema,
                      @RequestParam String localRootSchema,
                      @RequestParam(value = "integrationPointSelectorNamespace[]") String[] integrationPointSelectorNamespace,
                      @RequestParam(value = "integrationPointSelectorElementName[]") String[] integrationPointSelectorElementName,
                      @RequestParam(required = false) String queueConnectionFactory,
                      @RequestParam(required = false) String mockIncomeQueue,
                      @RequestParam(required = false) String mockOutcomeQueue,
                      @RequestParam(required = false) String driverOutcomeQueue,
                      @RequestParam(required = false) String driverIncomeQueue,
                      @RequestParam(required = false) String driverWebServiceEndpoint) {
        Systems systems = configContainer.getConfig().getSystems();
        XpathSelector xpathSelector = new XpathSelector(integrationPointSelectorNamespace, integrationPointSelectorElementName);

        System system = new System(name, remoteRootSchema, localRootSchema, xpathSelector, type, queueConnectionFactory, mockIncomeQueue,
                mockOutcomeQueue, driverOutcomeQueue, driverIncomeQueue, driverWebServiceEndpoint, null);

        if (systems.getSystems() == null) {
            systems.setSystems(new ArrayList<System>());
        }
        systems.getSystems().add(system);
        return "OK";
    }

    @RequestMapping(value = "/system/update/{system}/", method = RequestMethod.GET)
    public String update(@PathVariable String system,
                         Model model) {
        Systems systems = configContainer.getConfig().getSystems();
        System systemByName = systems.getSystemByName(system);
        assert systemByName != null;
        model.addAttribute("system", systemByName);
        return "system/form";
    }

    @ResponseBody
    @RequestMapping(value = "/system/update/{systemName}/", method = RequestMethod.POST)
    public String update(@PathVariable String systemName,
                         @RequestParam(value = "name") String newSystemName,
                         @RequestParam Protocol protocol,
                         @RequestParam String remoteRootSchema,
                         @RequestParam String localRootSchema,
                         @RequestParam(value = "integrationPointSelectorNamespace[]") String[] integrationPointSelectorNamespace,
                         @RequestParam(value = "integrationPointSelectorElementName[]") String[] integrationPointSelectorElementName,
                         @RequestParam(required = false) String queueConnectionFactory,
                         @RequestParam(required = false) String mockIncomeQueue,
                         @RequestParam(required = false) String mockOutcomeQueue,
                         @RequestParam(required = false) String driverOutcomeQueue,
                         @RequestParam(required = false) String driverIncomeQueue,
                         @RequestParam(required = false) String driverWebServiceEndpoint) throws IOException {

        System systemObject = configContainer.getConfig().getSystems().getSystemByName(systemName);

        if (!systemObject.getSystemName().equals(systemName)) {
            systemObject.setSystemName(newSystemName);
            //TODO move system directory
            dataFileService.moveSystemDir(systemName, newSystemName);
        }

        if (!systemObject.getProtocol().equals(protocol)) {
            systemObject.setProtocol(protocol);
        }

        if (remoteRootSchema != null) {
            if (!systemObject.getRemoteRootSchema().equals(remoteRootSchema)) {
                systemObject.setRemoteRootSchema(remoteRootSchema);
            }
        }

        if (localRootSchema != null) {
            if (!systemObject.getLocalRootSchema().equals(localRootSchema)) {
                systemObject.setLocalRootSchema(localRootSchema);
            }
        }

        XpathSelector xpathSelector = new XpathSelector(integrationPointSelectorNamespace, integrationPointSelectorElementName);
        if (!systemObject.getIntegrationPointSelector().equals(xpathSelector)) {
            systemObject.setIntegrationPointSelector(xpathSelector);
        }

        if (queueConnectionFactory != null) {
            if (!systemObject.getQueueConnectionFactory().equals(queueConnectionFactory)) {
                systemObject.setQueueConnectionFactory(queueConnectionFactory);
            }
        }

        if (mockIncomeQueue != null) {
            if (!systemObject.getMockIncomeQueue().equals(mockIncomeQueue)) {
                systemObject.setMockIncomeQueue(mockIncomeQueue);
            }
        }

        if (mockOutcomeQueue != null) {
            if (!systemObject.getMockOutcomeQueue().equals(mockOutcomeQueue)) {
                systemObject.setMockOutcomeQueue(mockOutcomeQueue);
            }
        }

        if (driverOutcomeQueue != null) {
            if (!systemObject.getDriverOutcomeQueue().equals(driverOutcomeQueue)) {
                systemObject.setDriverOutcomeQueue(driverOutcomeQueue);
            }
        }

        if (driverIncomeQueue != null) {
            if (!systemObject.getDriverIncomeQueue().equals(driverIncomeQueue)) {
                systemObject.setDriverIncomeQueue(driverIncomeQueue);
            }
        }

        if (driverWebServiceEndpoint != null) {
            if (!systemObject.getDriverWebServiceEndpoint().equals(driverWebServiceEndpoint)) {
                systemObject.setDriverWebServiceEndpoint(driverWebServiceEndpoint);
            }
        }

        return "OK";
    }

    @RequestMapping(value = "/system/delete/{system}/", method = RequestMethod.GET)
    public String delete(@PathVariable String system,
                         Model model) {
        model.addAttribute("system", system);
        return "integrationPoint/delete";
    }

    @ResponseBody
    @RequestMapping(value = "/ip/delete/{systemName}/", method = RequestMethod.POST)
    public String delete(@PathVariable String systemName) throws IOException {
        Systems systems = configContainer.getConfig().getSystems();
        System system = systems.getSystemByName(systemName);
        dataFileService.deleteSystemDir(systemName);
        systems.getSystems().remove(system);
        return "OK";
    }

}
