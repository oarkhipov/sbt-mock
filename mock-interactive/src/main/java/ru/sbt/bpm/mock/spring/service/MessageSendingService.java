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
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.bean.ResponseGenerator;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;
import ru.sbt.bpm.mock.spring.integration.gateway.ClientService;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;
import ru.sbt.bpm.mock.spring.utils.XpathUtils;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;

/**
 * @author sbt-bochev-as on 20.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Service
public class MessageSendingService {

    @Autowired
    ClientService clientService;

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    ResponseGenerator responseGenerator;

    public String send(MockMessage message) throws IOException {
        responseGenerator.log(message, MessageType.RQ);
        Protocol protocol = message.getProtocol();
        if (protocol == Protocol.JMS) {
            return sendJMS(message);
        }
        if (protocol == Protocol.SOAP) {
            return sendWs(message);
        }
        //TODO log answers
        throw new IllegalStateException("No such protocol implementation [" + protocol + "]");
    }

    public String sendJMS(MockMessage message) {
        return clientService.sendMockMessage(message);
    }

    private String sendWs(MockMessage message) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();

        ru.sbt.bpm.mock.config.entities.System system = message.getSystem();
        WsdlProject wsdlProject = configContainer.getWsdlProjectMap().get(system.getSystemName());
        String[] endpoints = wsdlProject.getInterfaceList().get(0).getEndpoints();

        HttpPost httpPost = new HttpPost(endpoints[0]);
        httpPost.addHeader("Content-Type", "application/xml");
        httpPost.addHeader("SOAP-Action", message.getIntegrationPoint().getName());
        HttpEntity entity = new ByteArrayEntity(message.getPayload().getBytes("UTF-8"));

        httpPost.setEntity(entity);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).build();
        httpPost.setConfig(requestConfig);
        HttpResponse response = httpClient.execute(httpPost);
        return EntityUtils.toString(response.getEntity());

    }
}
