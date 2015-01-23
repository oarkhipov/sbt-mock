package ru.sbt.bpm.mock.controller;

import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author sbt-barinov-sv
 */
@Controller
public class DefaultController {
    @RequestMapping("/")
    public String _default(Model model) {
        List<String> functions = new LinkedList<String>();
        functions.add("mock");
        functions.add("driver");
        functions.add("");
        functions.add("FrameMock");
//        functions.add("transform");
//        functions.add("channel");
        model.addAttribute("list", functions);
        return "form";
    }
}
