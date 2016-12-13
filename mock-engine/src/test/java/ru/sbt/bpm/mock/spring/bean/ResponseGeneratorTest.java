package ru.sbt.bpm.mock.spring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.chain.entities.ChainsEntity;
import ru.sbt.bpm.mock.chain.repository.ChainsRepository;
import ru.sbt.bpm.mock.chain.service.ChainsService;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.MockChain;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;
import ru.sbt.bpm.mock.spring.service.XmlGeneratorService;
import ru.sbt.bpm.mock.spring.service.message.JmsService;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.MockMessageValidationException;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


/**
 * @author sbt-bochev-as on 29.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class ResponseGeneratorTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    XmlGeneratorService generatorService;

    @Autowired
    ResponseGenerator responseGenerator;

    @Autowired
    MessageValidationService messageValidationService;

    @Autowired
    JmsService jmsService;

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    ChainsService chainsService;

    @Autowired
    ChainsRepository repository;

    //TODO Fix it
    @Test(enabled = false)
    public void testGetSystemName() throws Exception {
        String payload1 = generatorService.generate("CRM", "getReferenceData", MessageType.RQ, true);
        List<MockMessageValidationException> validationErrors = messageValidationService.validate(payload1, "CRM");
        if (validationErrors.size() > 0) {
            logger.error(payload1);
            for (MockMessageValidationException validationError : validationErrors) {
                logger.error(validationError);
            }
        }
        assertEquals(validationErrors.size(), 0);
        String payload2 = generatorService.generate("CRM", "getAdditionalInfo", MessageType.RQ, true);

        validationErrors = messageValidationService.validate(payload2, "CRM");
        if (validationErrors.size() > 0) {
            logger.error(payload2);
            for (MockMessageValidationException validationError : validationErrors) {
                logger.error(validationError);
            }
        }
        assertEquals(validationErrors.size(), 0);
        assertEquals("CRM", jmsService.getSystemByPayload(payload1).getSystemName());
        assertEquals("CRM", jmsService.getSystemByPayload(payload2).getSystemName());

    }

    @Test
    public void testGetIntegrationPoint() throws Exception {
        String payload = generatorService.generate("CRM", "getReferenceData", MessageType.RS, true);
        MockMessage mockMessage = new MockMessage(Protocol.JMS, "", "", payload);
        mockMessage.setSystem(jmsService.getSystemByPayload(payload));
        responseGenerator.findIntegrationPoint(mockMessage);
        assertEquals("getReferenceData", mockMessage.getIntegrationPoint().getName());
    }

    @Test
    public void execMockChainTest() throws Exception {
        repository.delete(repository.findAll());
        String systemName = "CRM";
        String integrationPointName = "getReferenceData";

        System system = configContainer.getSystemByName(systemName);
        IntegrationPoint integrationPoint = configContainer.getIntegrationPointByName(systemName, integrationPointName);
        assertTrue(chainsService.getChainsToExecute().size() == 0);
        addChainToConfig(systemName, integrationPointName, integrationPoint);

        String payload = generatorService.generate(systemName, integrationPointName, MessageType.RS, true);
        MockMessage mockMessage = new MockMessage(Protocol.JMS, "", "", payload);
        mockMessage.setSystem(system);
        mockMessage.setIntegrationPoint(integrationPoint);

        responseGenerator.execMockChain(mockMessage);
        List<ChainsEntity> chainsInQueue = chainsService.getChainsInQueue();
        assertEquals(chainsInQueue.size(), 1);
        assertEquals(chainsService.getChainsToExecute().size(), 0);
        ChainsEntity chainsEntity = chainsInQueue.get(0);

        assertEquals(chainsEntity.getMessage(), payload);

        Thread.sleep(1200);
        assertEquals(chainsService.getChainsInQueue().size(), 1);
//        assertEquals(chainsService.getChainsToExecute().size(), 1);
        Thread.sleep(4000);
    }

    private void addChainToConfig(String systemName, String integrationPointName, IntegrationPoint integrationPoint) {
        List<MockChain> mockChainList = integrationPoint.getMockChains().getMockChainList();
        String templateMessage = "${res}";
        String script = "response.res = request;";
        String testMessage = "";
        mockChainList.add(new MockChain(1200L, systemName, integrationPointName, templateMessage, script, testMessage));
    }
}