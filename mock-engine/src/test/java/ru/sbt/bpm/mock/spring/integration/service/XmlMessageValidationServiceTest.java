package ru.sbt.bpm.mock.spring.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;

import static org.testng.Assert.assertNotNull;


/**
 * Created by sbt-bochev-as on 03.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */

@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class XmlMessageValidationServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    MessageValidationService service;

    @Test
    public void testInit() {
        assertNotNull(service);
    }
}