package ru.sbt.bpm.mock.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.spring.service.XmlGeneratorService;
import ru.sbt.bpm.mock.spring.utils.AjaxObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

/**
 * @author sbt-bochev-as on 23.11.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class XmlGeneratorController {

    @Autowired
    XmlGeneratorService generatorService;

    @ResponseBody
    @RequestMapping(value = "/xml/{system}/{integrationPointName}")
    public String generate(@PathVariable String system, @PathVariable String integrationPointName) {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(system, integrationPointName));
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }
        return ajaxObject.toJSON();
    }
}