package ru.sbt.bpm.mock.spring.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.tuple.Tuple;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.*;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.spring.service.ConfigurationService;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.IntegrationPointNameSuggestionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-bochev-as on 23.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class IntegrationPointApiController {

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    DataFileService dataFileService;

    @Autowired
    IntegrationPointNameSuggestionService suggestionService;

    @RequestMapping(value = "/api/ip/add/", method = RequestMethod.POST)
    public String add(@RequestParam String system,
                      @RequestParam String name,
                      @RequestParam String type,
                      @RequestParam(required = false) Integer delayMs,
                      @RequestParam(required = false, value = "xpathValidatorNamespace[]") String[] xpathValidatorNamespace,
                      @RequestParam(required = false, value = "xpathValidatorElementName[]") String[] xpathValidatorElementName,
                      @RequestParam(required = false) String incomeQueue,
                      @RequestParam(required = false) String outcomeQueue,
                      @RequestParam(required = false) Boolean answerRequired,
                      @RequestParam(required = false) String xsdFile,
                      @RequestParam(required = false) String rootElementNamespace,
                      @RequestParam String rootElementName) throws IOException {
        System systemObject = configContainer.getConfig().getSystems().getSystemByName(system);
        XpathSelector xpathSelector = xpathValidatorNamespace != null ? new XpathSelector(xpathValidatorNamespace, xpathValidatorElementName) : null;
        if (delayMs == null) delayMs = 1000;
        IntegrationPoint integrationPoint =
                new IntegrationPoint(name,
                        type,
                        delayMs,
                        xpathSelector,
                        incomeQueue,
                        outcomeQueue,
                        answerRequired,
                        Tuple.of(incomeQueue, outcomeQueue),
                        xsdFile,
                        new ElementSelector(rootElementNamespace, rootElementName));

        IntegrationPoints integrationPointContainer = systemObject.getIntegrationPoints();
        if (integrationPointContainer == null) {
            integrationPointContainer = new IntegrationPoints();
            systemObject.setIntegrationPoints(integrationPointContainer);
        }

        List<IntegrationPoint> integrationPoints = integrationPointContainer.getIntegrationPoints();
        if (integrationPoints == null) {
            integrationPointContainer.setIntegrationPoints(new ArrayList<IntegrationPoint>());
            integrationPoints = integrationPointContainer.getIntegrationPoints();
        }
        integrationPoints.add(integrationPoint);
        configurationService.saveConfig();
        return "redirect:/";
    }

    @RequestMapping(value = "/api/ip/update/{system}/{name}/", method = RequestMethod.POST)
    public String update(@PathVariable String system,
                         @PathVariable String name,
                         @RequestParam(value = "name") String newIntegrationPointName,
                         @RequestParam String type,
                         @RequestParam(required = false) Integer delayMs,
                         @RequestParam(required = false, value = "xpathValidatorNamespace[]") String[] xpathValidatorNamespace,
                         @RequestParam(required = false, value = "xpathValidatorElementName[]") String[] xpathValidatorElementName,
                         @RequestParam(required = false) String incomeQueue,
                         @RequestParam(required = false) String outcomeQueue,
                         @RequestParam(required = false) Boolean answerRequired,
                         @RequestParam(required = false) String xsdFile,
                         @RequestParam(required = false) String rootElementNamespace,
                         @RequestParam String rootElementName) throws IOException {

        System systemObject = configContainer.getConfig().getSystems().getSystemByName(system);
        IntegrationPoint integrationPoint = systemObject.getIntegrationPoints().getIntegrationPointByName(name);

        if (!integrationPoint.getName().equals(newIntegrationPointName)) {
            integrationPoint.setName(newIntegrationPointName);
            dataFileService.moveDataFiles(system, name, newIntegrationPointName);
        }

        integrationPoint.setIntegrationPointType(type);
        integrationPoint.setDelayMs(delayMs);

        XpathSelector xpathSelector = xpathValidatorNamespace != null ? new XpathSelector(xpathValidatorNamespace, xpathValidatorElementName) : null;
        integrationPoint.setXpathValidatorSelector(xpathSelector);

        integrationPoint.setIncomeQueue(incomeQueue);
        integrationPoint.setOutcomeQueue(outcomeQueue);
        integrationPoint.setAnswerRequired(answerRequired);
        integrationPoint.setXsdFile(xsdFile);

        ElementSelector elementSelector = new ElementSelector(rootElementNamespace, rootElementName);
        integrationPoint.setRootElement(elementSelector);

        configurationService.saveConfig();
        return "redirect:/";
    }

    @RequestMapping(value = "/api/ip/delete/{system}/{name}/", method = RequestMethod.POST)
    public String delete(@PathVariable String system,
                         @PathVariable String name) throws IOException {
        System systemObject = configContainer.getConfig().getSystems().getSystemByName(system);
        List<IntegrationPoint> integrationPoints = systemObject.getIntegrationPoints().getIntegrationPoints();
        for (int i = 0; i < integrationPoints.size(); i++) {
            if (integrationPoints.get(i).getName().equals(name)) {
                integrationPoints.remove(i);
                dataFileService.deleteDataFiles(system, name);
                configurationService.saveConfig();
                return "redirect:/";
            }
        }
        throw new RuntimeException("No such integrationPoint");
    }

}
