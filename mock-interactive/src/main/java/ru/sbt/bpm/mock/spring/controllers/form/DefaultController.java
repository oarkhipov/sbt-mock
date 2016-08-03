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
