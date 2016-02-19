package ru.sbt.bpm.mock.spring.controller;

import net.sf.saxon.s9api.SaxonApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.integration.gateway.ClientService;
import ru.sbt.bpm.mock.spring.integration.gateway.TestGatewayService;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.DataService;
import ru.sbt.bpm.mock.spring.service.GroovyService;
import ru.sbt.bpm.mock.spring.utils.AjaxObject;
import ru.sbt.bpm.mock.spring.utils.SaveFile;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;

/**
 * Created by sbt-bochev-as on 15.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
@Controller
public class DriverController {

    @Autowired
    ApplicationContext appContext;

    @Autowired
    ClientService clientService;

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    DataService dataService;

    @Autowired
    DataFileService dataFileService;

    @Autowired
    GroovyService groovyService;

    @Autowired
    XmlGeneratorController generatorController;

    @Autowired
    TestGatewayService testGatewayService;


    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/", method = RequestMethod.GET)
    public String get(@PathVariable String systemName,
                      @PathVariable String integrationPointName,
                      Model model) throws IOException, TransformerException {
        model.addAttribute("systemName", systemName);
        model.addAttribute("name", integrationPointName);
        model.addAttribute("link", "driver");
        //TODO send "xpath with namespace" via js tooltip
        model.addAttribute("xpath",
                configContainer.getConfig().getSystems().getSystemByName(systemName)
                        .getIntegrationPoints().getIntegrationPointByName(integrationPointName)
                        .getXpathString());
        model.addAttribute("message", dataFileService.getCurrentMessage(systemName, integrationPointName));
        model.addAttribute("script", dataFileService.getCurrentScript(systemName, integrationPointName));
        model.addAttribute("test", dataFileService.getCurrentTest(systemName, integrationPointName));
        return "editor";
    }

    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/validate/", method = RequestMethod.POST)
    public String validate(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName,
            @RequestParam String xml,
            @RequestParam String script,
            @RequestParam String test,
            ModelMap model) {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            String compiledXml = groovyService.execute(test, xml, script);
            if (dataService.assertXpath(compiledXml, systemName, integrationPointName)) {
                dataService.validate(compiledXml, systemName);
                ajaxObject.setInfo("Valid!");
            } else {
                ajaxObject.setError("xml did not pass xpath assertion!");
            }
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }
        model.addAttribute("object", ajaxObject.toJSON());
        return "blank";
    }

    @ResponseBody
    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/save/", method = RequestMethod.POST)
    public String save(
            @PathVariable String systemName,
            @PathVariable String integrationPointName,
            @RequestParam String xml,
            @RequestParam String script,
            @RequestParam String test) throws IOException {
        AjaxObject ajaxObject = new AjaxObject();
        SaveFile saver = SaveFile.getInstance(appContext);
        try {
            String compiledXml = groovyService.execute(test, xml, script);
            if (dataService.assertXpath(compiledXml, systemName, integrationPointName)) {
                dataService.validate(compiledXml, systemName);
//                IF Valid - then save
                File messageFile = null;
                File scriptFile = null;
                File testFile = null;
                try {
                    //message file
                    messageFile = saver.getBackUpedDataFile(saver.TranslateNameToPath(systemName + "_" + integrationPointName + "_" + "message.xml"));
                    //script file
                    scriptFile = saver.getBackUpedDataFile(saver.TranslateNameToPath(systemName + "_" + integrationPointName + "_" + "script.groovy"));
                    //test file
                    testFile = saver.getBackUpedDataFile(saver.TranslateNameToPath(systemName + "_" + integrationPointName + "_" + "test.xml"));
                } catch (Exception e) {
                    ajaxObject.setError(e.getMessage());
                }
                if (messageFile != null && scriptFile != null && testFile != null) {
                    try {
                        saver.writeStringToFile(messageFile, xml);
                        saver.writeStringToFile(scriptFile, script);
                        saver.writeStringToFile(testFile, test);
                        ajaxObject.setInfo("saved");
                    } catch (IOException e) {
                        ajaxObject.setErrorFromException(e);
                    } catch (Exception e) {
                        ajaxObject.setErrorFromException(e);
                    }
                }
            }
//            END Save
        } catch (Exception e) {
            ajaxObject.setError(e.getMessage());
        }
        return ajaxObject.toJSON();
    }

    //TODO
//    @RequestMapping(value="/driver/{name}/undo/", method=RequestMethod.POST)
//    public String rollback(
//            @PathVariable("name") String name,
//            ModelMap model) throws IOException  {
//        AjaxObject ajaxObject = new AjaxObject();
//        SaveFile saver = SaveFile.getInstance(appContext);
//        File dataFile;
//        try {
//            String path = saver.TranslateNameToPath(name);
//            dataFile = saver.rollbackNextBackupDataFile(path);
//            String datavalue = saver.getFileString(dataFile);
//            ajaxObject.setData(datavalue);
//            ajaxObject.setInfo("undo");
//        }catch (IndexOutOfBoundsException e) {
//            ajaxObject.setError(e.getMessage());
//        }
//        model.addAttribute("object", ajaxObject.toJSON());
//
//        return "blank";
//    }
//
//    @RequestMapping(value="/driver/{name}/redo/", method=RequestMethod.POST)
//    public String rollforward(
//            @PathVariable("name") String name,
//            ModelMap model) throws IOException  {
//        AjaxObject ajaxObject = new AjaxObject();
//        SaveFile saver = SaveFile.getInstance(appContext);
//        File dataFile;
//        try {
//            String path = saver.TranslateNameToPath(name);
//            dataFile = saver.rollbackPervBackUpedDataFile(path);
//            String datavalue = saver.getFileString(dataFile);
//            ajaxObject.setData(datavalue);
//            ajaxObject.setInfo("redo");
//        }catch (IndexOutOfBoundsException e) {
//            ajaxObject.setError(e.getMessage());
//        }
//        catch (IOException e) {
//            ajaxObject.setError(e.getMessage());
//        }
//        model.addAttribute("object", ajaxObject.toJSON());
//
//        return "blank";
//    }

    @ResponseBody
    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/resetToDefault/filtered", method = RequestMethod.POST)
    public String resetToDefaultFiltered(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName) throws IOException {
        return generatorController.generate(systemName, integrationPointName, true);
    }

    @ResponseBody
    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/resetToDefault/", method = RequestMethod.POST)
    public String resetToDefaultFull(
            @PathVariable("systemName") String systemName,
            @PathVariable("integrationPointName") String integrationPointName) throws IOException {
        return generatorController.generate(systemName, integrationPointName, false);
    }


    @ResponseBody
    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/send/", method = RequestMethod.POST)
    public String send(
            @PathVariable String systemName,
            @PathVariable String integrationPointName,
            @RequestParam(required = false) String xml,
            @RequestParam(required = false) String script,
            @RequestParam(required = false) String test) {

        AjaxObject ajaxObject = new AjaxObject();
//        VALIDATE
        try {
            String compiledXml = groovyService.execute(test, xml, script);
            if (dataService.assertXpath(compiledXml, systemName, integrationPointName)) {
                dataService.validate(compiledXml, systemName);

                String response = clientService.send(compiledXml);
                ajaxObject.setData(response);
                ajaxObject.setInfo("DONE!");
            }
        } catch (SaxonApiException e) {
            ajaxObject.setErrorFromException(e);
        } catch (XPathExpressionException e) {
            ajaxObject.setErrorFromException(e);
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }

        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/driver/{systemName}/{integrationPointName}/test/", method = RequestMethod.POST)
    public String test(
            @PathVariable String systemName,
            @PathVariable String integrationPointName,
            @RequestParam String xml,
            @RequestParam String script,
            @RequestParam String test) {

        AjaxObject ajaxObject = new AjaxObject();
        try {
            String compiledXml = groovyService.execute(test, xml, script);
            if (dataService.assertXpath(compiledXml, systemName, integrationPointName)) {
                dataService.validate(compiledXml, systemName);

                String response = testGatewayService.test(compiledXml);
                ajaxObject.setData(response);
                ajaxObject.setInfo("DONE!");
            }
        } catch (SaxonApiException e) {
            ajaxObject.setErrorFromException(e);
        } catch (XPathExpressionException e) {
            ajaxObject.setErrorFromException(e);
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }

        return ajaxObject.toJSON();
    }
}
