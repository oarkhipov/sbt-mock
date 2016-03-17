package ru.sbt.bpm.mock.spring.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;
import ru.sbt.bpm.mock.spring.integration.gateway.ClientService;

import static org.junit.Assert.assertEquals;

/**
 * @author sbt-bochev-as on 10.02.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/env/aggregation-mockapp-servlet.xml"})
public class MessageAggregatorTest {

    @Autowired
    ClientService service;

    @Test
    public void testAggregate() throws Exception {
        String message = "test message";
        final MockMessage mockMessage = service.sendMockMessage(message);
        assertEquals(message, mockMessage.getPayload());
        assertEquals("test endpoint", mockMessage.getQueue());
    }
}