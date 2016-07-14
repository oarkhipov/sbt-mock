package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;

import static org.testng.Assert.assertTrue;


/**
 * @author sbt-bochev-as on 31.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-jms-http.xml"})
public class JmsMessageValidationServiceTestIT extends AbstractVirtualHttpServerTentNGSpringContextTests {

    @Autowired
    MessageValidationService messageValidationService;

    @Autowired
    XmlGeneratorService generatorService;

    @Test
    public void testInitHttpValidator() throws Exception {
        String xml = generatorService.generate("CRM2", "test1", MessageType.RS, true);
        log.info(xml);
        assertTrue(!xml.isEmpty());
        assertTrue(messageValidationService.validate(xml, "CRM2").size() == 0, messageValidationService.validate(xml, "CRM2").toString());
    }
}