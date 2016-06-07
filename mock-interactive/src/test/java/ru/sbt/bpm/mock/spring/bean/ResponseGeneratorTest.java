package ru.sbt.bpm.mock.spring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;
import ru.sbt.bpm.mock.spring.service.XmlGeneratorService;
import ru.sbt.bpm.mock.spring.service.message.JmsService;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;

import java.util.List;

import static org.testng.Assert.assertEquals;


/**
 * @author sbt-bochev-as on 29.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class ResponseGeneratorTest extends AbstractTestNGSpringContextTests {

    @Autowired
    XmlGeneratorService generatorService;

    @Autowired
    ResponseGenerator responseGenerator;

    @Autowired
    MessageValidationService messageValidationService;

    @Autowired
    JmsService jmsService;

    //TODO Fix it
    @Test(enabled = false)
    public void testGetSystemName() throws Exception {
        String payload1 = generatorService.generate("CRM", "getReferenceData", MessageType.RQ, true);
        List<String> validationErrors = messageValidationService.validate(payload1, "CRM");
        if (validationErrors.size() > 0 ) {
            logger.error(payload1);
            for (String validationError : validationErrors) {
                logger.error(validationError);
            }
        }
        assertEquals(validationErrors.size(),0);
        String payload2 = generatorService.generate("CRM", "getAdditionalInfo", MessageType.RQ, true);

        validationErrors = messageValidationService.validate(payload2, "CRM");
        if (validationErrors.size() > 0 ) {
            logger.error(payload2);
            for (String validationError : validationErrors) {
                logger.error(validationError);
            }
        }
        assertEquals(validationErrors.size(),0);
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
}