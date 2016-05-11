package ru.sbt.bpm.mock.spring.controllers.form;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.service.DataFileService;

import javax.annotation.PostConstruct;
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
    ApplicationContext applicationContext;

    @PostConstruct
    private void init() throws IOException {
        log.info("BasePath: " + applicationContext.getResource("").getFile().getAbsolutePath());
    }

    @RequestMapping("/")
    public String _default(Model model) {
        model.addAttribute("list", mockConfigContainer.getConfig().getSystems());
        return "stepForm";
    }

    /**
     * @return integration info
     */
    @RequestMapping(value = "/info", produces = "application/xml;charset=UTF-8")
    public
    @ResponseBody
    String getInfo() throws IOException {
        return FileUtils.readFileToString(new File(dataFileService.getPathBaseFilePath("mockapp-servlet.xml")), "UTF-8");
    }

    @RequestMapping(value = "/prop", produces = "text/html;charset=UTF-8")
    public
    @ResponseBody
    String getProp() throws IOException {
        return FileUtils.readFileToString(new File(dataFileService.getPathBaseFilePath("../META-INF/maven/ru.sbt.bpm.mock/mock-interactive-by/pom.properties")), "UTF-8");
    }

}