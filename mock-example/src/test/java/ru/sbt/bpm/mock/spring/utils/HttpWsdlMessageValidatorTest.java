package ru.sbt.bpm.mock.spring.utils;


import com.soapuiutil.wsdlvalidator.WsdlMessageValidator;
import org.apache.commons.io.FileUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Test class
 *
 * @author sbt-bochev-as
 * @since 2016-03-28
 */
public class HttpWsdlMessageValidatorTest {

    private String getWsdlPath() throws Exception {
        return "http://localhost:8080/path/to/dir/spyne.wsdl";
    }

    private Server server;

    private String getSoapEnvelope(final String innerContent) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spy=\"spyne.examples.hello\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                innerContent +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }

    @Test
    public void testValidateRequest() throws Exception {
        final String bodyContent = "<spy:say_helloResponse>" +
                "<spy:say_helloResult>" +
                "<badNode>bad info</badNode>" +
                "</spy:say_helloResult>" +
                "</spy:say_helloResponse>";
        final String responseString = getSoapEnvelope(bodyContent);

        final String wsdlUrl = getWsdlPath();

        final WsdlMessageValidator rubyValidationWrapper = new WsdlMessageValidator(wsdlUrl);
        final String[] assertionErrors = rubyValidationWrapper.validateSchemaCompliance(responseString);

        System.out.println("assertion count : " + assertionErrors.length);
        Assert.assertEquals(1, assertionErrors.length);
    }

    @Test
    public void testValidateSchemaComplianceError() throws Exception {

        final String bodyContent = "<spy:say_helloResponse>" +
                "<spy:say_helloResult>" +
                "<badNode>bad info</badNode>" +
                "<badNode2>bad info 2</badNode2>" +
                "</spy:say_helloResult>" +
                "</spy:say_helloResponse>";
        final String responseString = getSoapEnvelope(bodyContent);

        final String wsdlUrl = getWsdlPath();

        final WsdlMessageValidator rubyValidationWrapper = new WsdlMessageValidator(wsdlUrl);
        final String[] assertionErrors = rubyValidationWrapper.validateSchemaCompliance(responseString);

        System.out.println("assertion count : " + assertionErrors.length);
        Assert.assertEquals(2, assertionErrors.length);
    }

    @Test
    public void testValidateSchemaComplianceNoError() throws Exception {

        final String bodyContent = "<spy:say_helloResponse>" +
                "<spy:say_helloResult>" +
                "</spy:say_helloResult>" +
                "</spy:say_helloResponse>";
        final String responseString = getSoapEnvelope(bodyContent);

        final String wsdlUrl = getWsdlPath();


        final WsdlMessageValidator rubyValidationWrapper = new WsdlMessageValidator(wsdlUrl);
        final String[] assertionErrors = rubyValidationWrapper.validateSchemaCompliance(responseString);

        System.out.println("assertion count : " + assertionErrors.length);
        Assert.assertEquals(0, assertionErrors.length);
    }


    @AfterSuite
    public void tearDown() throws Exception {
        server.stop();
    }

    @BeforeSuite
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
            String fileName = basePath.getPath() + "wsdl" + request.getServletPath().substring("path/to/dir/".length());
            File file = new File(fileName);
            response.setContentType("application/xml");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(FileUtils.readFileToString(file, "UTF-8"));
        }
    }
}
