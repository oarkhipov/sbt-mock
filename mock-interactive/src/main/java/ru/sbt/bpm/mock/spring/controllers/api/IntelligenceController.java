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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.spring.service.JndiNameService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author sbt-bochev-as on 05.07.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Controller
public class IntelligenceController {

    @Autowired
    JndiNameService jndiService;

    @Autowired
    MockConfigContainer configContainer;

    @ResponseBody
    @RequestMapping(value = "/api/checkJndi/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String checkJndi(@RequestParam String name) {
        return "{\"result\": " + String.valueOf(jndiService.isExist(name)) + "}";
    }

    @ResponseBody
    @RequestMapping(value = "/api/getJndi/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllJndi(@RequestParam(required = false) String substring) {
        Set<String> allJndi = new TreeSet<String>();
        Set<ru.sbt.bpm.mock.config.entities.System> systems = configContainer.getConfig().getSystems().getSystems();
        for (System system : systems) {
            allJndi.add(system.getQueueConnectionFactory());
            allJndi.add(system.getMockIncomeQueue());
            allJndi.add(system.getMockOutcomeQueue());
            allJndi.add(system.getDriverOutcomeQueue());
            allJndi.add(system.getDriverIncomeQueue());
        }

        allJndi.removeAll(new HashSet<String>() {{
            add("");
        }});
        if (substring != null && !substring.isEmpty()) {
            return makeJsonArray(getSubTreeSet(allJndi, substring), "value");
        } else {
            return makeJsonArray(allJndi, "value");
        }
    }

    public String makeJsonArray(Set<?> valueSet, String propertyName) {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (Object o : valueSet) {
            stringBuilder.append("{\"").append(propertyName).append("\": ").append("\"").append(o.toString()).append("\"},");
        }
        stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private Set<String> getSubTreeSet(Set<String> set, String substring) {
        Set<String> resultSet = new TreeSet<String>();
        String lowerCaseSubstring = substring.toLowerCase();
        for (String s : set) {
            if (s.toLowerCase().contains(lowerCaseSubstring)) {
                resultSet.add(s);
            }
        }
        return resultSet;
    }
}
