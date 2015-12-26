package ru.sbt.bpm.mock.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.service.DataFileService;

import javax.servlet.http.HttpServletResponse;
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
    DataFileService dataFileService;

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
        return dataFileService.compressConfiguration();
    }
}
