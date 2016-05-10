package ru.sbt.bpm.mock.spring.controllers.api;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.sbt.bpm.mock.logging.spring.services.LogService;

/**
 * @author sbt-bochev-as on 05.05.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class ApiLogController {

    @Autowired
    LogService logService;

    @RequestMapping(value = "/api/log/getTop/{rowNumbers}/", method = RequestMethod.GET)
    @ResponseBody
    public String getTopLogs(@PathVariable int rowNumbers,
                             @RequestParam(required = false, value = "integrationPoint") String integrationPointName,
                             @RequestParam(required = false, value = "system") String systemName) {
        return logService.getTopLogs(rowNumbers, systemName, integrationPointName);
    }

    @RequestMapping(value = "/api/log/getMessage/{stringTimestamp}")
    @ResponseBody
    public String getMessage(@PathVariable String stringTimestamp) {
        return StringEscapeUtils.escapeHtml4(logService.getMessageByTs(stringTimestamp));
    }
}
