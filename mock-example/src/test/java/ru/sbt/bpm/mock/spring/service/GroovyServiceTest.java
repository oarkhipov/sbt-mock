package ru.sbt.bpm.mock.spring.service;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


/**
 * @author sbt-bochev-as on 02.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class GroovyServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    GroovyService groovyService;

    @Test
    public void testCompileEmpty() throws Exception {
        assertEquals("", groovyService.execute("", "", ""));
    }

    @Test
    public void testCompileStatic() throws Exception {
        assertEquals("test", groovyService.execute("", "test", ""));
    }

    @Test
    public void testCompileAutoFill() throws Exception {
        assertEquals("test12", groovyService.execute("", "test${a}", "response.a=\"12\""));
        assertEquals("test12", groovyService.execute("", "test${a}", "response.b=\"test\"\n response.a=\"12\""));
        assertEquals("<a>12</a>", groovyService.execute("", "<a>${a}</a>", "response.b=\"test\"\n response.a=\"12\""));
    }

    @Test
    public void testCompileInline() throws Exception {
        assertEquals("5", groovyService.execute("", "${=new Integer(5)}", ""));
        assertEquals("6", groovyService.execute("", "${= new Integer(6)}", ""));
    }

    @Test
    public void testCompileHugeStaticAlphaNumericText() throws Exception {
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(68000);
        assertEquals(randomAlphanumeric, groovyService.execute("", randomAlphanumeric, ""));
    }

    @Test
    public void testCompileHugeDynamicAlphaNumericText() throws Exception {
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(68000);
        assertEquals(randomAlphanumeric, groovyService.execute("", randomAlphanumeric + "${a}", "response.a=123"));
    }
}