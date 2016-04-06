package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.testng.Assert.assertTrue;


/**
 * @author sbt-bochev-as on 31.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-jms-http.xml"})
public class JmsMessageValidationServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    MessageValidationService messageValidationService;

    @Autowired
    XmlGeneratorService generatorService;

    private Server server;

    @Test
    public void testInitHttpValidator() throws Exception {
        String xml = generatorService.generate("CRM2", "test1", MessageType.RS, true);
        log.info(xml);
        assertTrue(!xml.isEmpty());
        assertTrue(messageValidationService.validate(xml, "CRM2").size() == 0, messageValidationService.validate(xml, "CRM2").toString());
    }

    @AfterMethod
    public void tearDown() throws Exception {
        server.stop();
    }

    @BeforeTest
    public void setUp() throws Exception {
        server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(MockServlet.class, "/");
        server.start();

    }

    @SuppressWarnings("serial")
    public static class MockServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            URL basePath = this.getClass().getClassLoader().getResource("");
            assert basePath != null;
            String fileName = basePath.getPath() + "WEB-INF/xsd/CRM" + request.getServletPath().substring("path/to/dir/".length());
            File file = new File(fileName);
            response.setContentType("application/xml");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(FileUtils.readFileToString(file, "UTF-8"));
        }
    }
}