package ru.sbt.bpm.mock.spring.utils;

import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.enums.DispatcherTypes;

import static org.testng.Assert.*;

/**
 * @author sbt-bochev-as on 04.08.2016.
 *         <p>
 *         Company: SBT - Moscow
 */
public class DispatcherUtilsTest {

    private String xml = "<ns:Envelope xmlns:ns=\"http://sbrf.ru/legal/enquiry/integration\">\n" +
            "  <ns:confirmationMessage>\n" +
            "    <ns:errorMessage>\n" +
            "      <ns1:errorCode xmlns:ns1=\"http://sbrf.ru/legal/enquiry/integration/types\">errorCo1</ns1:errorCode>\n" +
            "      <ns1:errorShortDescription xmlns:ns1=\"http://sbrf.ru/legal/enquiry/integration/types\">errorShortDescription1</ns1:errorShortDescription>\n" +
            "      <ns1:errorText xmlns:ns1=\"http://sbrf.ru/legal/enquiry/integration/types\">errorText1</ns1:errorText>\n" +
            "      <ns1:validationErrorsOnFields xmlns:ns1=\"http://sbrf.ru/legal/enquiry/integration/types\">\n" +
            "        <ns1:xsdFieldName>xsdFieldName1</ns1:xsdFieldName>\n" +
            "      </ns1:validationErrorsOnFields>\n" +
            "    </ns:errorMessage>\n" +
            "    <ns:clientSystemTaskID>clientSystemT11</ns:clientSystemTaskID>\n" +
            "  </ns:confirmationMessage>\n" +
            "</ns:Envelope>";

    private String xpath = "//*[local-name()='xsdFieldName']";
    private String regex = "clientSystemTaskID>(.*?)<";

    @Test
    public void testCheckXpathSucceed() throws Exception {
        assertTrue( DispatcherUtils.check(xml, DispatcherTypes.XPATH, xpath, null, "xsdFieldName1") );
    }

    @Test
    public void testCheckXpathFail() throws Exception {
        assertFalse( DispatcherUtils.check(xml, DispatcherTypes.XPATH, xpath, null, "xsdFieldName2") );
    }

    @Test
    public void testCheckRegexSuccess() throws Exception {
        assertTrue( DispatcherUtils.check(xml, DispatcherTypes.REGEX, regex, "$1", "clientSystemT11") );
        assertTrue( DispatcherUtils.check(xml, DispatcherTypes.REGEX, regex, "$0", "clientSystemTaskID>clientSystemT11<") );
        assertTrue( DispatcherUtils.check(xml, DispatcherTypes.REGEX, regex, "$0 $1", "clientSystemTaskID>clientSystemT11< clientSystemT11") );
    }

    @Test
    public void testCheckRegexFail() throws Exception {
        assertFalse( DispatcherUtils.check(xml, DispatcherTypes.REGEX, regex, "$1", "clientSystemT12") );
        assertFalse( DispatcherUtils.check(xml, DispatcherTypes.REGEX, regex, "$0", "clientSystemT11") );
    }

    @Test
    public void testCheckGroovySuccess() throws Exception {
        assertTrue( DispatcherUtils.check(xml, DispatcherTypes.GROOVY, "return 1", null, "1") );
        assertTrue( DispatcherUtils.check(xml, DispatcherTypes.GROOVY, "return 2+2", null, "4") );
        assertTrue( DispatcherUtils.check(xml, DispatcherTypes.GROOVY, "return request.length() > 0", null, "true") );
    }

    @Test
    public void testCheckGroovyFail() throws Exception {
        assertFalse( DispatcherUtils.check(xml, DispatcherTypes.GROOVY, "2;1; 5", null, "1") );
    }

}