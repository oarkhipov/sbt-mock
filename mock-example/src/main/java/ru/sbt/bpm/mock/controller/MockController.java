package ru.sbt.bpm.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
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
        model.addAttribute("type", "Response");
        model.addAttribute("link", "mock");
        model.addAttribute("list", transformService.getTransformers());
        return "stepForm";
    }

    @RequestMapping(value="/mock/{name}/", method= RequestMethod.GET)
    public String get(@PathVariable("name") String name, Model model) throws IOException {
        model.addAttribute("name", name);
        model.addAttribute("link", "mock");
        model.addAttribute("object", xmlDataService.getXml(name + "_Data"));
        return "editor";
    }

    @RequestMapping(value="/mock/{name}/validate/", method=RequestMethod.POST)
    public String validate(
            @PathVariable("name") String name,
            @RequestParam("xml") String xml,
            ModelMap model) {
        try {
            if (xmlDataService.validate(xml)) {
                model.addAttribute("object", "true");
            }
        } catch (SAXException|IOException e) {
            model.addAttribute("object", e.getMessage());
        }
        return "blank";
    }
}
