package ru.sbt.bpm.mock.util;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.junit.Before;
import org.junit.Test;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.bean.TemplateEngineBean;
import ru.sbt.bpm.mock.generator.util.TemplateEngine.TemplateEngine;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sbt-vostrikov-mi on 10.04.2015.
 */
public class ConfigTemplateEngineTest {

    MockConfigContainer configContainer;
    MockConfigContainer testContainer;

    @Before
    public void initConfig() throws Exception {
//        configContainer = MockConfigContainer.getInstance("src/test/resources/xml/MockConfigFiles/MockConfig.xml");
        configContainer = MockConfigContainer.getInstance("src/main/webapp/resources/MockConfigFiles/NCPConfig.xml");
        testContainer = MockConfigContainer.getInstance("src/test/resources/xml/MockConfigFiles/MockConfig.xml");

        configContainer.init();
    }

    @Test
    public void testTemplateEngine() throws Exception {
        TemplateEngineBean engine = TemplateEngine.getInstance(configContainer.getConfig());
        String input = "agg${}aeuio${test}rarhhadrha${test}eagh${notexistingTemplate}asreh${test}";
        String result = engine.applyTemplate(input);
        assert result.equals("agg${}aeuiotestrarhhadrhatesteagh${notexistingTemplate}asrehtest") : result + " from " + input;
        System.out.println(result);
        System.out.println("___[${CHANELS}]_:");
        System.out.println(engine.applyTemplate("___[${CHANELS}]_"));
        System.out.println("___[${ROUTER_MAPPING}]_:");
        System.out.println(engine.applyTemplate("___[${ROUTER_MAPPING}]_"));
        System.out.println("___[${BEANS_RefreshableXSLTransformer}]_:");
        System.out.println(engine.applyTemplate("___[${BEANS_RefreshableXSLTransformer}]_"));
        System.out.println("___[${TRANSFORMERS}]_:");
        System.out.println(engine.applyTemplate("___[${TRANSFORMERS}]_"));
    }

    @Test
    public void testGateTemplate() throws Exception {
        TemplateEngineBean engine = TemplateEngine.getInstance(testContainer.getConfig());
        System.out.println("test [${GATEWAY}] template:");
        String expectedValue = "<inbound-gateway id=\"jmsin\" request-channel=\"IN.KKMB.2\" reply-channel=\"OUT.KKMB2\"/>\n" +
                "\n" +
                "<outbound-gateway id=\"jmsout\" request-channel=\"IN.KKMB.2\" reply-channel=\"OUT.KKMB2\"/>\n" +
                "\n";
        System.out.println("expected result:");
        System.out.println(expectedValue);
        String result = engine.applyTemplate("${GATEWAY}");
        System.out.println(result);
        matches(expectedValue, result);
    }

    @Test
    public void testChanelsTemplate() throws Exception {
        TemplateEngineBean engine = TemplateEngine.getInstance(testContainer.getConfig());
        System.out.println("test [${CHANELS}] template:");
        String expectedValue = "\r\n" +
                "\t<!-- CRM -->\r\n" +
                "\t<channel id=\"CreateTaskChannel\"/>\r\n" +
                "\t<channel id=\"ForceSignalChannel\"/>\r\n" +
                "\t<channel id=\"GetParticipantsChannel\"/>\r\n" +
                "\t<channel id=\"SaveDealChannel\"/>\r\n" +
                "\t<channel id=\"UpdateDealChannel\"/>\r\n" +
                "\t<channel id=\"UpdateRefChannel\"/>\r\n" +
                "\r\n" +
                "\t<!-- AMRLiRT -->\r\n" +
                "\t<channel id=\"CalculateDebtCapacityChannel\"/>\r\n" +
                "\t<channel id=\"CalculateLGDChannel\"/>\r\n" +
                "\t<channel id=\"CalculateRatingChannel\"/>\r\n" +
                "\t<channel id=\"ConfirmRatingChannel\"/>\r\n" +
                "\t<channel id=\"CorrectRatingChannel\"/>\r\n" +
                "\t<channel id=\"FinalizeLGDChannel\"/>\r\n" +
                "\r\n" +
                "\t<!-- FinRep -->\r\n" +
                "\t<channel id=\"importFinReportChannel\"/>\r\n" +
                "\t<channel id=\"importRatingChannel\"/>\r\n" +
                "\t<channel id=\"updateRatingChannel\"/>\r\n" +
                "\t<channel id=\"getExchangeRatesChannel\"/>\r\n";
        System.out.println("expected result:");
        System.out.println(expectedValue);
        String result = engine.applyTemplate("${CHANELS}");
        System.out.println(result);
        matches(expectedValue, result);
    }

