package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.bean.ResponseGenerator;
import ru.sbt.bpm.mock.spring.context.generator.service.SpringContextGeneratorService;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by oleg on 30.12.2016.
 */
@Slf4j
@ContextConfiguration({ "/env/mockapp-simple-proxy.xml" })
public class SimplePorxyTest extends AbstractTestNGSpringContextTests {
    @Autowired
    MockSpringContextGeneratorService generator;

    @Autowired
    SpringContextGeneratorService generatorService;

    @Autowired
    MockConfigContainer mockConfigContainer;

    @Test
    public void testSimpleProxy () throws JAXBException {
        String actual = generatorService.toXml(generator.generateContext());
        log.debug(actual);




        final MessageChannel stdinToJmsoutChannel = applicationContext.getBean("stdinToJmsoutChannel", MessageChannel.class);
        stdinToJmsoutChannel.send(MessageBuilder.withPayload("jms test").build());

        final QueueChannel queueChannel = applicationContext.getBean("queueChannel", QueueChannel.class);

        @SuppressWarnings("unchecked")
        Message<String> reply = (Message<String>) queueChannel.receive(20000);
        Assert.assertNotNull(reply);
        String out = reply.getPayload();

        Assert.assertEquals("JMS response: JMS TEST", out);
    }

    @Test
    public void testSimpleProxy2 () throws Exception {
        String actual = generatorService.toXml(generator.generateContext());
        Resource o = applicationContext.getResource("/env");
        File envDir = o.getFile();
        //GenericApplicationContext parent = new GenericApplicationContext();
        //MockConfigContainer mockConfigContainer = new MockConfigContainer("/env/MockConfigSimpleProxy.xml");
        //parent.getBeanFactory().registerSingleton("mockConfigContainer",mockConfigContainer);
        //parent.refresh();
        //ClassPathXmlApplicationContext  base = new ClassPathXmlApplicationContext(new String[]{"/contextConfigs/base-add-client-service.xml"}, parent);

        String addBeans = "    <int:channel id=\"MockInboundRequestAggregated\"/>\n" +
                "    <int:channel id=\"MockOutboundRouterResponse\"/>\n" +
                "    <int:service-activator input-channel=\"MockInboundRequestAggregated\" output-channel=\"MockOutboundRouterResponse\"\n" +
                "                           expression=\"@responseGenerator.proceedJmsRequest(payload)\"/>\n" +
                "\n" +
                "    <int:channel id=\"DriverOutboundRequest\"/>\n" +
                "\n" +
                "    <int:channel id=\"DriverInboundResponse\"/>\n" +
                "\n" +
                "    <int:gateway id=\"systemEntry\"\n" +
                "             default-request-channel=\"DriverOutboundRequest\"\n" +
                "             default-reply-channel=\"DriverInboundResponse\"\n" +
                "             error-channel=\"DriverInboundResponse\"\n" +
                "             service-interface=\"ru.sbt.bpm.mock.spring.integration.gateway.ClientService\"\n" +
                "             default-reply-timeout=\"30000\"\n" +
                "             default-request-timeout=\"30000\"\n" +
                "    />\n" +
                "\n" +
                "    <bean id=\"mockConfigContainer\" class=\"ru.sbt.bpm.mock.config.MockConfigContainer\">\n" +
                "        <constructor-arg value=\"src\\test\\resources\\env\\MockConfigSimpleProxy.xml\" type=\"java.lang.String\"/>\n" +
                "    </bean>\n"+
                "    <bean id=\"version\" class=\"java.lang.String\">\n" +
                "        <constructor-arg type=\"java.lang.String\" value=\"test.version\"/>\n" +
                "    </bean>\n"+
                "</beans>\n";


        actual = actual.replace("</beans>",addBeans);

        File contextXML = File.createTempFile("~AppContext_SimpleProxy",".tmpxml",envDir);
        log.debug("=============:"+contextXML.getAbsolutePath());
        PrintWriter out = new PrintWriter(contextXML);
        out.print(actual);
        out.flush();
        out.close();
        FileSystemXmlApplicationContext context = null;

        try {
            context = new FileSystemXmlApplicationContext(new String[]{contextXML.getAbsolutePath()});
            XmlGeneratorService xmlGeneratorService = context.getBean(XmlGeneratorService.class);
            String payload = xmlGeneratorService.generate("CRM", "processRequest", MessageType.RS, true);
            ResponseGenerator responseGenerator = (ResponseGenerator) context.getBean("responseGenerator");
            log.debug("responseGenerator.class = " + responseGenerator.getClass().getName());

            if (false){
            InitialContext jndiContext = new InitialContext();
            Queue requestQueue = (Queue) jndiContext.lookup("jms/INBOUND_QUEUE");
            Queue replyQueue = (Queue) jndiContext.lookup("jms/OUTBOUND_QUEUE");
            QueueConnectionFactory qconFactory = (QueueConnectionFactory) jndiContext.lookup("jms/CF");
        /*Queue requestQueue = applicationContext.getBean("requestQueue",Queue.class);
        Queue replyQueue = applicationContext.getBean("replyQueue",Queue.class);
        QueueConnectionFactory qconFactory = (QueueConnectionFactory) applicationContext.getBean("connectionFactory");*/
            QueueConnection qcon = qconFactory.createQueueConnection();
            QueueSession qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueSender qsender = qsession.createSender(requestQueue);
            TextMessage msg = qsession.createTextMessage();
            msg.setText(payload);
            msg.setJMSReplyTo(replyQueue);
            qcon.start();
            qsender.send(msg);

            QueueReceiver r = qsession.createReceiver(replyQueue);
            TextMessage replyMsg = (TextMessage) r.receive(5000);
            if (replyMsg != null)
                log.info("***REPLY MESSAGE***: " + replyMsg.getText());}

        } finally {
            if (context!=null) context.close();
        }
    }

    @Test
    public void testContainer() throws IOException {
        log.debug("systems:"+mockConfigContainer.getConfig().getSystems());
        MockConfigContainer cont = new MockConfigContainer("/env/MockConfigSimpleProxy.xml");
        cont.init();
        log.debug("systems:"+cont.getConfig().getSystems());
    }
}
