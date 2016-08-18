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

import net.sf.saxon.s9api.SaxonApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.MainConfig;
import ru.sbt.bpm.mock.spring.service.ConfigurationService;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 * @author sbt-bochev-as on 26.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class ConfigController {

    @Autowired
    MockConfigContainer mockConfigContainer;

    @Autowired
    ConfigurationService configurationService;

    @RequestMapping(value = "/config", produces = "application/xml;charset=UTF-8")
    @ResponseBody
    public String getConfig() throws IOException {
        return mockConfigContainer.toXml();
    }

    @RequestMapping(value = "/config/export", produces = "application/zip")
    @ResponseBody
    public byte[] exportConfig(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"mockConfig.zip\"");
        return configurationService.compressConfiguration();
    }

    @RequestMapping(value = "/config/import", method = RequestMethod.GET)
    public String importConfig() throws IOException {
        return "config/uploadForm";
    }

    @ResponseBody
    @RequestMapping(value = "/config/import", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String importConfig(@RequestParam MultipartFile file) throws IOException, SaxonApiException, JAXBException {
        File tempFile = new File(file.getOriginalFilename() + "_" + System.currentTimeMillis());
        file.transferTo(tempFile);
        configurationService.unzipConfiguration(tempFile);
        if (!tempFile.delete()) {
            tempFile.deleteOnExit();
        }
        return "OK!";
    }

    @RequestMapping(value = "/config/refreshContext", produces = MediaType.TEXT_HTML_VALUE)
    public String refreshContext() throws JAXBException, IOException {
        configurationService.reInitSpringContext();
        return "redirect:/";
    }

    @RequestMapping(value = "/config/updateConfigSettings", method = RequestMethod.GET)
    public String showConfigSettings(Model model) {
        model.addAttribute("config", mockConfigContainer.getConfig().getMainConfig());
        return "config/mainSettings";
    }

    @RequestMapping(value = "/config/updateConfigSettings", method = RequestMethod.POST)
    public String updateConfigSettings(@RequestParam String driverTimeout,
                                       @RequestParam Integer maxLogsCount,
                                       @RequestParam(required = false) Boolean  validationEnabled) throws JAXBException, IOException {
        MockConfig config = mockConfigContainer.getConfig();
        boolean reInitNeeded = false;
        MainConfig mainConfig = config.getMainConfig();
        if (mainConfig == null) {
            config.setMainConfig(new MainConfig());
            mainConfig = config.getMainConfig();
        }

        if (mainConfig.getDriverTimeout() == null || !mainConfig.getDriverTimeout().equals(driverTimeout)) {
            mainConfig.setDriverTimeout(driverTimeout);
            reInitNeeded = true;
        }

        mainConfig.setMaxLogsCount(maxLogsCount);
        if (validationEnabled != null) {
            mainConfig.setValidationEnabled(validationEnabled);
        } else {
            mainConfig.setValidationEnabled(false);
        }

        configurationService.saveConfig();

        if (reInitNeeded) {
            configurationService.reInitSpringContext();
        }
        return "redirect:/";
    }
}
