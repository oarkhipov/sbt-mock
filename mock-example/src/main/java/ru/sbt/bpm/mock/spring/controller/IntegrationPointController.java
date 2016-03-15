package ru.sbt.bpm.mock.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.*;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.generator.spring.integration.Pair;
import ru.sbt.bpm.mock.spring.service.ConfigurationService;
import ru.sbt.bpm.mock.spring.service.DataFileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-bochev-as on 23.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class IntegrationPointController {

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    DataFileService dataFileService;

    @RequestMapping(value = "/ip/add/", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("systems", configContainer.getConfig().getSystems().getSystems());
        model.addAttribute("systemName", "");
        model.addAttribute("integrationPointName", "");
        return "integrationPoint/form";
    }

    @ResponseBody
    @RequestMapping(value = "/ip/add/", method = RequestMethod.POST)
    public String add(@RequestParam String system,
                      @RequestParam String name,
                      @RequestParam String type,
                      @RequestParam(required = false) Integer delayMs,
                      @RequestParam(value = "rqXpathValidatorNamespace[]") String[] rqXpathValidatorNamespace,
                      @RequestParam(value = "rqXpathValidatorElementName[]") String[] rqXpathValidatorElementName,
                      @RequestParam(value = "rsXpathValidatorNamespace[]") String[] rsXpathValidatorNamespace,
                      @RequestParam(value = "rsXpathValidatorElementName[]") String[] rsXpathValidatorElementName,
                      @RequestParam(required = false) String incomeQueue,
                      @RequestParam(required = false) String outcomeQueue,
                      @RequestParam(required = false) Boolean answerRequired,
                      @RequestParam(required = false) String xsdFile,
                      @RequestParam String rootElementNamespace,
                      @RequestParam String rootElementName) throws IOException {
        System systemObject = configContainer.getConfig().getSystems().getSystemByName(system);
        XpathSelector rqXpathSelector = new XpathSelector(rqXpathValidatorNamespace, rqXpathValidatorElementName);
        XpathSelector rsXpathSelector = new XpathSelector(rsXpathValidatorNamespace, rsXpathValidatorElementName);
        if (delayMs == null) delayMs = 1000;
        IntegrationPoint integrationPoint =
                new IntegrationPoint(name,
                        type,
                        delayMs,
                        rqXpathSelector,
                        rsXpathSelector,
                        incomeQueue,
                        outcomeQueue,
                        answerRequired,
                        new Pair<String, String>(incomeQueue, outcomeQueue),
                        xsdFile,
                        new ElementSelector(rootElementNamespace, rootElementName));
        List<IntegrationPoint> integrationPoints = systemObject.getIntegrationPoints().getIntegrationPoints();
        if (integrationPoints == null) {
            systemObject.getIntegrationPoints().setIntegrationPoints(new ArrayList<IntegrationPoint>());
            integrationPoints = systemObject.getIntegrationPoints().getIntegrationPoints();
        }
        integrationPoints.add(integrationPoint);
        configurationService.saveConfig();
        return "OK";
    }

    @RequestMapping(value = "/ip/update/{system}/{name}/", method = RequestMethod.GET)
    public String update(@PathVariable String system,
                         @PathVariable String name,
                         Model model) {
        Systems systems = configContainer.getConfig().getSystems();
        model.addAttribute("systems", systems.getSystems());
        assert systems.getSystemByName(system) != null;
        model.addAttribute("systemName", system);
        IntegrationPoint integrationPoint = systems.getSystemByName(system).getIntegrationPoints().getIntegrationPointByName(name);
        assert integrationPoint != null;
        model.addAttribute("integrationPointName", name);
        model.addAttribute("isMock", integrationPoint.isMock());
        model.addAttribute("isDriver", integrationPoint.isDriver());
        model.addAttribute("isAnswerRequired", integrationPoint.getAnswerRequired());
        model.addAttribute("xsdFile", integrationPoint.getXsdFile());
        model.addAttribute("rootElement", integrationPoint.getRootElement());
        model.addAttribute("xpathValidation", integrationPoint.getRequestXpathValidatorSelector().getElementSelectors());
        return "integrationPoint/form";
    }

    @ResponseBody
    @RequestMapping(value = "/ip/update/{system}/{name}/", method = RequestMethod.POST)
    public String update(@PathVariable String system,
                         @PathVariable String name,
                         @RequestParam(value = "name") String newIntegrationPointName,
                         @RequestParam String type,
                         @RequestParam(required = false) Integer delayMs,
                         @RequestParam(value = "rqXpathValidatorNamespace[]") String[] rqXpathValidatorNamespace,
                         @RequestParam(value = "rqXpathValidatorElementName[]") String[] rqXpathValidatorElementName,
                         @RequestParam(value = "rsXpathValidatorNamespace[]") String[] rsXpathValidatorNamespace,
                         @RequestParam(value = "rsXpathValidatorElementName[]") String[] rsXpathValidatorElementName,
                         @RequestParam(required = false) String incomeQueue,
                         @RequestParam(required = false) String outcomeQueue,
                         @RequestParam(required = false) Boolean answerRequired,
                         @RequestParam(required = false) String xsdFile,
                         @RequestParam String rootElementNamespace,
                         @RequestParam String rootElementName) throws IOException {

        System systemObject = configContainer.getConfig().getSystems().getSystemByName(system);
        IntegrationPoint integrationPoint = systemObject.getIntegrationPoints().getIntegrationPointByName(name);

        if (!integrationPoint.getName().equals(newIntegrationPointName)) {
            integrationPoint.setName(newIntegrationPointName);
            dataFileService.moveDataFiles(system, name, newIntegrationPointName);
        }

        if (!integrationPoint.getIntegrationPointType().equals(type)) {
            integrationPoint.setIntegrationPointType(type);
        }

        if (delayMs != null) {
            if (!integrationPoint.getDelayMs().equals(delayMs)) {
                integrationPoint.setDelayMs(delayMs);
            }
        }

        XpathSelector rqXpathSelector = new XpathSelector(rqXpathValidatorNamespace, rqXpathValidatorElementName);
        if (!integrationPoint.getRequestXpathValidatorSelector().equals(rqXpathSelector)) {
            integrationPoint.setRequestXpathValidatorSelector(rqXpathSelector);
        }

        XpathSelector rsXpathSelector = new XpathSelector(rsXpathValidatorNamespace, rsXpathValidatorElementName);
        if (!integrationPoint.getRequestXpathValidatorSelector().equals(rsXpathSelector)) {
            integrationPoint.setRequestXpathValidatorSelector(rsXpathSelector);
        }

        if (incomeQueue != null) {
            if (!integrationPoint.getIncomeQueue().equals(incomeQueue)) {
                integrationPoint.setIncomeQueue(incomeQueue);
            }
        }

        if (outcomeQueue != null) {
            if (!integrationPoint.getOutcomeQueue().equals(outcomeQueue)) {
                integrationPoint.setOutcomeQueue(outcomeQueue);
            }
        }

        if (answerRequired != null) {
            if (integrationPoint.getAnswerRequired() != answerRequired) {
                integrationPoint.setAnswerRequired(answerRequired);
            }
        }

        if (xsdFile != null) {
            if (!integrationPoint.getXsdFile().equals(xsdFile)) {
                integrationPoint.setXsdFile(xsdFile);
            }
        }

        ElementSelector elementSelector = new ElementSelector(rootElementNamespace, rootElementName);
        if (!integrationPoint.getRootElement().equals(elementSelector)) {
            integrationPoint.setRootElement(elementSelector);
        }

        configurationService.saveConfig();
        return "OK";
    }

    @RequestMapping(value = "/ip/delete/{system}/{name}/", method = RequestMethod.GET)
    public String delete(@PathVariable String system,
                         @PathVariable String name,
                         Model model) {
        model.addAttribute("system", system);
        model.addAttribute("integrationPoint", name);
        return "integrationPoint/delete";
    }

    @ResponseBody
    @RequestMapping(value = "/ip/delete/{system}/{name}/", method = RequestMethod.POST)
    public String delete(@PathVariable String system,
                         @PathVariable String name) throws IOException {
        System systemObject = configContainer.getConfig().getSystems().getSystemByName(system);
        List<IntegrationPoint> integrationPoints = systemObject.getIntegrationPoints().getIntegrationPoints();
        for (int i = 0; i < integrationPoints.size(); i++) {
            if (integrationPoints.get(i).getName().equals(name)) {
                integrationPoints.remove(i);
                dataFileService.deleteDataFiles(system, name);
                configurationService.saveConfig();
                return "OK";
            }
        }
        return "No such integrationPoint";
    }

}
