package ru.sbt.bpm.mock.controller;

import com.google.gson.Gson;
import org.apache.commons.lang.StringEscapeUtils;
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
import ru.sbt.bpm.mock.utils.AjaxObject;
import ru.sbt.bpm.mock.utils.SaveFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
//        model.addAttribute("list", new ArrayList<String>(){"123","123"});
        return "editor";
    }

    @RequestMapping(value="/driver/{name}/validate/", method=RequestMethod.POST)
    public String validate(
            @PathVariable("name") String name,
            @RequestParam("xml") String xml,
            ModelMap model) {
        try {
            if (xmlDataService.validate(xml)) {
                model.addAttribute("info", "Valid!");
            }
        } catch (SAXException |IOException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "ajaxResponseObject";
    }

    @RequestMapping(value="/driver/{name}/save/", method=RequestMethod.POST)
    public String save(
            @PathVariable("name") String name,
            @RequestParam("xml") String xml,
            ModelMap model) throws IOException {
        SaveFile saver = SaveFile.getInstance(appContext);
        File dataFile = null;
        try {
            String path = saver.TranslateNameToPath(name);
            dataFile = saver.getBackUpedDataFile(path);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        if (dataFile!=null) {
            try {
                saver.writeStringToFile(dataFile, xml);
                model.addAttribute("info", "saved");
            } catch (IOException e) {
                model.addAttribute("error", e.getMessage());
                model.addAttribute("info", "fail");
            }
        }
        return "ajaxResponseObject";
    }

    @RequestMapping(value="/driver/{name}/undo/", method=RequestMethod.POST)
    public String rollback(
            @PathVariable("name") String name,
            ModelMap model) throws IOException  {
        SaveFile saver = SaveFile.getInstance(appContext);
        File dataFile = null;
        try {
            String path = saver.TranslateNameToPath(name);
            dataFile = saver.rollbackNextBackUpedDataFile(path);
            String datavalue = saver.getFileString(dataFile);
            datavalue = datavalue.replace("\r", "\\r").replace("\n", "\\n");
            model.addAttribute("data", datavalue);
            model.addAttribute("info", "undo");
        }catch (IndexOutOfBoundsException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("info", "fail");
        }
        catch (IOException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("info", "fail");
        }
        return "ajaxResponseObject";
    }

    @RequestMapping(value="/driver/{name}/redo/", method=RequestMethod.POST)
    public String rollforward(
            @PathVariable("name") String name,
            ModelMap model) throws IOException  {
        SaveFile saver = SaveFile.getInstance(appContext);
        File dataFile = null;
        try {
            String path = saver.TranslateNameToPath(name);
            dataFile = saver.rollbackPervBackUpedDataFile(path);
            String datavalue = saver.getFileString(dataFile);
            datavalue = datavalue.replace("\r", "\\r").replace("\n", "\\n");
            model.addAttribute("data", datavalue);
            model.addAttribute("info", "redo");
        }catch (IndexOutOfBoundsException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("info", "fail");
        }
        catch (IOException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("info", "fail");
        }
        return "ajaxResponseObject";
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
            String datavalue = saver.getFileString(dataFile);
            datavalue = datavalue.replace("\r", "\\r").replace("\n","\\n");
            model.addAttribute("data", datavalue);
            model.addAttribute("info", "reset");
        } catch (IOException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("info", "fail");
        }
        return "ajaxResponseObject";
    }

    @RequestMapping(value="/driver/{name}/send/", method=RequestMethod.POST)
    public String send(
            @PathVariable("name") String name,
            @RequestParam("xml") String xml,
            ModelMap model) {
//        VALIDATE
//        try {
//            if (xmlDataService.validate(xml)) {
//                model.addAttribute("info", "DONE!");
//                model.addAttribute("data", clientService.invoke(xml));
//            }
//        } catch (SAXException |IOException e) {
//            model.addAttribute("error", e.getMessage());
//        }
        AjaxObject ajaxObject = new AjaxObject();
        ajaxObject.setInfo("DONE!");
        ajaxObject.setData(clientService.invoke(xml));
        Gson gson = new Gson();
        String json = customEscape(gson.toJson(ajaxObject));
        System.out.println(json);
        model.addAttribute("object", json);

        return "blank";
    }

    private String customEscape(String in) {
        return in.replace("\r", "\\r").replace("\n","\\n");
    }
}
