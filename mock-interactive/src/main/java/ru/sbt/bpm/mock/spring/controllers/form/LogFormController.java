/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ru.sbt.bpm.mock.spring.controllers.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.tuple.Tuple2;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.logging.entities.LogsApiEntity;
import ru.sbt.bpm.mock.logging.entities.LogsEntity;
import ru.sbt.bpm.mock.logging.spring.services.LogService;
import ru.sbt.bpm.mock.logging.LogApiEntityBuilder;
import ru.sbt.bpm.mock.logging.LogControllerResponseBuilder;

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

    @Autowired
    MockConfigContainer mockConfigContainer;

    @RequestMapping(value = "/log/", method = RequestMethod.GET)
    public String get(Model model) throws IOException, TransformerException {
        model.addAttribute("config",mockConfigContainer.getConfig());
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
