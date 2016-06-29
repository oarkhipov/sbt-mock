package queue;

import lombok.extern.slf4j.Slf4j;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.spring.service.ValidateQueueService;

import javax.naming.NamingException;

/**
 * Created by sbt-langueva-ep on 20.06.2016.
 */
@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-test.xml"})
public class QueueValidatorTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ValidateQueueService validateQueueService;

    private Server server;

    @Test
    public void testingQueueValidator() {
        String test = "MockInboundRequest";
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
        String test = "driverConnectionInputString";
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
    protected void springTestContextPrepareTestInstance() throws Exception {
        super.springTestContextPrepareTestInstance();
    }

    @BeforeClass
    @Override
    protected void springTestContextBeforeTestClass() throws Exception {
        super.springTestContextBeforeTestClass();
        server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
//        handler.addServletWithMapping(SoapMessageValidationServiceTestIT.MockServlet.class, "/");
        server.start();
    }

    @AfterClass
    @Override
    protected void springTestContextAfterTestClass() throws Exception {
        super.springTestContextAfterTestClass();
        server.stop();
    }
}
