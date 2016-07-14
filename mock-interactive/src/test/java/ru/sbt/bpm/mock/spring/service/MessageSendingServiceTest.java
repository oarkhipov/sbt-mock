package ru.sbt.bpm.mock.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;

import static org.testng.Assert.*;

/**
 * @author sbt-bochev-as on 14.07.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ContextConfiguration("classpath:/env/mockapp-servlet-jms-http.xml")
public class MessageSendingServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    MessageSendingService messageSendingService;

    @Test
    public void testSendWs() throws Exception {
        MockMessage message = new MockMessage(Protocol.JMS, "", "", "");
        String response = messageSendingService.sendWs(message);
    }
}