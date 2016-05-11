package ru.sbt.bpm.mock.spring.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.service.XmlGeneratorService;
import ru.sbt.bpm.mock.spring.utils.AjaxObject;

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
    @RequestMapping(value = "/xml/{system}/{integrationPointName}/rq/{filtered}")
    public String generateRq(@PathVariable String system, @PathVariable String integrationPointName,  @PathVariable boolean filtered) {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(system, integrationPointName, MessageType.RQ, filtered));
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }
        return ajaxObject.toJSON();
    }

    @ResponseBody
    @RequestMapping(value = "/xml/{system}/{integrationPointName}/rs/{filtered}")
    public String generateRs(@PathVariable String system, @PathVariable String integrationPointName,  @PathVariable boolean filtered) {
        AjaxObject ajaxObject = new AjaxObject();
        try {
            ajaxObject.setData(generatorService.generate(system, integrationPointName, MessageType.RS, filtered));
        } catch (Exception e) {
            ajaxObject.setErrorFromException(e);
        }
        return ajaxObject.toJSON();
    }
}