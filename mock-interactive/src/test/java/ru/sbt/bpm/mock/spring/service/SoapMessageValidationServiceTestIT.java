package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * @author sbt-bochev-as on 31.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-soap-http.xml"})
public class SoapMessageValidationServiceTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    MessageValidationService messageValidationService;

    @Autowired
    XmlGeneratorService generatorService;

    private Server server;

    @Test
    public void testInitHttpValidator() throws Exception {
        String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spy=\"spyne.examples.hello\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <spy:say_helloResponse>\n" +
                "         <!--Optional:-->\n" +
                "         <spy:say_helloResult>\n" +
                "            <!--Zero or more repetitions:-->\n" +
                "            <spy:string>test</spy:string>\n" +
                "         </spy:say_helloResult>\n" +
                "      </spy:say_helloResponse>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        assertTrue(!xml.isEmpty());
        List<String> validationErrors = messageValidationService.validate(xml, "Spyne");
        assertTrue(validationErrors.size() == 0, validationErrors.toString());

        xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spy=\"spyne.examples.hello\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <spy:say_hello/>" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        assertTrue(!xml.isEmpty());
        validationErrors = messageValidationService.validate(xml, "Spyne");
        assertTrue(validationErrors.size() == 0, validationErrors.toString());
    }

    @Test
    public void testInitHttpValidatorWithError() throws Exception {
        String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spy=\"spyne.examples.hello\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <spy:say_helloResponse>\n" +
                "         <!--Optional:-->\n" +
                "           <spy:badTag>Bad Value</spy:badTag>" +
                "         <spy:say_helloResult>\n" +
                "            <!--Zero or more repetitions:-->\n" +
                "            <spy:string>test</spy:string>\n" +
                "         </spy:say_helloResult>\n" +
                "      </spy:say_helloResponse>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        assertTrue(!xml.isEmpty());
        List<String> validationErrors = messageValidationService.validate(xml, "Spyne");
        assertFalse(validationErrors.size() == 0, validationErrors.toString());

        xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spy=\"spyne.examples.hello\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <spy:say_hello>" +
                "        <badTag>test</badTag>" +
                "      </spy:say_hello>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        assertTrue(!xml.isEmpty());
        validationErrors = messageValidationService.validate(xml, "Spyne");
        assertFalse(validationErrors.size() == 0, validationErrors.toString());

    }

    @Test
    public void testInitHttpValidatorWithGenerator() throws Exception {
        String xml = generatorService.generate("Spyne", "say_hello", MessageType.RQ, true).replace("?", "test");
        assertTrue(!xml.isEmpty());
        List<String> validationErrors = messageValidationService.validate(xml, "Spyne");
        assertTrue(validationErrors.size() == 0, validationErrors.toString());

        xml = generatorService.generate("Spyne", "say_hello", MessageType.RS, true).replace("?", "test");
        assertTrue(!xml.isEmpty());
        validationErrors = messageValidationService.validate(xml, "Spyne");
        assertTrue(validationErrors.size() == 0, validationErrors.toString());
    }

    @Test
    public void testInitHttpValidatorWithGeneratorErrors() throws Exception {
        String xml = generatorService.generate("Spyne", "say_hello", MessageType.RQ, true).replace("<spy:say_hello/>", "<spy:say_hello><badTag>test</badTag></spy:say_hello>");
        assertTrue(!xml.isEmpty());
        List<String> validationErrors = messageValidationService.validate(xml, "Spyne");
        assertFalse(validationErrors.size() == 0, validationErrors.toString());

        xml = generatorService.generate("Spyne", "say_hello", MessageType.RS, true).replace("?", "<badTag>test</badTag>");
        assertTrue(!xml.isEmpty());
        validationErrors = messageValidationService.validate(xml, "Spyne");
        assertFalse(validationErrors.size() == 0, validationErrors.toString());
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
        handler.addServletWithMapping(MockServlet.class, "/");
        server.start();
    }

    @AfterClass
    @Override
    protected void springTestContextAfterTestClass() throws Exception {
        super.springTestContextAfterTestClass();
        server.stop();
    }

    @SuppressWarnings("serial")
    public static class MockServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            URL basePath = this.getClass().getClassLoader().getResource("");
            assert basePath != null;
            String fileName = basePath.getPath() + "wsdl" + request.getServletPath().substring("path/to/dir/".length());
            File file = new File(fileName);
            response.setContentType("application/xml");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(FileUtils.readFileToString(file, "UTF-8"));
        }
    }
}