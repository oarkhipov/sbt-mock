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

package ru.sbt.bpm.mock.spring.controllers.form;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.service.DataFileService;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 * @author sbt-barinov-sv
 */
@Slf4j
@Controller
public class DefaultController {

    @Autowired
    MockConfigContainer mockConfigContainer;

    @Autowired
    DataFileService dataFileService;

    @Autowired
    String version;

    @RequestMapping("/")
    public String _default(Model model) throws JAXBException, IOException {
        model.addAttribute("list", mockConfigContainer.getConfig().getSystems());
        if (mockConfigContainer.getConfig().getMainConfig() != null) {
            model.addAttribute("globalValidation", mockConfigContainer.getConfig().getMainConfig().getValidationEnabled());
        }
        model.addAttribute("version", version);
        return "stepForm";
    }

    /**
     * @return integration info
     */
    @RequestMapping(value = "/info", produces = "application/xml;charset=UTF-8")
    public
    @ResponseBody
    String getInfo() throws IOException {
        return FileUtils.readFileToString(new File(dataFileService.getContextFilePath("mockapp-servlet.xml")), "UTF-8");
    }

    @RequestMapping(value = "/prop", produces = "text/html;charset=UTF-8")
    public
    @ResponseBody
    String getProp() throws IOException {
        return FileUtils.readFileToString(new File(dataFileService.getContextFilePath("../META-INF/maven/ru.sbt.bpm.mock/mock-interactive/pom.properties")), "UTF-8");
    }

}
