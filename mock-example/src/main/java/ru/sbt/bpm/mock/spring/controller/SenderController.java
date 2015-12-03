package ru.sbt.bpm.mock.spring.controller;

import com.google.gson.Gson;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sbt.bpm.mock.spring.bean.TemplateEngineBean;
import ru.sbt.bpm.mock.spring.integration.gateway.ClientService;
import ru.sbt.bpm.mock.spring.utils.AjaxObject;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by sbt-bochev-as on 22.05.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@Log
@Controller
public class SenderController {

    @Autowired
    ClientService clientService;

    @RequestMapping(value = "/sender/", method = RequestMethod.GET)
    public String get(Model model) throws IOException, TransformerException {
        model.addAttribute("name", "Any Message Sender");
        model.addAttribute("link", "driver");
        model.addAttribute("object", "<tag>Type request here...</tag>");
        model.addAttribute("title", "Sender");
        return "wrappedEditor";
    }

    @RequestMapping(value = "/sender/send/", method = RequestMethod.POST)
    public String send(
            @RequestParam("xml") String xml,
            ModelMap model) throws IOException, TransformerException {
        AjaxObject ajaxObject = new AjaxObject();
//        String message = templateEngineBean.applyTemplate(xml);
//        log.info("Sending:\n" +
//                "============================================\n" +
//                message + "\n" +
//                "============================================");
//        ajaxObject.setInfo("DONE!");
//        try {
//            ajaxObject.setData(clientService.send(message));
//        } catch (Exception e) {
//            ajaxObject.setError(e.getMessage());
//        }

        String init = "class RespObj {\n" +
                "def properties = [:]\n" +
                "def getProperty(String name) { properties[name] }\n" +
                "void setProperty(String name, value) { properties[name] = value }\n" +
                "}\n" +
                "def response = new RespObj()\n" +
                "\n";

        GroovyShell shell = new GroovyShell();
        ajaxObject.setData((String) shell.evaluate(init + xml));
        Gson gson = new Gson();
        model.addAttribute("object", gson.toJson(ajaxObject));
        return "blank";
    }
}
