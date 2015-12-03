package ru.sbt.bpm.mock.spring.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sbt.bpm.mock.spring.logging.impl.LogEntry;
import ru.sbt.bpm.mock.spring.logging.impl.LogJsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sbt-vostrikov-mi on 13.10.2015.
 */

@Controller
public class LogController {


    @RequestMapping(value = "/log/", method = RequestMethod.GET)
    public String get(Model model) throws IOException, TransformerException {
        model.addAttribute("name", "Mock Driver Log");
        model.addAttribute("link", "driver");
        model.addAttribute("object", "<tag>Type request here 2...</tag>");
        model.addAttribute("title", "Log");
        return "logDataTable";
    }

    @RequestMapping(value = "/log/springPaginationDataTables.web", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String springPaginationDataTables(HttpServletRequest request) throws IOException {

        int size = 500;

        //Fetch the page number from client
        Integer pageNumber = 0;
        if (null != request.getParameter("iDisplayStart")) {
            pageNumber = Integer.valueOf(request.getParameter("iDisplayStart"));
        }

        //Fetch search parameter
        String searchParameter = request.getParameter("sSearch");

        //Fetch Page display length
        Integer pageDisplayLength = Integer.valueOf(request.getParameter("iDisplayLength"));

        //Create page list data
        List<LogEntry> logList = createPaginationData(size);

        //Search functionality: Returns filtered list based on search parameter
        logList = getListBasedOnSearchParameter(searchParameter,logList);

        List<LogEntry> pagedLogList = logList.subList(pageNumber, pageNumber + pageDisplayLength);

        LogJsonObject logJsonObject = new LogJsonObject();
        //Set Total display record
        logJsonObject.setITotalDisplayRecords(pagedLogList.size());
        //Set Total record
        logJsonObject.setITotalRecords(logList.size());
        logJsonObject.setAaData(pagedLogList);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json2 = gson.toJson(logJsonObject);

        return json2;
    }

    private List<LogEntry> getListBasedOnSearchParameter(String searchParameter,List<LogEntry> logList) {

        if (null != searchParameter && !searchParameter.equals("")) {
            List<LogEntry> logListForSearch = new ArrayList<LogEntry>();
            searchParameter = searchParameter.toUpperCase();
            for (LogEntry logEntry : logList) {
                if (logEntry.toString().toUpperCase().contains(searchParameter)) {
                    logListForSearch.add(logEntry);
                }

            }
            logList = logListForSearch;
        }
        return logList;
    }

    private List<LogEntry> createPaginationData(Integer pageDisplayLength) {
        List<LogEntry> personsList = new ArrayList<LogEntry>();
        Date date = new Date();
        for (int i = 0; i < pageDisplayLength; i++) {
            LogEntry log = new LogEntry(date.toString(), "queue", "msgtype", "log msg ["+i+"]");
            personsList.add(log);
        }
        return personsList;
    }
}
