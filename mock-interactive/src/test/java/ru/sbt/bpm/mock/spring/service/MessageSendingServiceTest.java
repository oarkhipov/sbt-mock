package ru.sbt.bpm.mock.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;

/**
 * @author sbt-bochev-as on 14.07.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ContextConfiguration("classpath:/env/mockapp-servlet-jms-http.xml")
public class MessageSendingServiceTest extends AbstractVirtualHttpServerTransactionalTestNGSpringContextTests {

    @Autowired
    MessageSendingService messageSendingService;

    @Test(enabled = false)
    public void testSendWs() throws Exception {
        MockMessage message = new MockMessage(Protocol.SOAP, "", "", "");
        String response = messageSendingService.sendWs(message);
    }
}