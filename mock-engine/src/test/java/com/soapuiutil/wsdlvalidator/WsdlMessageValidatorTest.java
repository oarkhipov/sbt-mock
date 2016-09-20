package com.soapuiutil.wsdlvalidator;


import org.testng.Assert;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.MockMessageValidationException;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Test class
 *
 * @author kchan
 */
public class WsdlMessageValidatorTest {

    private String getWsdlPath() throws Exception {
        URL rootResource = this.getClass().getClassLoader().getResource("");
        assert rootResource != null;
        final String moduleHome = new File(rootResource.getFile()).getParentFile().getParentFile().getCanonicalPath();
        return "file:" + moduleHome + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "wsdl" + File.separator + "spyne.wsdl";
    }

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
        final List<MockMessageValidationException> assertionErrors = rubyValidationWrapper.validateSchemaCompliance(responseString);

        System.out.println("assertion count : " + assertionErrors.size());
        Assert.assertEquals(1, assertionErrors.size());
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
        final List assertionErrors = rubyValidationWrapper.validateSchemaCompliance(responseString);

        System.out.println("assertion count : " + assertionErrors.size());
        Assert.assertEquals(2, assertionErrors.size());
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
        final List assertionErrors = rubyValidationWrapper.validateSchemaCompliance(responseString);

        System.out.println("assertion count : " + assertionErrors.size());
        Assert.assertEquals(0, assertionErrors.size());
    }
}
