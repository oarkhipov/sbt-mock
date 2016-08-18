package ru.sbt.bpm.mock.spring.utils;

import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.enums.DispatcherTypes;
import ru.sbt.bpm.mock.utils.DispatcherUtils;

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

    private String xpath = "//*[local-name()='xsdFieldName']/text()";
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

    @Test
    public void testCheckXpathConcat() throws Exception {
        assertTrue( DispatcherUtils.check(xml, DispatcherTypes.XPATH, "concat(//*:errorText/text(),//*:clientSystemTaskID/text())", null, "errorText1clientSystemT11") );
    }

    @Test
    public void testSapGetLimitXpathSuccess() throws Exception {
        String xml = "<ns2:Envelope\n" +
                "        xmlns:ns1=\"http://sbrf.ru/ESB/mq/headers\"\n" +
                "        xmlns:ns2=\"http://sbrf.ru/NCPCA/SAPLM/GetLimitMessageElements/\">\n" +
                "    <ns1:Header>\n" +
                "        <ns1:message-id>79cb77fb4cc34641a4acde21f5e22a6a</ns1:message-id>\n" +
                "        <ns1:request-time>2016-08-12T11:31:33.230Z</ns1:request-time>\n" +
                "        <ns1:eis-name>urn:sbrfsystems:99-apkkb</ns1:eis-name>\n" +
                "        <ns1:system-id>urn:sbrfsystems:99-saplm</ns1:system-id>\n" +
                "        <ns1:operation-name>GetLimitRq</ns1:operation-name>\n" +
                "        <ns1:operation-version>20</ns1:operation-version>\n" +
                "    </ns1:Header>\n" +
                "    <ns3:Body\n" +
                "            xmlns:ns1=\"http://sbrf.ru/NCPCA/SAPLM/types\"\n" +
                "            xmlns:ns2=\"http://sbrf.ru/NCPCA/SAPLM/GetLimitRq/\"\n" +
                "            xmlns:ns3=\"http://sbrf.ru/NCPCA/SAPLM/GetLimitMessageElements/\">\n" +
                "        <ns2:GetLimitRq>\n" +
                "            <ns1:SourceKey>\n" +
                "                <ns1:SourceSystem>BPM</ns1:SourceSystem>\n" +
                "                <ns1:LegalEntity>SBRF</ns1:LegalEntity>\n" +
                "            </ns1:SourceKey>\n" +
                "            <ns2:LimitFilter>\n" +
                "                <ns1:ValidityDate>9999-12-31</ns1:ValidityDate>\n" +
                "                <ns1:IncludeArchLimits>false</ns1:IncludeArchLimits>\n" +
                "                <ns1:GetFChars>true</ns1:GetFChars>\n" +
                "                <ns1:GetPos>true</ns1:GetPos>\n" +
                "                <ns1:GetUtlSum>false</ns1:GetUtlSum>\n" +
                "                <ns1:GetSnglUtlSum>false</ns1:GetSnglUtlSum>\n" +
                "                <ns1:GetMChars>true</ns1:GetMChars>\n" +
                "                <ns1:GetNotes>true</ns1:GetNotes>\n" +
                "                <ns1:GetCharTexts>false</ns1:GetCharTexts>\n" +
                "                <ns1:LimSecTypeRange>\n" +
                "                    <ns1:RangeLsec>\n" +
                "                        <ns1:Sign>I</ns1:Sign>\n" +
                "                        <ns1:Option>BT</ns1:Option>\n" +
                "                        <ns1:Low>11</ns1:Low>\n" +
                "                        <ns1:High>30</ns1:High>\n" +
                "                    </ns1:RangeLsec>\n" +
                "                    <ns1:TRangesLtype>\n" +
                "                        <ns1:Sign>E</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>C11</ns1:Low>\n" +
                "                    </ns1:TRangesLtype>\n" +
                "                    <ns1:TRangesLtype>\n" +
                "                        <ns1:Sign>E</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>C12</ns1:Low>\n" +
                "                    </ns1:TRangesLtype>\n" +
                "                    <ns1:TRangesLtype>\n" +
                "                        <ns1:Sign>E</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>C21</ns1:Low>\n" +
                "                    </ns1:TRangesLtype>\n" +
                "                    <ns1:TRangesLtype>\n" +
                "                        <ns1:Sign>E</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>C22</ns1:Low>\n" +
                "                    </ns1:TRangesLtype>\n" +
                "                    <ns1:TRangesLtype>\n" +
                "                        <ns1:Sign>E</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>C31</ns1:Low>\n" +
                "                    </ns1:TRangesLtype>\n" +
                "                    <ns1:TRangesLtype>\n" +
                "                        <ns1:Sign>E</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>C32</ns1:Low>\n" +
                "                    </ns1:TRangesLtype>\n" +
                "                </ns1:LimSecTypeRange>\n" +
                "                <ns1:LimCharRange>\n" +
                "                    <ns1:FieldName>/BA1/C20BPART</ns1:FieldName>\n" +
                "                    <ns1:TRanges>\n" +
                "                        <ns1:Sign>I</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>CRM_1-5LN92LS</ns1:Low>\n" +
                "                    </ns1:TRanges>\n" +
                "                    <ns1:TRanges>\n" +
                "                        <ns1:Sign>I</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>MDM_EKMdmId1</ns1:Low>\n" +
                "                    </ns1:TRanges>\n" +
                "                </ns1:LimCharRange>\n" +
                "                <ns1:LimCharRange>\n" +
                "                    <ns1:FieldName>/BIC/ZLIMVERS</ns1:FieldName>\n" +
                "                    <ns1:TRanges>\n" +
                "                        <ns1:Sign>I</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>APR</ns1:Low>\n" +
                "                    </ns1:TRanges>\n" +
                "                    <ns1:TRanges>\n" +
                "                        <ns1:Sign>I</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>VCL</ns1:Low>\n" +
                "                    </ns1:TRanges>\n" +
                "                    <ns1:TRanges>\n" +
                "                        <ns1:Sign>I</ns1:Sign>\n" +
                "                        <ns1:Option>BT</ns1:Option>\n" +
                "                        <ns1:Low>V01</ns1:Low>\n" +
                "                        <ns1:High>V03</ns1:High>\n" +
                "                    </ns1:TRanges>\n" +
                "                </ns1:LimCharRange>\n" +
                "                <ns1:LimCharRange>\n" +
                "                    <ns1:FieldName>/BIC/ZCRMDEAL</ns1:FieldName>\n" +
                "                    <ns1:TRanges>\n" +
                "                        <ns1:Sign>I</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>1-5NJ77YF</ns1:Low>\n" +
                "                    </ns1:TRanges>\n" +
                "                    <ns1:TRanges>\n" +
                "                        <ns1:Sign>I</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>NO_DEAL</ns1:Low>\n" +
                "                    </ns1:TRanges>\n" +
                "                </ns1:LimCharRange>\n" +
                "                <ns1:LimCharRange>\n" +
                "                    <ns1:FieldName>/BIC/ZFLLIMFT</ns1:FieldName>\n" +
                "                    <ns1:TRanges>\n" +
                "                        <ns1:Sign>I</ns1:Sign>\n" +
                "                        <ns1:Option>EQ</ns1:Option>\n" +
                "                        <ns1:Low>X</ns1:Low>\n" +
                "                    </ns1:TRanges>\n" +
                "                </ns1:LimCharRange>\n" +
                "            </ns2:LimitFilter>\n" +
                "        </ns2:GetLimitRq>\n" +
                "    </ns3:Body>\n" +
                "</ns2:Envelope>\n";
        String xpath = "/*[local-name()='Envelope' and namespace-uri()='http://sbrf.ru/NCPCA/SAPLM/GetLimitMessageElements/']/*[local-name()='Body' and namespace-uri()='http://sbrf.ru/NCPCA/SAPLM/GetLimitMessageElements/']/*[local-name()='GetLimitRq' and namespace-uri()='http://sbrf.ru/NCPCA/SAPLM/GetLimitRq/']/*[local-name()='LimitFilter' and namespace-uri()='http://sbrf.ru/NCPCA/SAPLM/GetLimitRq/']/*[local-name()='LimCharRange' and namespace-uri()='http://sbrf.ru/NCPCA/SAPLM/types']/*[local-name()='TRanges' and namespace-uri()='http://sbrf.ru/NCPCA/SAPLM/types']/*[local-name()='Option' and namespace-uri()='http://sbrf.ru/NCPCA/SAPLM/types'][1]/text()";
        assertTrue( DispatcherUtils.check(xml, DispatcherTypes.XPATH, xpath, null, "EQ") );
        assertTrue( DispatcherUtils.check(xml, DispatcherTypes.XPATH, "//*[local-name()='Low' and namespace-uri()='http://sbrf.ru/NCPCA/SAPLM/types' and contains(text(),'NO_DEAL')]/text()", null, "NO_DEAL") );
    }

}