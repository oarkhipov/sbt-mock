package ru.sbt.bpm.mock.spring.controller;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.service.DataService;

/**
 *
 * @author sbt-barinov-sv
 */
@Controller
public class DefaultController {

    @Autowired
    MockConfigContainer mockConfigContainer;

    @Autowired
    DataService dataService;

    @RequestMapping("/")
    public String _default(Model model) {
        model.addAttribute("list", mockConfigContainer.getConfig().getSystems());
        return "stepForm";
    }

    /**
     * @return integration info
     */
    @RequestMapping(value = "/info", produces = "application/xml;charset=UTF-8")
    public @ResponseBody String getInfo() throws IOException {
        return FileUtils.readFileToString(dataService.getResource("mockapp-servlet.xml").getFile(), "UTF-8");
    }

    @RequestMapping(value = "/config", produces = "application/xml;charset=UTF-8")
    public @ResponseBody String getConfig() throws IOException {
        return mockConfigContainer.toXml();
    }

    @RequestMapping(value = "/prop", produces = "text/html;charset=UTF-8")
    public @ResponseBody String getProp() throws IOException {
        return FileUtils.readFileToString(dataService.getResource("../META-INF/maven/ru.sbt.bpm.mock/mock-interactive-by/pom.properties").getFile(),"UTF-8");
    }

}
