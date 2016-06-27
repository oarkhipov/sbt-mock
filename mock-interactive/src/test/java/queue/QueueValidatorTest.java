package queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.spring.service.ValidateQueueService;

import javax.naming.NamingException;

/**
 * Created by sbt-langueva-ep on 20.06.2016.
 */
@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-test.xml"})
@WebAppConfiguration("/mock-interactive/src/main/webapp")
public class QueueValidatorTest extends AbstractTestNGSpringContextTests {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ValidateQueueService validateQueueService;

    @Test
    public void testingQueueValidator(){
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
    public void testingQueueValidatorWrongData(){
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
}
