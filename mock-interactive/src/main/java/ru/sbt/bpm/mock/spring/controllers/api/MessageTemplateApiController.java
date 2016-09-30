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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.tuple.Tuple3;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.MessageTemplate;
import ru.sbt.bpm.mock.config.entities.MessageTemplates;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.DispatcherTypes;
import ru.sbt.bpm.mock.spring.service.ConfigurationService;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.DispatcherService;

import java.io.IOException;
import java.util.UUID;

/**
 * @author SBT-Bochev-AS on 02.08.2016.
 *         <p>
 *         Company: SBT - Moscow
 */

@Controller
public class MessageTemplateApiController {

    @Autowired
    private MockConfigContainer configContainer;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private DataFileService dataFileService;
    @Autowired
    private DispatcherService dispatcherService;

    @RequestMapping(value = "/api/messageTemplate/add/{systemName}/{integrationPointName}/", method = RequestMethod.POST)
    public String add(@PathVariable String systemName,
                      @PathVariable String integrationPointName,
                      @RequestParam String name,
                      @RequestParam String type,
                      @RequestParam String expression,
                      @RequestParam String regexGroups,
                      @RequestParam String value) throws IOException {
        IntegrationPoint integrationPointByName = configContainer.getIntegrationPointByName(systemName, integrationPointName);
        MessageTemplates messageTemplates = integrationPointByName.getMessageTemplates();
        MessageTemplate messageTemplate = new MessageTemplate(name, DispatcherTypes.valueOf(type), expression, regexGroups, value);
        messageTemplates.getMessageTemplateList().add(messageTemplate);
        configurationService.saveConfig();
        return "redirect:/";
    }

    @RequestMapping(value = "/api/messageTemplate/update/{templateId}/", method = RequestMethod.POST)
    public String update(@PathVariable String templateId,
                         @RequestParam String name,
                         @RequestParam String type,
                         @RequestParam String expression,
                         @RequestParam String regexGroups,
                         @RequestParam String value) throws IOException {
        Tuple3<System, IntegrationPoint, MessageTemplate> entitiesByMessageTemplateUUID = configContainer.getConfigEntitiesByMessageTemplateUUID(UUID.fromString(templateId));
        MessageTemplate messageTemplate = entitiesByMessageTemplateUUID.getT3();
        messageTemplate.setCaption(name);
        messageTemplate.setDispatcherType(DispatcherTypes.valueOf(type));
        messageTemplate.setDispatcherExpression(expression);
        messageTemplate.setRegexGroups(regexGroups);
        messageTemplate.setValue(value);
        configurationService.saveConfig();
        return "redirect:/";
    }

    @RequestMapping(value = "/api/messageTemplate/delete/{templateId}/", method = RequestMethod.POST)
    public String delete(@PathVariable String templateId) throws IOException {
        UUID templateUuid = UUID.fromString(templateId);
        Tuple3<System, IntegrationPoint, MessageTemplate> entitiesByMessageTemplateUUID = configContainer.getConfigEntitiesByMessageTemplateUUID(templateUuid);
        IntegrationPoint integrationPoint = entitiesByMessageTemplateUUID.getT2();
        MessageTemplate messageTemplate = entitiesByMessageTemplateUUID.getT3();
        integrationPoint.getMessageTemplates().getMessageTemplateList().remove(messageTemplate);
        dataFileService.deleteDataFiles(entitiesByMessageTemplateUUID.getT1().getSystemName(), integrationPoint.getName(), messageTemplate.getTemplateId().toString());
        configurationService.saveConfig();
        return "redirect:/";
    }

    @ResponseBody
    @RequestMapping(value = "/api/messageTemplate/check/", method = RequestMethod.POST)
    public String checkDispatchingRule(@RequestParam String payload,
                                       @RequestParam String dispatcherTypeString,
                                       @RequestParam String dispatcherExpression,
                                       @RequestParam String regexGroups,
                                       @RequestParam String value) {
        return String.valueOf(
                dispatcherService.test(payload, DispatcherTypes.valueOf(dispatcherTypeString), dispatcherExpression, regexGroups, value));
    }
}
