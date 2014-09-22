/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.sbt.bpm.mock.service;

import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author sbt-barinov-sv
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/mockapp-servlet.xml"})
public class ChannelServiceTest {
    private static String MSG = "test";
    private static String CHANNEL = "toGateChannel";
    @Autowired
    ApplicationContext appContext;
    
    @Autowired
    ChannelService service;
    
    public ChannelServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }



    /**
     * Test of sendMessage method, of class ChannelService.
     */
    @Test
    public void testSendMessage() {
        System.out.println("sendMessage");
        String channelName = CHANNEL;
        String payload = MSG;
        service.sendMessage(channelName, payload);
    }

    /**
     * Test of clearMessages method, of class ChannelService.
     */
    @Test
    public void testClearMessages() {
        System.out.println("clearMessages");
        String channelName = CHANNEL;
        service.sendMessage(channelName, MSG);
        service.clearMessages(channelName);
        assertEquals(0, service.getPayloadsCount(channelName));
    }

    /**
     * Test of getPayloadsCount method, of class ChannelService.
     */
    @Test
    public void testGetPayloadsCount() {
        System.out.println("getPayloadsCount");
        String channelName = CHANNEL;
        int expResult = service.getPayloadsCount(channelName) + 1;
        service.sendMessage(channelName, MSG);
        int result = service.getPayloadsCount(channelName);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPayload method, of class ChannelService.
     */
    @Test
    public void testGetPayload() {
        System.out.println("getPayload");
        String channelName = CHANNEL;
        int index = service.getPayloadsCount(channelName);
        testSendMessage();
        String expResult = MSG;
        String result = service.getPayload(channelName, index);
        assertEquals(expResult, result);
    }
    
}
