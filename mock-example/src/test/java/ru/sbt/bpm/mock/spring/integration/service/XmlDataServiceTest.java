package ru.sbt.bpm.mock.spring.integration.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * Created by sbt-bochev-as on 03.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class XmlDataServiceTest{

    @Autowired
    XmlDataService service;

    @Test
    public void testInit(){
        assertNotNull(service);
    }
}