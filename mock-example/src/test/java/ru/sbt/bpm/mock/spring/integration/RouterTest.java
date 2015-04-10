package ru.sbt.bpm.mock.spring.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.sbt.bpm.mock.spring.integration.service.ChannelService;
import ru.sbt.bpm.mock.spring.utils.XmlUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by sbt-bochev-as on 11.03.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/env/mockapp-servlet-routerTest.xml"})
public class RouterTest {
    @Autowired
    ChannelService service;

    @Test
    public void routerTest1() throws Exception {
        String defaultChannel = "DefaultOutputChannelIfNoOperationRouted";
        String requestChannel = "MockInboundRequest";
        String responseChannel = "CreateTask";
        String request = "routerTest\\rq.xml";

        String message = XmlUtil.docAsString(XmlUtil.createXmlMessageFromResource(request).getPayload());
        service.sendMessage(requestChannel, message);
        assertTrue(service.getPayloadsCount(defaultChannel) == 0);
        assertTrue(service.getPayloadsCount(responseChannel) > 0);
        int index = service.getPayloadsCount(responseChannel);

        assertEquals(message, service.getPayload(responseChannel, index-1));

    }
}
