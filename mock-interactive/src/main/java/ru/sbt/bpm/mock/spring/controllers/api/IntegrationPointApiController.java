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

import ru.sbt.bpm.mock.config.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.tuple.Tuple;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.spring.service.ConfigurationService;
import ru.sbt.bpm.mock.spring.service.DataFileService;

import java.io.IOException;
import java.util.Set;

/**
 * @author sbt-bochev-as on 23.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class IntegrationPointApiController {

    @Autowired
    private MockConfigContainer configContainer;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private DataFileService dataFileService;

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
                      @RequestParam String rootElementName,
                      @RequestParam(required = false) boolean enabled,
                      @RequestParam(required = false) Boolean validationEnabled,
                      @RequestParam(required = false) Boolean sequenceEnabled) throws IOException {
        System systemObject = configContainer.getConfig().getSystems().getSystemByName(system);
        XpathSelector xpathSelector = xpathValidatorNamespace != null ? new XpathSelector(xpathValidatorNamespace, xpathValidatorElementName) : null;
        if (delayMs == null) delayMs = 1000;
        if (answerRequired == null) answerRequired = false;
        IntegrationPoint newIntegrationPoint =
                new IntegrationPoint(name,
                        type,
                        delayMs,
                        xpathSelector,
                        incomeQueue,
                        outcomeQueue,
                        answerRequired,
                        Tuple.of(incomeQueue, outcomeQueue),
                        xsdFile,
                        new ElementSelector(rootElementNamespace, rootElementName),
                        sequenceEnabled);

        newIntegrationPoint.setEnabled(enabled);

        if (validationEnabled != null) {
            newIntegrationPoint.setValidationEnabled(validationEnabled);
        }

        IntegrationPoints integrationPoints = systemObject.getIntegrationPoints();
        if (integrationPoints == null) {
            integrationPoints = new IntegrationPoints();
            systemObject.setIntegrationPoints(integrationPoints);
        }

        Set<IntegrationPoint> integrationPointSet = integrationPoints.getIntegrationPoints();
        if (integrationPointSet == null) {
            integrationPoints.setIntegrationPoints(IntegrationPoints.initSet());
            integrationPointSet = integrationPoints.getIntegrationPoints();
        }
        integrationPointSet.add(newIntegrationPoint);
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
                         @RequestParam String rootElementName,
                         @RequestParam(required = false) boolean validationEnabled,
                         @RequestParam(required = false) boolean enabled,
                         @RequestParam(required = false) boolean sequenceEnabled) throws IOException {

        System systemObject = configContainer.getConfig().getSystems().getSystemByName(system);
        IntegrationPoint integrationPoint = systemObject.getIntegrationPoints().getIntegrationPointByName(name);

        if (answerRequired == null) answerRequired = false;

        if (!integrationPoint.getName().equals(newIntegrationPointName)) {
            integrationPoint.setName(newIntegrationPointName);
            dataFileService.moveDataFiles(system, name, newIntegrationPointName);
        }

        integrationPoint.setEnabled(enabled);

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

        integrationPoint.setValidationEnabled(validationEnabled);

        integrationPoint.setSequenceEnabled(sequenceEnabled);

        configurationService.saveConfig();
        return "redirect:/";
    }

    @RequestMapping(value = "/api/ip/delete/{system}/{name}/", method = RequestMethod.POST)
    public String delete(@PathVariable String system,
                         @PathVariable String name) throws IOException {
        System systemObject = configContainer.getConfig().getSystems().getSystemByName(system);
        Set<IntegrationPoint> integrationPoints = systemObject.getIntegrationPoints().getIntegrationPoints();
        for (IntegrationPoint integrationPoint : integrationPoints) {
            if (integrationPoint.getName().equals(name)) {
                integrationPoints.remove(integrationPoint);
                dataFileService.deleteDataFiles(system, name);
                configurationService.saveConfig();
                return "redirect:/";
            }
        }
        throw new RuntimeException("No such integrationPoint");
    }

}
