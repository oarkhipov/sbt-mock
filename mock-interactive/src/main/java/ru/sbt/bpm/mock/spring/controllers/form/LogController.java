package ru.sbt.bpm.mock.spring.controllers.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.logging.entities.LogsApiEntity;
import ru.sbt.bpm.mock.logging.entities.LogsEntity;
import ru.sbt.bpm.mock.logging.spring.services.LogService;
import ru.sbt.bpm.mock.spring.logging.LogApiEntityBuilder;
import ru.sbt.bpm.mock.spring.logging.LogControllerResponseBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by sbt-bochev-as
 * on 26.04.2016.
 */

@Controller
public class LogController {

    @Autowired
    LogService logService;

    @RequestMapping(value = "/log/", method = RequestMethod.GET)
    public String get(Model model) throws IOException, TransformerException {
        model.addAttribute("name", "Mock Driver Log");
        model.addAttribute("link", "driver");
        model.addAttribute("object", "<tag>Type request here ...</tag>");
        model.addAttribute("title", "Log");
        return "logDataTable";
    }

    @RequestMapping(value = "/log/data.web", produces = "application/json")
    public
    @ResponseBody
    String getLogsDataTableData(HttpServletRequest request) {
        LogsApiEntity apiEntity = new LogApiEntityBuilder().with(request.getParameterMap()).build();
        Iterable<LogsEntity> logsResult = logService.getLogs(apiEntity);
        long size = logService.getLogsDataBaseSize();
        String response = new LogControllerResponseBuilder().withApiEntity(apiEntity).withLogsQueryEntities(logsResult).withDataBaseSize(size).build();
        return response;
    }
}
