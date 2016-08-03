package ru.sbt.bpm.mock.spring.controllers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.tuple.Tuple3;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.MessageTemplate;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.DispatcherTypes;

import java.util.UUID;

/**
 * @author SBT-Bochev-AS on 02.08.2016.
 *         <p>
 *         Company: SBT - Moscow
 */
@Controller
public class MessageTemplateController {
    MockConfigContainer configContainer;

    @Autowired
    public MessageTemplateController(MockConfigContainer configContainer) {
        this.configContainer = configContainer;
    }

    @RequestMapping(value = "/messageTemplate/add/{systemName}/{integrationPointName}/", method = RequestMethod.GET)
    public String add(@PathVariable String systemName,
                      @PathVariable String integrationPointName,
                      Model model) {
        model.addAttribute("systemName", systemName);
        model.addAttribute("integrationPointName", integrationPointName);
        model.addAttribute("dispatcherTypes", DispatcherTypes.values());
        model.addAttribute("template", null);
        return "messageTemplate/form";
    }

    @RequestMapping(value = "/messageTemplate/update/{templateId}/", method = RequestMethod.GET)
    public String update(@PathVariable String templateId,
                         Model model) {

        Tuple3<System, IntegrationPoint, MessageTemplate> entitiesByMessageTemplateUUID = configContainer.getConfigEntitiesByMessageTemplateUUID(UUID.fromString(templateId));
        MessageTemplate messageTemplate = entitiesByMessageTemplateUUID.getT3();

        model.addAttribute("systemName", entitiesByMessageTemplateUUID.getT1().getSystemName());
        model.addAttribute("integrationPointName", entitiesByMessageTemplateUUID.getT2().getName());
        model.addAttribute("dispatcherTypes", DispatcherTypes.values());
        model.addAttribute("template", messageTemplate);
        return "messageTemplate/form";
    }
}
