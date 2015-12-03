package ru.sbt.bpm.mock.spring.integration.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * Created by sbt-bochev-as on 08.05.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class ClientServiceTest {

    @Autowired
    ClientService clientService;

    @Test
    public void testInvoke() throws Exception {
        assertEquals("test",clientService.send("test"));
    }
}