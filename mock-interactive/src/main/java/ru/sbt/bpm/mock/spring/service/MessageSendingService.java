package ru.sbt.bpm.mock.spring.service;

import com.eviware.soapui.impl.wsdl.WsdlOperation;
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
import ru.sbt.bpm.mock.spring.utils.ExceptionUtils;

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
    ClientService clientService;

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    ResponseGenerator responseGenerator;

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

    public String sendJMS(MockMessage message) {
        message.setTransactionId(UUID.randomUUID());
        ru.sbt.bpm.mock.config.entities.System messageSystem = message.getSystem();
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

    protected String sendWs(MockMessage message) throws IOException {
        message.setTransactionId(UUID.randomUUID());
        HttpClient httpClient = HttpClientBuilder.create().build();

        ru.sbt.bpm.mock.config.entities.System messageSystem = message.getSystem();
        WsdlProject wsdlProject = configContainer.getWsdlProjectMap().get(messageSystem.getSystemName());


        String operationName = ((WsdlOperation) wsdlProject.getInterfaceList().get(0).getOperationByName(message.getIntegrationPoint().getName())).getAction();
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
