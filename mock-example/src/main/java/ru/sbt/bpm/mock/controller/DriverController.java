package ru.sbt.bpm.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.sbt.bpm.mock.service.TransformService;
import ru.sbt.bpm.mock.service.XmlDataService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by sbt-bochev-as on 15.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
@Controller
public class DriverController {

    @Autowired
    XmlDataService xmlDataService;

    @Autowired
    ApplicationContext appContext;

    @RequestMapping("/driver/")
    public String  getDriver(Model model) throws IOException {
        model.addAttribute("type", "Request");
//        List of drivers
        Path filePath = appContext.getResource("/WEB-INF/driverList.txt").getFile().toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        model.addAttribute("link", "driver");
        model.addAttribute("list", stringList);
        return "stepForm";
    }

    @RequestMapping(value="/driver/{name}/", method= RequestMethod.GET)
    public String get(@PathVariable("name") String name, Model model) throws IOException {
        model.addAttribute("name", name);
        model.addAttribute("link", "driver");
        model.addAttribute("object", xmlDataService.getXml(name));
        return "editor";
    }
}
