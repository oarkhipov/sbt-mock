package ru.sbt.bpm.mock.spring.bean;

import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.entities.XpathSelector;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.logging.entities.LogsEntity;
import ru.sbt.bpm.mock.logging.spring.services.LogService;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.GroovyService;
import ru.sbt.bpm.mock.spring.service.message.JmsService;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;
import ru.sbt.bpm.mock.spring.service.message.validation.SOAPValidationService;
import ru.sbt.bpm.mock.spring.service.message.validation.ValidationUtils;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.MessageValidationException;
import ru.sbt.bpm.mock.spring.utils.ExceptionUtils;
import ru.sbt.bpm.mock.spring.utils.XmlUtils;

import javax.xml.xpath.XPathExpressionException;
import java.util.List;
import java.util.UUID;

/**
 * @author sbt-bochev-as on 23.11.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Slf4j
@Component
public class ResponseGenerator {

    private static LogService logService;
    private static GroovyService groovyService;
    private static MessageValidationService messageValidationService;
    private static DataFileService dataFileService;
    private static MockConfigContainer configContainer;
    private static JmsService jmsService;
    private static SOAPValidationService soapValidationService;

    @Autowired
    public ResponseGenerator(LogService logService, GroovyService groovyService, MessageValidationService messageValidationService, DataFileService dataFileService, MockConfigContainer configContainer, JmsService jmsService, SOAPValidationService soapValidationService) {
        ResponseGenerator.logService = logService;
        ResponseGenerator.groovyService = groovyService;
        ResponseGenerator.messageValidationService = messageValidationService;
        ResponseGenerator.dataFileService = dataFileService;
        ResponseGenerator.configContainer = configContainer;
        ResponseGenerator.jmsService = jmsService;
        ResponseGenerator.soapValidationService = soapValidationService;
    }

    public MockMessage proceedJmsRequest(MockMessage mockMessage) throws Exception {
        mockMessage.setTransactionId(UUID.randomUUID());
        mockMessage.setProtocol(Protocol.JMS);
        MockMessage responseMockMessage;
        try {
            final System systemByPayload = jmsService.getSystemByPayload(mockMessage.getPayload(), mockMessage.getQueue());
            mockMessage.setSystem(systemByPayload);
            responseMockMessage = proceedAbstractMessageRequest(mockMessage);
        } catch (Exception e) {
            log(mockMessage, MessageType.RQ);
            throw e;
        }

        System system = responseMockMessage.getSystem();
        IntegrationPoint integrationPoint = responseMockMessage.getIntegrationPoint();
        String outcomeQueue = system.getMockOutcomeQueue();

        String integrationPointOutcomeQueue = integrationPoint.getOutcomeQueue();
        if (integrationPointOutcomeQueue != null && !integrationPointOutcomeQueue.isEmpty()) {
            outcomeQueue = integrationPointOutcomeQueue;
        }
        responseMockMessage.setQueue(outcomeQueue);
        return responseMockMessage;
    }

    public MockMessage proceedWsRequest(MockMessage mockMessage, String systemName) {
        mockMessage.setTransactionId(UUID.randomUUID());
        mockMessage.setProtocol(Protocol.SOAP);
        final System systemByName = configContainer.getSystemByName(systemName);
        mockMessage.setSystem(systemByName);
        return proceedAbstractMessageRequest(mockMessage);
    }

    private MockMessage proceedAbstractMessageRequest(MockMessage mockMessage) {
        try {
            findIntegrationPoint(mockMessage);
            log(mockMessage, MessageType.RQ);
            validate(mockMessage, MessageType.RQ);
            if (mockMessage.isFaultMessage()) log(mockMessage, MessageType.RQ);
            Boolean answerRequired = mockMessage.getIntegrationPoint().getAnswerRequired();
            if (answerRequired == null || answerRequired) {
                generateResponse(mockMessage);
                delay(mockMessage);
                validate(mockMessage, MessageType.RS);
                log(mockMessage, MessageType.RS);
            } else {
                mockMessage.setSendMessage(false);
                log(mockMessage, MessageType.RS);
                mockMessage.setPayload(null);
            }
        } catch (Exception e) {
            //TODO fix builder error generator
//            String fault = SoapMessageBuilder.buildFault("Server", e.getMessage(), SoapVersion.Soap11);
            mockMessage.setPayload(wrapMessageWithSoapFault("Server", ExceptionUtils.getExceptionStackTrace(e)));
        }
        return mockMessage;
    }


    /**
     * Returns integration point by system and payload from mock message
     *
     * @param mockMessage incoming message
     * @throws XPathExpressionException
     * @throws SaxonApiException
     */
    protected void findIntegrationPoint(MockMessage mockMessage) throws SaxonApiException, XmlException {
        final System system = mockMessage.getSystem();
        Protocol protocol = system.getProtocol();
        if (protocol == Protocol.JMS) {
            findJMSIntegrationPoint(mockMessage);
            return;
        }
        if (protocol == Protocol.SOAP) {
            findSoapIntegrationPoint(mockMessage);
            return;
        }
        throw new IllegalStateException("No such protocol [" + protocol + "]");

    }

    private void findSoapIntegrationPoint(MockMessage mockMessage) throws XmlException {
        System system = mockMessage.getSystem();
        String compactXml = XmlUtils.compactXml(mockMessage.getPayload());
        String soapMessageElementName = soapValidationService.getOperationByElementName(system.getSystemName(), soapValidationService.getSoapMessageElementName(compactXml), MessageType.RQ);
        IntegrationPoint integrationPointByName = system.getIntegrationPoints().getIntegrationPointByName(soapMessageElementName);
        mockMessage.setIntegrationPoint(integrationPointByName);
    }

    private void findJMSIntegrationPoint(MockMessage mockMessage) throws SaxonApiException {
        final System system = mockMessage.getSystem();
        final String payload = mockMessage.getPayload();

        final XpathSelector integrationPointSelector = system.getIntegrationPointSelector();
        final int xpathSize = integrationPointSelector.getElementSelectors().size();
        final String lastElement = integrationPointSelector.getElementSelectors().get(xpathSize - 1).getElement();

        String integrationPointSelectorXpath = integrationPointSelector.toXpath();
        XdmValue value = XmlUtils.evaluateXpath(payload, integrationPointSelectorXpath);
        String integrationPointName;

        if (lastElement.isEmpty()) {
            //return element name
            integrationPointName = ((XdmNode) value).getNodeName().getLocalName();
        } else {
            //return element value
            integrationPointName = ((XdmNode) value).getStringValue();
        }
        assert integrationPointName != null;
        final IntegrationPoint integrationPointByName = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);

        mockMessage.setIntegrationPoint(integrationPointByName);
        log.debug(integrationPointByName.getName());
    }

    private void delay(MockMessage mockMessage) throws InterruptedException {
        //emulate System latency
        final Integer delayMs = mockMessage.getIntegrationPoint().getDelayMs();
        if (delayMs != null) {
            Thread.sleep(delayMs);
        }
    }

    /**
     * Generates response
     *
     * @param mockMessage incoming message
     * @throws Exception
     */
    private void generateResponse(MockMessage mockMessage) throws Exception {
        if (mockMessage.isFaultMessage()) return;
        final System system = mockMessage.getSystem();
        final IntegrationPoint integrationPoint = mockMessage.getIntegrationPoint();
        final String payload = mockMessage.getPayload();

        String message = dataFileService.getDefaultMessage(system.getSystemName(), integrationPoint.getName());
        String script = dataFileService.getDefaultScript(system.getSystemName(), integrationPoint.getName());
        mockMessage.setPayload(groovyService.execute(payload, message, script));
    }

    /**
     * Validate system message
     *
     * @param mockMessage message to validate
     * @param messageType type of message RQ/RS
     */

    private void validate(MockMessage mockMessage, MessageType messageType) {
        if (mockMessage.isFaultMessage()) return;
        final String systemName = mockMessage.getSystem().getSystemName();
        final String payload = mockMessage.getPayload();

        log.debug("Validate [" + systemName + "] " + messageType.name());
        List<String> validationErrors = messageValidationService.validate(payload, systemName);
        if (validationErrors.size() > 0) {
            mockMessage.setPayload(wrapMessageWithSoapFault("Validation error", ValidationUtils.getSolidErrorMessage(validationErrors)));
            mockMessage.setFaultMessage(true);
        }
        log.debug("Validation status: OK!");
    }

    private void assertElementName(MockMessage mockMessage, MessageType messageType) throws SaxonApiException, XmlException, MessageValidationException {
        System system = mockMessage.getSystem();
        IntegrationPoint integrationPoint = mockMessage.getIntegrationPoint();
        messageValidationService.assertMessageElementName(mockMessage.getPayload(), system, integrationPoint, messageType);
    }

    public void log(MockMessage mockMessage, MessageType messageType) {
        String fullEndpointName = mockMessage.getProtocol().equals(Protocol.JMS) ? mockMessage.getJmsConnectionFactoryName() + "/" + mockMessage.getQueue() : "";
        IntegrationPoint integrationPoint = mockMessage.getIntegrationPoint();
        String shortEndpointName = mockMessage.getProtocol().equals(Protocol.JMS) ? mockMessage.getQueue() : (integrationPoint != null ? integrationPoint.getName() : "<Unknown>");
        String messageState = MessageStatusConverter.convert(mockMessage, messageType).toString();
        String messagePreview = XmlUtils.compactXml(mockMessage.getPayload().length() > 50 ? mockMessage.getPayload().substring(0, 46) + "..." : mockMessage.getPayload());

        System system = mockMessage.getSystem();
        LogsEntity entity = new LogsEntity(
                mockMessage.getTransactionId(),
                mockMessage.getProtocol().toString(),
                system != null ? system.getSystemName() : "-- Unknown --",
                integrationPoint != null ? integrationPoint.getName() : "-- Unknown --",
                fullEndpointName,
                shortEndpointName,
                messageState,
                messagePreview,
                mockMessage.getPayload()
        );
        logService.write(entity);
    }

    public String wrapMessageWithSoapFault(String code, String message) {
        return String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soapenv:Body>\n" +
                "    <soapenv:Fault>\n" +
                "      <faultcode>%s</faultcode>\n" +
                "      <faultstring>%s</faultstring>\n" +
                "    </soapenv:Fault>\n" +
                "  </soapenv:Body>\n" +
                "</soapenv:Envelope>", code, message);
    }

}
