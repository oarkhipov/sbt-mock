package ru.sbt.bpm.mock.spring.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * @author sbt-bochev-as on 02.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class GroovyServiceTest {

    @Autowired
    GroovyService groovyService;

    @Test
    public void testCompile() throws Exception {
        Assert.assertTrue(groovyService.compile("", "", "").equals(""));
        Assert.assertTrue(groovyService.compile("", "test", "").equals("test"));
        Assert.assertEquals("test12", groovyService.compile("", "test${a}", "response.a=\"12\""));
        Assert.assertEquals("test12", groovyService.compile("", "test${a}", "response.b=\"test\"\n response.a=\"12\""));
        Assert.assertEquals("<a>12</a>", groovyService.compile("", "<a>${a}</a>", "response.b=\"test\"\n response.a=\"12\""));
    }
}