package ru.sbt.bpm.mock.spring.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.tuple.Tuple3;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.MessageTemplate;
import ru.sbt.bpm.mock.config.entities.MessageTemplates;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.DispatcherTypes;
import ru.sbt.bpm.mock.spring.service.ConfigurationService;
import ru.sbt.bpm.mock.spring.service.DataFileService;

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
}
