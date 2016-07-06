package ru.sbt.bpm.mock.spring.controllers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.entities.Systems;

/**
 * @author sbt-bochev-as on 23.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class SystemController {

    @Autowired
    MockConfigContainer configContainer;

    @RequestMapping(value = "/api/system/add/", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("system", new System());
        return "system/form";
    }

    @RequestMapping(value = "/api/system/update/{system}/", method = RequestMethod.GET)
    public String update(@PathVariable String system,
                         Model model) {
        Systems systems = configContainer.getConfig().getSystems();
        System systemByName = systems.getSystemByName(system);
        assert systemByName != null;
        model.addAttribute("system", systemByName);
        return "system/form";
    }
}
