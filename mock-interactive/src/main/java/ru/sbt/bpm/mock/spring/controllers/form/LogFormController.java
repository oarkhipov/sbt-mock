package ru.sbt.bpm.mock.spring.controllers.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.tuple.Tuple2;
import ru.sbt.bpm.mock.logging.entities.LogsApiEntity;
import ru.sbt.bpm.mock.logging.entities.LogsEntity;
import ru.sbt.bpm.mock.logging.spring.services.LogService;
import ru.sbt.bpm.mock.spring.logging.LogApiEntityBuilder;
import ru.sbt.bpm.mock.spring.logging.LogControllerResponseBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

/**
 * Created by sbt-bochev-as
 * on 26.04.2016.
 */

@Controller
public class LogFormController {

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
        Tuple2<List<LogsEntity>, Long> logsTuple = logService.getLogs(apiEntity);
        long size = logService.getLogsDatabaseSize();
        String response = new LogControllerResponseBuilder()
                .withApiEntity(apiEntity)
                .withLogsQueryEntities(logsTuple.getT1())
                .withDataBaseSize(size)
                .withFilteredRecordsCount(logsTuple.getT2())
                .build();
        return response;
    }
}
