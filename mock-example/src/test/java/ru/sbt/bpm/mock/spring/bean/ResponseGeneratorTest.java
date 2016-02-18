package ru.sbt.bpm.mock.spring.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.sbt.bpm.mock.spring.service.XmlGeneratorService;

import static org.junit.Assert.*;

/**
 * @author sbt-bochev-as on 29.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class ResponseGeneratorTest {

    @Autowired
    XmlGeneratorService generatorService;

    @Autowired
    ResponseGenerator responseGenerator;

    @Test
    public void testRouteAndGenerate() throws Exception {
    }

    @Test
    public void testGetSystemName() throws Exception {
        String payload1 = generatorService.generate("CRM","getReferenceData", true);
        String payload2 = generatorService.generate("CRM","getAdditionalInfo", true);
        assertEquals("CRM", responseGenerator.getSystemName(payload1).getSystemName());
        assertEquals("CRM", responseGenerator.getSystemName(payload2).getSystemName());

    }

    @Test
    public void testGetIntegrationPoint() throws Exception {
        String payload1 = generatorService.generate("CRM","getReferenceData", true);
        assertEquals("getReferenceData", responseGenerator.getIntegrationPoint(responseGenerator.getSystemName(payload1), payload1).getName());
    }
}