package ru.sbt.bpm.mock.spring.controllers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.service.ConfigurationService;

import javax.servlet.http.HttpServletResponse;
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
    public String importConfig(Model model) throws IOException {
        return "config/form";
    }

    @RequestMapping(value = "/config/import", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String importConfig(@RequestParam MultipartFile file) throws IOException {
        File tempFile = new File(file.getOriginalFilename() + "_" + System.currentTimeMillis());
        file.transferTo(tempFile);
        configurationService.unzipConfiguration(tempFile);
        tempFile.delete();
        return "OK";
    }
}