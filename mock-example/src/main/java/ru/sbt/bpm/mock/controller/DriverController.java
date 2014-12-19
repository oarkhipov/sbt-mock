package ru.sbt.bpm.mock.controller;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
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
import ru.sbt.bpm.mock.utils.XslTransformer;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        File drivers = appContext.getResource("/WEB-INF/driverList.txt").getFile();
        String string = IOUtils.toString(new FileInputStream(drivers));
        String[] stringList = string.split("\\r?\\n");
        model.addAttribute("link", "driver");
        model.addAttribute("list", stringList);
        return "stepForm";
    }

    @RequestMapping(value="/driver/{name}/", method= RequestMethod.GET)
    public String get(@PathVariable("name") String name, Model model) throws IOException, TransformerException {
        model.addAttribute("name", name);
        model.addAttribute("link", "driver");
        model.addAttribute("object", xmlDataService.getXml(name));
        model.addAttribute("list", getRequestList(name));
        return "editor";
    }

    @RequestMapping(value="/driver/{name}/validate/", method=RequestMethod.POST)
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

    @RequestMapping(value="/driver/{name}/save/", method=RequestMethod.POST)
    public String save(
            @PathVariable("name") String name,
            @RequestParam("xml") String xml,
            ModelMap model) throws IOException {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            if (xmlDataService.validate(xml)) {
//                IF Valid - then save
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
            }
//            END Save
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

    @RequestMapping(value="/driver/{name}/undo/", method=RequestMethod.POST)
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

    @RequestMapping(value="/driver/{name}/redo/", method=RequestMethod.POST)
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
            ajaxObject.setError(e.getMessage());
        }
        Gson gson = new Gson();
        model.addAttribute("object", gson.toJson(ajaxObject));

        return "blank";
    }

    @RequestMapping(value="/driver/{name}/resetToDefault/", method=RequestMethod.POST)
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

    @RequestMapping(value="/driver/{name}/send/", method=RequestMethod.POST)
    public String send(
            @PathVariable("name") String name,
            @RequestParam("request") String request,
            @RequestParam("xml") String xml,
            ModelMap model) throws IOException, TransformerException {
//        VALIDATE
        AjaxObject ajaxObject = new AjaxObject();
        try {
            if (xmlDataService.validate(xml)) {

                ajaxObject.setInfo("DONE!");
                Resource xslResource = xmlDataService.getXslResource(name);
//                Resource xmlData = xmlDataService.getXmlResource(name);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSSXXXXX");
                Map<String, String> params = new HashMap<String, String>(2);
                params.put("name", request);
                Date date = new Date();
                params.put("timestamp", dateFormat.format(date));
                String result = XslTransformer.transform(xslResource, xml, params);

                ajaxObject.setData(clientService.invoke(result));
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

    @RequestMapping(value="/driver/{name}/list/", method=RequestMethod.POST)
    public String getRequestList(
            @PathVariable("name") String name,
            @RequestParam("xml") String xml,
            ModelMap model) throws IOException, TransformerException {
        AjaxObject ajaxObject = new AjaxObject();
        ajaxObject.setInfo("Refreshed!");
        ajaxObject.setData(StringUtils.join(getRequestList(name, xml), ","));
        Gson gson = new Gson();
        model.addAttribute("object", gson.toJson(ajaxObject));

        return "blank";
    }

    private String[] getRequestList(String name) throws IOException, TransformerException {
        Resource xmlData = xmlDataService.getXmlResource(name);
        return getRequestList(xmlData);
    }

    private String[] getRequestList(String name, String xml) throws IOException, TransformerException {
        Resource xslResource = appContext.getResource("/WEB-INF/xsl/DataRowToDataList.xsl");
        String result = XslTransformer.transform(xslResource, xml);
        return result.split("\\r?\\n");
    }

    private String[] getRequestList(Resource xml) throws IOException, TransformerException {
        Resource xslResource = appContext.getResource("/WEB-INF/xsl/DataRowToDataList.xsl");
        String result = XslTransformer.transform(xslResource, xml);
        return result.split("\\r?\\n");
    }


}