    @Test
    public void testROUTER_MAPPINGTemplate() throws Exception {
        TemplateEngineBean engine = TemplateEngine.getInstance(testContainer.getConfig());
        System.out.println("test [${ROUTER_MAPPING}] template:");
        String expectedValue = "\r\n" +
                "\t\t<!-- CRM -->\r\n" +
                "\t\t<ix:mapping value=\"createTaskRs\" channel=\"CreateTaskChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"prtspRs\" channel=\"GetParticipantsChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"saveDealRs\" channel=\"SaveDealChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"updateRefRs\" channel=\"UpdateRefChannel\"/>\r\n" +
                "\r\n" +
                "\t\t<!-- AMRLiRT -->\r\n" +
                "\t\t<ix:mapping value=\"calculateDCRs\" channel=\"CalculateDebtCapacityChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"calculateLGDRs\" channel=\"CalculateLGDChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"calculateRatingRs\" channel=\"CalculateRatingChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"confirmRatingRs\" channel=\"ConfirmRatingChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"correctRatingRs\" channel=\"CorrectRatingChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"finalizeLGDRs\" channel=\"FinalizeLGDChannel\"/>\r\n" +
                "\r\n" +
                "\t\t<!-- FinRep -->\r\n" +
                "\t\t<ix:mapping value=\"importFinReportRs\" channel=\"importFinReportChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"importRatingRs\" channel=\"importRatingChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"updateRatingRs\" channel=\"updateRatingChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"getExchangeRatesRs\" channel=\"getExchangeRatesChannel\"/>\r\n";
        System.out.println("expected result:");
        System.out.println(expectedValue);
        String result = engine.applyTemplate("${ROUTER_MAPPING}");
        System.out.println(result);
        matches(expectedValue, result);
    }

    @Test
    public void testBEANS_RefreshableXSLTransformerTemplate() throws Exception {
        TemplateEngineBean engine = TemplateEngine.getInstance(testContainer.getConfig());
        System.out.println("test [${BEANS_RefreshableXSLTransformer}] template:");
        String expectedValue = "\r\n" +
                "\t\t<!-- CRM -->\r\n" +
                "\t<beans:bean id=\"CRM_CreateTask\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/CRM/CreateTask.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\t<beans:bean id=\"CRM_GetParticipants\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/CRM/GetParticipants.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\t<beans:bean id=\"CRM_SaveDeal\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/CRM/SaveDeal.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\t<beans:bean id=\"CRM_UpdateRef\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/CRM/UpdateRef.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\r\n" +
                "\t\t<!-- AMRLiRT -->\r\n" +
                "\t<beans:bean id=\"AMRLiRT_CalculateDebtCapacity\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/AMRLiRT/CalculateDebtCapacity.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\t<beans:bean id=\"AMRLiRT_CalculateLGD\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/AMRLiRT/CalculateLGD.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\t<beans:bean id=\"AMRLiRT_CalculateRating\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/AMRLiRT/CalculateRating.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\t<beans:bean id=\"AMRLiRT_ConfirmRating\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/AMRLiRT/ConfirmRating.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\t<beans:bean id=\"AMRLiRT_CorrectRating\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/AMRLiRT/CorrectRating.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\t<beans:bean id=\"AMRLiRT_FinalizeLGD\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/AMRLiRT/FinalizeLGD.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\r\n" +
                "\t\t<!-- FinRep -->\r\n" +
                "\t<beans:bean id=\"FinRep_importFinReport\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/FinRep/importFinReport.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\t<beans:bean id=\"FinRep_importRating\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/FinRep/importRating.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\t<beans:bean id=\"FinRep_updateRating\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/FinRep/updateRating.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n" +
                "\t<beans:bean id=\"FinRep_getExchangeRates\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
                "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/FinRep/getExchangeRates.xsl\"/>\r\n" +
                "\t</beans:bean>\r\n";
        System.out.println("expected result:");
        System.out.println(expectedValue);
        String result = engine.applyTemplate("${BEANS_RefreshableXSLTransformer}");
        System.out.println(result);
        matches(expectedValue, result);
    }

