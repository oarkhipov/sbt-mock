package ru.sbt.bpm.mock.spring.integration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by sbt-bochev-as on 12.05.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/env/jmsGateways.xml"})
@Profile("testCase")
public class JmsGateway {

    @Autowired
    MessageChannel stdinToJmsOutChannel;

    @Autowired
    QueueChannel queueChannel;

    @Test
    public void testJmsGateway() {
        stdinToJmsOutChannel.send(MessageBuilder.withPayload("jms test").build());

        @SuppressWarnings("unchecked")
        Message<String> reply = (Message<String>) queueChannel.receive(20000);
        Assert.assertNotNull(reply.getPayload());
        String out = reply.getPayload();

        Assert.assertEquals("JMS response: JMS TEST", out);
    }
}
