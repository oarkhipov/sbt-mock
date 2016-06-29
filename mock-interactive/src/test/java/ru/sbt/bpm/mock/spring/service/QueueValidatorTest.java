package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.mortbay.jetty.Server;
import org.mortbay.xml.XmlConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.NamingException;
import java.io.File;

/**
 * Created by sbt-langueva-ep on 20.06.2016.
 */
@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-test-jndi-services.xml"})
public class QueueValidatorTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ValidateQueueService validateQueueService;

    private Server server;

    @Test
    public void testingQueueValidatorQueue1() {
        String test = "jms/Q.LEGAL.TO.CRMORG.SYNCRESP";
        Boolean tmp = false;
        try {
            tmp = validateQueueService.valid(test);
        } catch (NamingException e) {
            log.error("Failed! Queue not found", e);
        }
        Assert.assertTrue(tmp);
    }

    @Test
    public void testingQueueValidatorQueue2() {
        String test = "jms/Q.LEGAL.FROM.CRMORG";
        Boolean tmp = false;
        try {
            tmp = validateQueueService.valid(test);
        } catch (NamingException e) {
            log.error("Failed! Queue not found", e);
        }
        Assert.assertTrue(tmp);
    }

    @Test
    public void testingQueueValidatorQueue3() {
        String test = "jms/Q.LEGAL.FROM.CRMORG.SYNCRESP";
        Boolean tmp = false;
        try {
            tmp = validateQueueService.valid(test);
        } catch (NamingException e) {
            log.error("Failed! Queue not found", e);
        }
        Assert.assertTrue(tmp);
    }

    @Test
    public void testingQueueValidatorWrongData() {
        String test = "jms/Q.LEGAL.TO.CRMORG";
        Boolean tmp = false;
        try {
            tmp = validateQueueService.valid(test);
        } catch (NamingException e) {
            e.printStackTrace();
            log.error("Failed! Queue not found", e);
        }
        Assert.assertFalse(tmp);
    }

    @BeforeClass
    @Override
    protected void springTestContextPrepareTestInstance () throws Exception {
        super.springTestContextPrepareTestInstance();
    }

    @BeforeClass
    @Override
    protected void springTestContextBeforeTestClass() throws Exception {
        super.springTestContextBeforeTestClass();
        server = new Server();
        File             file = new File(this.getClass().getClassLoader().getResource("WEB-INF/jetty/jetty-env.xml").getFile());
        XmlConfiguration xmlConfiguration = new XmlConfiguration(file.toURI().toURL());
        xmlConfiguration.configure(server);
        server.start();
    }

    @AfterClass
    @Override
    protected void springTestContextAfterTestClass() throws Exception {
        super.springTestContextAfterTestClass();
        server.stop();
    }
}
