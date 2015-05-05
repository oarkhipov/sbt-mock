package ru.sbt.bpm.mock.spring.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.spring.bean.TemplateEngineBean;
import ru.sbt.bpm.mock.spring.bean.impl.TemplateEngineBeanImpl;
import ru.sbt.bpm.mock.spring.integration.service.XmlDataService;



/**
 *
 * @author sbt-barinov-sv
 */
@Controller
public class DefaultController {

    @Autowired
    XmlDataService xmlDataService;

    @Autowired
    TemplateEngineBean templateEngineBean;

    @RequestMapping("/")
    public String _default(Model model) {
        List<String> functions = new LinkedList<String>();
        functions.add("mock");
        functions.add("driver");
//        functions.add("");
//        functions.add("FrameMock");
//        functions.add("transform");
//        functions.add("channel");
        model.addAttribute("list", functions);
        return "form";
    }

    /**
     * @return integration info
     */
    @RequestMapping(value = "/info", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody String getInfo() throws IOException {
        return FileUtils.readFileToString(xmlDataService.getResource("mockapp-servlet.xml").getFile(),"UTF-8");
    }

    @RequestMapping(value = "/prop", produces = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody String getProp() throws IOException {
        return FileUtils.readFileToString(xmlDataService.getResource("../META-INF/maven/ru.sbt.bpm.mock/mock-interactive-by/pom.properties").getFile(),"UTF-8");
    }


    @RequestMapping(value = "/templateInfo", produces = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody String getTemplateInfo() {
        return templateEngineBean.htmlInfo();
    }


}
