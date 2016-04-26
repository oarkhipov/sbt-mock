package com.soapuiutil.wsdlvalidator;


import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Test class
 *
 * @author kchan
 */
public class WsdlMessageValidatorTest {

    private String getWsdlPath() throws Exception {
        final String currentPath = new java.io.File(".").getCanonicalPath();
//		final String wsdlUrl = "file:" + currentPath + File.separator + ".."  + File.separator + "spec" + File.separator + "wsdl" + File.separator + "spyne.wsdl";
        final String wsdlUrl = "file:" + currentPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "wsdl" + File.separator + "spyne.wsdl";
        return wsdlUrl;
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
}
