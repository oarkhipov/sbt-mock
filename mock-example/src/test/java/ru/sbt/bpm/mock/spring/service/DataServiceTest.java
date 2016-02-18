package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * @author sbt-bochev-as on 31.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/dataServiceTest/mockapp-servlet.xml"})
public class DataServiceTest {

    @Autowired
    DataService dataService;

    @Autowired
    XmlGeneratorService generatorService;

    @Test
    public void testInitHttpValidator() throws Exception {
        String xml = generatorService.generate("CRM2", "test1", true);
        log.info(xml);
        assertTrue(!xml.isEmpty());
        assertTrue(dataService.validate(xml, "CRM2"));
    }
}