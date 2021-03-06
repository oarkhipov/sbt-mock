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

package ru.sbt.bpm.mock.spring.service;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.bean.ResponseGenerator;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;
import ru.sbt.bpm.mock.spring.integration.gateway.ClientService;
import ru.sbt.bpm.mock.spring.service.message.validation.SOAPValidationService;
import ru.sbt.bpm.mock.utils.ExceptionUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * @author sbt-bochev-as on 20.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Service
public class MessageSendingService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private MockConfigContainer configContainer;

    @Autowired
    private ResponseGenerator responseGenerator;

    public synchronized String send(MockMessage message) throws IOException {
        message.setTransactionId(UUID.randomUUID());
        responseGenerator.log(message, MessageType.RQ);
        Protocol protocol = message.getProtocol();
        if (protocol == Protocol.JMS) {
            return sendJMS(message);
        }
        if (protocol == Protocol.SOAP) {
            return sendWs(message);
        }
        throw new IllegalStateException("No such protocol implementation [" + protocol + "]");
    }

    private String sendJMS(MockMessage message) {
        if (message.getTransactionId() == null) {
            message.setTransactionId(UUID.randomUUID());
        }
        System messageSystem = message.getSystem();
        MockMessage responseMessage = new MockMessage(Protocol.JMS, messageSystem.getQueueConnectionFactory(), messageSystem.getDriverIncomeQueue(), "");
        String responseString;
        try {
            responseString = clientService.sendMockMessage(message);

        } catch (RuntimeException e) {
            responseString = ExceptionUtils.getExceptionStackTrace(e);
            responseMessage.setFaultMessage(true);
        }
        responseMessage.setPayload(responseString);
        responseMessage.setTransactionId(message.getTransactionId());
        responseMessage.setSystem(messageSystem);
        responseMessage.setIntegrationPoint(message.getIntegrationPoint());
        responseGenerator.log(responseMessage, MessageType.RS);
        return responseString;
    }

    public String sendWs(MockMessage message) throws IOException {
        if (message.getTransactionId() == null) {
            message.setTransactionId(UUID.randomUUID());
        }
        HttpClient httpClient = HttpClientBuilder.create().build();

        System messageSystem = message.getSystem();
        WsdlProject wsdlProject = configContainer.getWsdlProjectMap().get(messageSystem.getSystemName());

        String operationName = SOAPValidationService.getWsdlOperation(wsdlProject, message.getIntegrationPoint()).getAction();
        HttpPost httpPost = new HttpPost(messageSystem.getDriverWebServiceEndpoint());
        httpPost.addHeader("Content-Type", "application/xml");
        httpPost.addHeader("SOAP-Action", operationName);
        HttpEntity entity = new ByteArrayEntity(message.getPayload().getBytes("UTF-8"));

        httpPost.setEntity(entity);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).build();
        httpPost.setConfig(requestConfig);
        HttpResponse response = httpClient.execute(httpPost);
        String responseString = EntityUtils.toString(response.getEntity());

        MockMessage responseMessage = new MockMessage(Protocol.JMS, messageSystem.getQueueConnectionFactory(), messageSystem.getDriverIncomeQueue(), responseString);
        responseMessage.setTransactionId(message.getTransactionId());
        responseMessage.setSystem(messageSystem);
        responseMessage.setIntegrationPoint(message.getIntegrationPoint());
        responseGenerator.log(responseMessage, MessageType.RS);

        return responseString;

    }

}
