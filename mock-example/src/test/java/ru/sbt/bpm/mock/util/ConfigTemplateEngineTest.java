package ru.sbt.bpm.mock.util;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.junit.Before;
import org.junit.Ignore;
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
        System.out.println("actual result:");
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
        System.out.println("actual result:");
        String result = engine.applyTemplate("${CHANELS}");
        System.out.println(result);
        matches(expectedValue, result);
    }

    @Test
    //TODO @Vostrikov слабый тест: HardCode!
    public void testROUTER_MAPPINGTemplate() throws Exception {
        System.out.println("Config: " + testContainer.getConfig().getFilename());
        System.out.println("FilePath: " + testContainer.getFilePath());
        TemplateEngineBean engine = TemplateEngine.getInstance(testContainer.getConfig());
        System.out.println("test [${ROUTER_MAPPING}] template:");
        String expectedValue = "\r\n" +
                "\t\t<!-- CRM -->\r\n" +
                "\t\t<ix:mapping value=\"CreateTaskClientService\" channel=\"CreateTaskChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"GetParticipantsOfCreditDeal\" channel=\"GetParticipantsChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"SaveCreditDealData\" channel=\"SaveDealChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"UpdateReference\" channel=\"UpdateRefChannel\"/>\r\n" +
                "\r\n" +
                "\t\t<!-- AMRLiRT -->\r\n" +
                "\t\t<ix:mapping value=\"SrvCalcDebtCapacity\" channel=\"CalculateDebtCapacityChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"SrvCalcLGD\" channel=\"CalculateLGDChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"SrvCalcRating\" channel=\"CalculateRatingChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"SrvConfirmRating\" channel=\"ConfirmRatingChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"SrvUpdateRating\" channel=\"CorrectRatingChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"SrvFinalizeLGD\" channel=\"FinalizeLGDChannel\"/>\r\n" +
                "\r\n" +
                "\t\t<!-- FinRep -->\r\n" +
                "\t\t<ix:mapping value=\"SrvGetFinReport\" channel=\"importFinReportChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"SrvGetRatingsAndFactors\" channel=\"importRatingChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"SrvUpdateRating\" channel=\"updateRatingChannel\"/>\r\n" +
                "\t\t<ix:mapping value=\"SrvGetExchangeRates\" channel=\"getExchangeRatesChannel\"/>\r\n";
        System.out.println("expected result:");
        System.out.println(expectedValue);
        System.out.println("actual result:");
        String result = engine.applyTemplate("${ROUTER_MAPPING}");
        System.out.println(result);
        matches(expectedValue, result);
    }

    @Test
    @Ignore
    //TODO @Vostrikov тест не работает при запуске из Maven
    public void testFilenameTemplate() throws Exception {
        TemplateEngineBean engine = TemplateEngine.getInstance(testContainer.getConfig());
        System.out.println("test [${CONFIG_FILENAME}] template:");
        String expectedValue = "NCPConfig.xml";
        System.out.println("expected result:");
        System.out.println(expectedValue);
        System.out.println("actual result:");
        String result = engine.applyTemplate("${CONFIG_FILENAME}");
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
