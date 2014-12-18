package ru.sbt.bpm.mock.controller;

import com.google.gson.Gson;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.service.TransformService;
import ru.sbt.bpm.mock.service.XmlDataService;
import ru.sbt.bpm.mock.utils.AjaxObject;
import ru.sbt.bpm.mock.utils.SaveFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

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

    @Autowired
    ApplicationContext appContext;

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
        try {
            model.addAttribute("object", xmlDataService.getXml(name + "_Data"));
        }
        catch (Exception e) {
            model.addAttribute("object", e.getMessage());
        }
        return "editor";
    }

    @RequestMapping(value="/mock/{name}/validate/", method=RequestMethod.POST)
    public String validate(
            @PathVariable("name") String name,
            @RequestParam("xml") String xml,
            ModelMap model) {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            if (xmlDataService.validate(xml)) {
                ajaxObject.setInfo("Valid!");
            }
        } catch (SAXException e) {
            ajaxObject.setError(e.getMessage());
        }
        catch (IOException e) {
            ajaxObject.setError(e.getMessage());
        }
        Gson gson = new Gson();
        model.addAttribute("object", gson.toJson(ajaxObject));

        return "blank";
    }

    @RequestMapping(value="/mock/{name}/save/", method=RequestMethod.POST)
    public String save(
            @PathVariable("name") String name,
            @RequestParam("xml") String xml,
            ModelMap model) throws IOException {
        AjaxObject ajaxObject = new AjaxObject();
        SaveFile saver = SaveFile.getInstance(appContext);
        File dataFile = null;
        try {
            String path = saver.TranslateNameToPath(name);
            dataFile = saver.getBackUpedDataFile(path);
        } catch (Exception e) {
            ajaxObject.setError(e.getMessage());
        }
        if (dataFile!=null) {
            try {
                saver.writeStringToFile(dataFile, xml);
                ajaxObject.setInfo("saved");
            } catch (IOException e) {
                ajaxObject.setError(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                ajaxObject.setError(e.getMessage());
            }
        }
        Gson gson = new Gson();
        model.addAttribute("object", gson.toJson(ajaxObject));

        return "blank";
    }

    @RequestMapping(value="/mock/{name}/undo/", method=RequestMethod.POST)
    public String rollback(
            @PathVariable("name") String name,
            ModelMap model) throws IOException  {
        AjaxObject ajaxObject = new AjaxObject();
        SaveFile saver = SaveFile.getInstance(appContext);
        File dataFile;
        try {
            String path = saver.TranslateNameToPath(name);
            dataFile = saver.rollbackNextBackUpedDataFile(path);
            String datavalue = saver.getFileString(dataFile);
            ajaxObject.setData(datavalue);
            ajaxObject.setInfo("undo");
        }catch (IndexOutOfBoundsException e) {
            ajaxObject.setError(e.getMessage());
        }
        Gson gson = new Gson();
        model.addAttribute("object", gson.toJson(ajaxObject));

        return "blank";
    }

    @RequestMapping(value="/mock/{name}/redo/", method=RequestMethod.POST)
    public String rollforward(
            @PathVariable("name") String name,
            ModelMap model) throws IOException  {
        AjaxObject ajaxObject = new AjaxObject();
        SaveFile saver = SaveFile.getInstance(appContext);
        File dataFile;
        try {
            String path = saver.TranslateNameToPath(name);
            dataFile = saver.rollbackPervBackUpedDataFile(path);
            String datavalue = saver.getFileString(dataFile);
            ajaxObject.setData(datavalue);
            ajaxObject.setInfo("redo");
        }catch (IndexOutOfBoundsException e) {
            ajaxObject.setError(e.getMessage());
        }
        catch (IOException e) {
            model.addAttribute("error", e.getMessage());
        }
        Gson gson = new Gson();
        model.addAttribute("object", gson.toJson(ajaxObject));

        return "blank";
    }

    @RequestMapping(value="/mock/{name}/resetToDefault/", method=RequestMethod.POST)
    public String resetToDefault(
            @PathVariable("name") String name,
            ModelMap model) throws IOException  {
        SaveFile saver = SaveFile.getInstance(appContext);
        AjaxObject ajaxObject = new AjaxObject();
        File dataFile;
        try {
            String path = saver.TranslateNameToPath(name);
            dataFile = saver.restoreBackUpedDataFile(path);
            String datavalue = saver.getFileString(dataFile);
            ajaxObject.setData(datavalue);
            ajaxObject.setInfo("reset");
        } catch (IOException e) {
            ajaxObject.setError(e.getMessage());
        }
        Gson gson = new Gson();
        model.addAttribute("object", gson.toJson(ajaxObject));

        return "blank";
    }
}
