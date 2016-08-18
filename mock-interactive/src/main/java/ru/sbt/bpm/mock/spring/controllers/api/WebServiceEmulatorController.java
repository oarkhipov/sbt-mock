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

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockDispatcher;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockService;
import com.eviware.soapui.impl.wsdl.support.soap.SoapMessageBuilder;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import ru.sbt.bpm.mock.config.entities.System;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.bean.ResponseGenerator;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;
import ru.sbt.bpm.mock.spring.service.message.validation.mockObjects.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;

/**
 * @author sbt-bochev-as on 14.03.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class WebServiceEmulatorController {

    @Autowired
    ResponseGenerator responseGenerator;

    @Autowired
    MockConfigContainer configContainer;

    @RequestMapping(value = "/ws/{webServiceName}", method = RequestMethod.GET, produces = MediaType.TEXT_XML_VALUE + ";charset=utf-8")
    public
    @ResponseBody
    synchronized String getWsdl(@PathVariable String webServiceName, HttpServletRequest request) throws IOException {
        if (request.getQueryString().equalsIgnoreCase("wsdl")) {
            System systemByName = configContainer.getSystemByName(webServiceName);
            if (systemByName.getProtocol() == Protocol.SOAP) {
                URL url = new URL(request.getRequestURL().toString());
                WsdlProject wsdlProject = configContainer.getWsdlProjectMap().get(webServiceName);
                WsdlMockService mockService = wsdlProject.getMockServiceAt(0);
                mockService.setHost(url.getHost());
                mockService.setPort(url.getPort());
                mockService.setPath(url.getPath());
                WsdlMockDispatcher wsdlMockDispatcher = new WsdlMockDispatcher(mockService, null);
                MockHttpServletResponse response = new MockHttpServletResponse();
                wsdlMockDispatcher.printWsdl(response);
                return response.getContentAsString();
            }
        }
        return SoapMessageBuilder.buildFault("Server", "No such method", SoapVersion.Soap11);
    }

    @RequestMapping(value = "/ws/{webServiceName}", method = RequestMethod.POST, produces = MediaType.TEXT_XML_VALUE + ";charset=utf-8")
    public
    @ResponseBody
    String emulateWebService(@PathVariable String webServiceName, @RequestBody String payload) {
        final MockMessage mockMessage = responseGenerator.proceedWsRequest(new MockMessage(payload), webServiceName);
        if (mockMessage.isSendMessage()) {
            return mockMessage.getPayload();
        } else {
            return null;
        }
    }
}
