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

    @RequestMapping(value = "/system/add/", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("system", null);
        return "system/form";
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

    @RequestMapping(value = "/system/schema/upload/{system}/", method = RequestMethod.GET)
    public String uploadSchema(@PathVariable String system,
                         Model model) {
        Systems systems = configContainer.getConfig().getSystems();
        System systemByName = systems.getSystemByName(system);
        if (systemByName != null) {
            model.addAttribute("system", systemByName);
        }
        return "system/schemaUpload";
    }
}