    @Test
    public void testTRANSFORMERSTemplate() throws Exception {
        TemplateEngineBean engine = TemplateEngine.getInstance(testContainer.getConfig());
        System.out.println("test [${TRANSFORMERS}] template:");
        String expectedValue = "\r\n" +
                "\t\t<!-- CRM -->\r\n" +
                "\t<transformer input-channel=\"CreateTask\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"CRM_CreateTask\"/>\r\n" +
                "\t<transformer input-channel=\"GetParticipants\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"CRM_GetParticipants\"/>\r\n" +
                "\t<transformer input-channel=\"SaveDeal\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"CRM_SaveDeal\"/>\r\n" +
                "\t<transformer input-channel=\"UpdateRef\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"CRM_UpdateRef\"/>\r\n" +
                "\r\n" +
                "\t\t<!-- AMRLiRT -->\r\n" +
                "\t<transformer input-channel=\"CalculateDebtCapacity\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"AMRLiRT_CalculateDebtCapacity\"/>\r\n" +
                "\t<transformer input-channel=\"CalculateLGD\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"AMRLiRT_CalculateLGD\"/>\r\n" +
                "\t<transformer input-channel=\"CalculateRating\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"AMRLiRT_CalculateRating\"/>\r\n" +
                "\t<transformer input-channel=\"ConfirmRating\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"AMRLiRT_ConfirmRating\"/>\r\n" +
                "\t<transformer input-channel=\"CorrectRating\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"AMRLiRT_CorrectRating\"/>\r\n" +
                "\t<transformer input-channel=\"FinalizeLGD\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"AMRLiRT_FinalizeLGD\"/>\r\n" +
                "\r\n" +
                "\t\t<!-- FinRep -->\r\n" +
                "\t<transformer input-channel=\"importFinReport\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"FinRep_importFinReport\"/>\r\n" +
                "\t<transformer input-channel=\"importRating\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"FinRep_importRating\"/>\r\n" +
                "\t<transformer input-channel=\"updateRating\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"FinRep_updateRating\"/>\r\n" +
                "\t<transformer input-channel=\"getExchangeRates\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"FinRep_getExchangeRates\"/>\r\n";
        System.out.println("expected result:");
        System.out.println(expectedValue);
        String result = engine.applyTemplate("${TRANSFORMERS}");
        System.out.println(result);
        matches(expectedValue, result);
    }

    @Test
    public void testMockupServletTemplate() throws Exception {
        TemplateEngineBean engine = TemplateEngine.getInstance(configContainer.getConfig());

        String file = this.getClass().getClassLoader().getResource("mockupServletTemplate/mockupServletTemplate.xml").getFile();
        String templ = FileUtils.readFileToString(new File(file));

        System.out.println(file + ":");
        System.out.println(templ);
        System.out.println("\r\r\n ------ \r\r\n");

        System.out.println(engine.applyTemplate(templ));
    }

    private void matches(String expected, String  result) {
        assertEquals(expected, result);
    }
}
