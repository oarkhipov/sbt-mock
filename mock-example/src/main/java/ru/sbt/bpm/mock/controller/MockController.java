package ru.sbt.bpm.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.sbt.bpm.mock.service.TransformService;
import ru.sbt.bpm.mock.service.XmlDataService;

import java.io.IOException;

/**
 * Created by sbt-bochev-as on 13.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
@Controller
public class MockController {
    @Autowired
    private TransformService transformService;

    @Autowired
    private XmlDataService xmlDataService;

    @RequestMapping("/mock/")
    public String  getMock(Model model) {
        model.addAttribute("list", transformService.getTransformers());
        return "mock";
    }

    @RequestMapping(value="/mock/{name}/", method= RequestMethod.GET)
    public String get(@PathVariable("name") String name, Model model) throws IOException {
        model.addAttribute("name", name);

        model.addAttribute("object", xmlDataService.getXml(name + "_Data"));
        return "mock_editor";
    }
}
