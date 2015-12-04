package ru.sbt.bpm.mock.spring.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @ResponseBody
    @RequestMapping(value = "/sender/send/", method = RequestMethod.POST)
    public String send(
            @RequestParam String xml) {
        AjaxObject ajaxObject = new AjaxObject();
//        VALIDATE
        try {
                String response = clientService.send(xml);
                ajaxObject.setData(response);
                ajaxObject.setInfo("DONE!");
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }

        return ajaxObject.toJSON();
    }
}
