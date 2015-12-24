package ru.sbt.bpm.mock.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.*;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.generator.spring.integration.Pair;

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
                      @RequestParam String operationName,
                      @RequestParam String[] xpathValidatorNamespace,
                      @RequestParam String[] xpathValidatorElementName,
                      @RequestParam String incomeQueue,
                      @RequestParam String outcomeQueue,
                      @RequestParam Boolean answerRequired,
                      @RequestParam String xsdFile,
                      @RequestParam String rootElementNamespace,
                      @RequestParam String rootElementName) {
        System systemObject = configContainer.getConfig().getSystems().getSystemByName(system);
        XpathSelector xpathSelector = new XpathSelector(xpathValidatorNamespace, xpathValidatorElementName);

        IntegrationPoint integrationPoint =
                new IntegrationPoint(name,
                        operationName,
                        type,
                        xpathSelector,
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
        model.addAttribute("xpathValidation", integrationPoint.getXpathValidatorSelector().getElementSelectors());
        return "integrationPoint/form";
    }

    @ResponseBody
    @RequestMapping(value = "/ip/update/{system}/{name}/", method = RequestMethod.POST)
    public String update(@PathVariable String system,
                         @PathVariable String name,
                         @RequestParam String type,
                         @RequestParam(required = false) String operationName,
                         @RequestParam(value = "xpathValidatorNamespace[]") String[] xpathValidatorNamespace,
                         @RequestParam(value = "xpathValidatorElementName[]") String[] xpathValidatorElementName,
                         @RequestParam(required = false) String incomeQueue,
                         @RequestParam(required = false) String outcomeQueue,
                         @RequestParam(required = false) Boolean answerRequired,
                         @RequestParam(required = false) String xsdFile,
                         @RequestParam String rootElementNamespace,
                         @RequestParam String rootElementName) {

        System systemObject = configContainer.getConfig().getSystems().getSystemByName(system);
        IntegrationPoint integrationPoint = systemObject.getIntegrationPoints().getIntegrationPointByName(name);

        if (!integrationPoint.getIntegrationPointType().equals(type)) {
            integrationPoint.setIntegrationPointType(type);
        }

        if(operationName!=null)
        if (!integrationPoint.getOperationName().equals(operationName)) {
            integrationPoint.setOperationName(operationName);
        }

        XpathSelector xpathSelector = new XpathSelector(xpathValidatorNamespace, xpathValidatorElementName);
        if (!integrationPoint.getXpathValidatorSelector().equals(xpathSelector)) {
            integrationPoint.setXpathValidatorSelector(xpathSelector);
        }

        if(incomeQueue!=null)
        if (!integrationPoint.getIncomeQueue().equals(incomeQueue)) {
            integrationPoint.setIncomeQueue(incomeQueue);
        }

        if (outcomeQueue!=null)
        if (!integrationPoint.getOutcomeQueue().equals(outcomeQueue)) {
            integrationPoint.setOutcomeQueue(outcomeQueue);
        }

        if(answerRequired!=null)
        if (integrationPoint.getAnswerRequired() != answerRequired) {
            integrationPoint.setAnswerRequired(answerRequired);
        }

        if (xsdFile!=null)
        if (!integrationPoint.getXsdFile().equals(xsdFile)) {
            integrationPoint.setXsdFile(xsdFile);
        }

        ElementSelector elementSelector = new ElementSelector(rootElementNamespace, rootElementName);
        if (!integrationPoint.getRootElement().equals(elementSelector)) {
            integrationPoint.setRootElement(elementSelector);
        }
        return "OK";
    }

    @ResponseBody
    @RequestMapping(value = "/ip/delete/{system}/{name}/")
    public String delete(@PathVariable String system,
                         @PathVariable String name) {
        System systemObject = configContainer.getConfig().getSystems().getSystemByName(system);
        List<IntegrationPoint> integrationPoints = systemObject.getIntegrationPoints().getIntegrationPoints();
        for (int i = 0; i < integrationPoints.size(); i++) {
            if (integrationPoints.get(i).getName().equals(name)) {
                integrationPoints.remove(i);
                return "OK";
            }
        }
        return "No such integrationPoint";
    }

}
