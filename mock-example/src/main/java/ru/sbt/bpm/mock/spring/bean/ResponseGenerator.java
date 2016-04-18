package ru.sbt.bpm.mock.spring.bean;

import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.entities.XpathSelector;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.GroovyService;
import ru.sbt.bpm.mock.spring.service.message.JmsService;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;
import ru.sbt.bpm.mock.spring.service.message.validation.ValidationUtils;
import ru.sbt.bpm.mock.spring.utils.XpathUtils;

import javax.xml.xpath.XPathExpressionException;
import java.util.List;

/**
 * @author sbt-bochev-as on 23.11.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Slf4j
@Component
public class ResponseGenerator {

    @Autowired
    private GroovyService groovyService;

    @Autowired
    private MessageValidationService messageValidationService;

    @Autowired
    private DataFileService dataFileService;

    @Autowired
    private MockConfigContainer configContainer;

    @Autowired
    private JmsService jmsService;

    public MockMessage proceedJmsRequest(MockMessage mockMessage) throws Exception {
        mockMessage.setProtocol(Protocol.JMS);
        final System systemByPayload = jmsService.getSystemByPayload(mockMessage.getPayload(), mockMessage.getQueue());
        mockMessage.setSystem(systemByPayload);

        MockMessage responseMockMessage = proceedAbstractMessageRequest(mockMessage);

        System system = responseMockMessage.getSystem();
        IntegrationPoint integrationPoint = responseMockMessage.getIntegrationPoint();
        String outcomeQueue = system.getMockOutcomeQueue();
        if (outcomeQueue == null || outcomeQueue.isEmpty()) {
            outcomeQueue = integrationPoint.getOutcomeQueue();
        }
        responseMockMessage.setQueue(outcomeQueue);
        return responseMockMessage;
    }

    public MockMessage proceedWsRequest(MockMessage mockMessage, String systemName) {
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
            mockMessage.setPayload(e.getMessage());
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
    protected void findIntegrationPoint(MockMessage mockMessage) throws XPathExpressionException, SaxonApiException {
        final System system = mockMessage.getSystem();
        final String payload = mockMessage.getPayload();

        final XpathSelector integrationPointSelector = system.getIntegrationPointSelector();
        final int xpathSize = integrationPointSelector.getElementSelectors().size();
        final String lastElement = integrationPointSelector.getElementSelectors().get(xpathSize - 1).getElement();

        String integrationPointSelectorXpath = integrationPointSelector.toXpath();
        XdmValue value = XpathUtils.evaluateXpath(payload, integrationPointSelectorXpath);
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

        String message = dataFileService.getCurrentMessage(system.getSystemName(), integrationPoint.getName());
        String script = dataFileService.getCurrentScript(system.getSystemName(), integrationPoint.getName());
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
            mockMessage.setPayload(ValidationUtils.getSolidErrorMessage(validationErrors));
            mockMessage.setFaultMessage(true);
        }
        log.debug("Validation status: OK!");
    }

    private void assertMessageByXpath(MockMessage mockMessage, MessageType messageType) throws SaxonApiException {
        System system = mockMessage.getSystem();
        IntegrationPoint integrationPoint = mockMessage.getIntegrationPoint();
        messageValidationService.assertXpath(mockMessage.getPayload(), system, integrationPoint, messageType);
    }

    private void log(MockMessage mockMessage, MessageType messageType) {
        //TODO log implementation
    }
}
