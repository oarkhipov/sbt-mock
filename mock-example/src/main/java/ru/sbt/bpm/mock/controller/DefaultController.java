package ru.sbt.bpm.mock.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.service.XmlDataService;



/**
 *
 * @author sbt-barinov-sv
 */
@Controller
public class DefaultController {

    @Autowired
    XmlDataService xmlDataService;

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
    @RequestMapping(value = "/info", produces = "application/xml;charset=UTF-8")
    public @ResponseBody String getInfo() throws IOException {
        return FileUtils.readFileToString(xmlDataService.getResource("mockapp-servlet.xml").getFile(),"UTF-8");
    }
}
