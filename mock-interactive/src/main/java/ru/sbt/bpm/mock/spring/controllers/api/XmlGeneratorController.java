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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.service.XmlGeneratorService;
import ru.sbt.bpm.mock.utils.AjaxObject;

/**
 * @author sbt-bochev-as on 23.11.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class XmlGeneratorController {

    @Autowired
    XmlGeneratorService generatorService;

    @ResponseBody
    @RequestMapping(value = "/xml/{system}/{integrationPointName}/rq/{filtered}")
    public String generateRq(@PathVariable String system, @PathVariable String integrationPointName,  @PathVariable boolean filtered) {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(system, integrationPointName, MessageType.RQ, filtered));
        } catch (Exception e) {
            ajaxObject.setError(e);
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/xml/{system}/{integrationPointName}/rs/{filtered}")
    public String generateRs(@PathVariable String system, @PathVariable String integrationPointName,  @PathVariable boolean filtered) {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(system, integrationPointName, MessageType.RS, filtered));
        } catch (Exception e) {
            ajaxObject.setError(e);
        }
        return ajaxObject.toJSON();
    }
}
