package ru.sbt.bpm.mock.spring.integration.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


/**
 * Created by sbt-bochev-as on 08.05.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class ClientServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    ClientService clientService;

    @Test
    public void testInvoke() throws Exception {
        assertEquals("test",clientService.send("test"));
    }
}