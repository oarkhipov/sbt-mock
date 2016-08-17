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

package ru.sbt.bpm.mock.spring.controllers.api;

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
