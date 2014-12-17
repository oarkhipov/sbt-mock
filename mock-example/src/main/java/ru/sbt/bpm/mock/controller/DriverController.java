package ru.sbt.bpm.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.gateway.ClientService;
import ru.sbt.bpm.mock.service.XmlDataService;
import ru.sbt.bpm.mock.utils.SaveFile;

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

    @Autowired
    ClientService clientService;

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

    @RequestMapping(value="/driver/{name}/validate/", method=RequestMethod.POST)
    public String validate(
            @PathVariable("name") String name,
            @RequestParam("xml") String xml,
            ModelMap model) {
        try {
            if (xmlDataService.validate(xml)) {
                model.addAttribute("object", "true");
            }
        } catch (SAXException |IOException e) {
            model.addAttribute("object", e.getMessage());
        }
        return "blank";
    }

    @RequestMapping(value="/driver/{name}/save/", method=RequestMethod.POST)
    public String save(
            @PathVariable("name") String name,
            @RequestParam("xml") String xml,
            ModelMap model) throws IOException  {
        SaveFile saver = SaveFile.getInstance(appContext);
        File dataFile = null;
        try {
            String path = saver.TranslateNameToPath(name);
            dataFile = saver.getBackUpedDataFile(path);
        } catch (Exception e) {
            model.addAttribute("object", e.getMessage());
        }
        if (dataFile!=null) {
            try {
                saver.writeStringToFile(dataFile, xml);
                model.addAttribute("object", "saved");
            } catch (IOException e) {
                model.addAttribute("object", e.getMessage());
            }
        }
        return "blank";
    }

    @RequestMapping(value="/driver/{name}/rollback/", method=RequestMethod.POST)
    public String rollback(
            @PathVariable("name") String name,
            ModelMap model) throws IOException  {
        SaveFile saver = SaveFile.getInstance(appContext);
        File dataFile = null;
        try {
            String path = saver.TranslateNameToPath(name);
            dataFile = saver.getNextBackUpedDataFile(path);
            model.put("object", saver.getFileString(dataFile));
            model.addAttribute("object", "rollbacked. ChangesAreUnsaved");
        } catch (IOException e) {
            model.addAttribute("object", e.getMessage());
        }
        return "blank";
    }

    @RequestMapping(value="/driver/{name}/resetToDefault/", method=RequestMethod.POST)
    public String resetToDefault(
            @PathVariable("name") String name,
            ModelMap model) throws IOException  {
        SaveFile saver = SaveFile.getInstance(appContext);
        File dataFile = null;
        try {
            String path = saver.TranslateNameToPath(name);
            dataFile = saver.restoreBackUpedDataFile(path);
            model.addAttribute("object", saver.getFileString(dataFile));
        } catch (IOException e) {
            model.addAttribute("object", e.getMessage());
        }
        return "blank";
    }
    @RequestMapping(value="/driver/{name}/send/", method=RequestMethod.POST)
    public String send(
            @PathVariable("name") String name,
            @RequestParam("xml") String xml,
            ModelMap model) {
        model.addAttribute("object", clientService.invoke(xml));
        return "blank";
    }
}
