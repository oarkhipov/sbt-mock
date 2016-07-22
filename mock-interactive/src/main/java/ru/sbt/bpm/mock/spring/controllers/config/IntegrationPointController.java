package ru.sbt.bpm.mock.spring.controllers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.*;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.spring.service.IntegrationPointNameSuggestionService;

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
    IntegrationPointNameSuggestionService suggestionService;

    @RequestMapping(value = "/ip/add/", method = RequestMethod.GET)
    public String add(Model model,
                      @RequestParam(required = false, value = "system") String systemName) {
        model.addAttribute("systems", configContainer.getConfig().getSystems().getSystems());
        model.addAttribute("systemName", systemName);
        model.addAttribute("integrationPointName", "");
        model.addAttribute("suggestedNames", "");
        model.addAttribute("adding",true);
        return "integrationPoint/form";
    }

    @RequestMapping(value = "/ip/update/{system}/{name}/", method = RequestMethod.GET)
    public String update(@PathVariable final String system,
                         @PathVariable final String name,
                         Model model) throws Exception {
        Systems systems = configContainer.getConfig().getSystems();
        model.addAttribute("systems", systems.getSystems());
        System systemObject = systems.getSystemByName(system);
        assert systemObject != null;
        model.addAttribute("systemName", system);
        IntegrationPoint integrationPoint = systemObject.getIntegrationPoints().getIntegrationPointByName(name);
        assert integrationPoint != null;
        model.addAttribute("integrationPointName", name);
        model.addAttribute("isMock", integrationPoint.isMock());
        model.addAttribute("isDriver", integrationPoint.isDriver());
        model.addAttribute("isAnswerRequired", integrationPoint.getAnswerRequired());
        model.addAttribute("xsdFile", integrationPoint.getXsdFile());
        model.addAttribute("rootElement", integrationPoint.getRootElement());
        model.addAttribute("xpathValidation", integrationPoint.getXpathValidatorSelector() != null ? integrationPoint.getXpathValidatorSelector().getElementSelectors() : null);
        model.addAttribute("adding", false);

        List<String> integrationPointNames = new ArrayList<String>() {{
            add(name);
            addAll(suggestionService.suggestName(configContainer.getSystemByName(system)));
        }};
        model.addAttribute("suggestedNames", integrationPointNames);
        return "integrationPoint/form";
    }
}
