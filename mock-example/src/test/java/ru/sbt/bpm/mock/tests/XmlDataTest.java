package ru.sbt.bpm.mock.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.service.TransformService;
import ru.sbt.bpm.mock.service.XmlDataService;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by sbt-bochev-as on 13.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/mockapp-servlet-test.xml"})
public class XmlDataTest {
    @Autowired
    XmlDataService xmlDataService;

    @Test
    public void validateTest() throws IOException {
        try {
            assertTrue(xmlDataService.validate(xmlDataService.getXml("CRM_CreateTask")));
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
