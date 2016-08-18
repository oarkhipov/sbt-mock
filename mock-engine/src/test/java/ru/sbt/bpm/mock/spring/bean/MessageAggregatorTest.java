package ru.sbt.bpm.mock.spring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.spring.integration.gateway.ClientService;

import static org.testng.Assert.assertEquals;


/**
 * @author sbt-bochev-as on 10.02.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ContextConfiguration({"/env/aggregation-mockapp-servlet.xml"})
public class MessageAggregatorTest extends AbstractTestNGSpringContextTests {

    @Autowired
    ClientService service;

    @Test
    public void testAggregate() throws Exception {
        String queueName = "test endpoint";
        final String response = service.send("");
        assertEquals(queueName, response);
    }
}