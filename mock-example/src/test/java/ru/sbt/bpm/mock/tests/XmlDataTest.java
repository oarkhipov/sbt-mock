package ru.sbt.bpm.mock.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.sbt.bpm.mock.spring.integration.service.XmlDataService;

import static org.junit.Assert.*;

/**
 * Created by sbt-bochev-as on 13.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class XmlDataTest {
    @Autowired
    XmlDataService xmlDataService;

    @Test
    public void validateTest() throws Exception {
        assertTrue(xmlDataService.validate(xmlDataService.getDataXml("CRM_CreateTask")));
    }

    @Test
    public void customTest() throws Exception {
        assertTrue(xmlDataService.validate(xmlDataService.getXml("in/test.xml")));
    }
}
