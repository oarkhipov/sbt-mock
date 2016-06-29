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
public class ValidateQueueTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ValidateQueueService validateQueueService;

    private Server server;

    @Test
    public void testingQueueValidatorQueue1() {
        Assert.assertTrue(validateQueueService.valid("jms/Q.LEGAL.TO.CRMORG.SYNCRESP"));
    }

    @Test
    public void testingQueueValidatorQueue2() {
        Assert.assertTrue(validateQueueService.valid("jms/Q.LEGAL.FROM.CRMORG"));
    }

    @Test
    public void testingQueueValidatorQueue3() {
        Assert.assertTrue(validateQueueService.valid("jms/Q.LEGAL.FROM.CRMORG.SYNCRESP"));
    }

    @Test
    public void testingQueueValidatorWrongData() {
        Assert.assertFalse(validateQueueService.valid("jms/Q.LEGAL.TO.CRMORG"));
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
        File file = new File(this.getClass().getClassLoader().getResource("WEB-INF/jetty/jetty-env.xml").getFile());
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
