package ru.sbt.bpm.mock.tests;

import org.junit.Test;
import ru.sbt.bpm.mock.generator.spring.integration.GatewayContextGenerator;
import ru.sbt.bpm.mock.generator.spring.integration.GenerateMockAppServlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by sbt-hodakovskiy-da on 02.04.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
// Тест для проверки создание Spring Context File
public class MockConfigTest {

    @Test
    public void testGeneratorSingletonWithFile() throws Exception {
        final String fileExpected = "/xml/MockConfigFiles/MockConfig1.xml";
        GenerateMockAppServlet gen1 = GenerateMockAppServlet.getInstance("/xml/MockConfigFiles/MockConfig1.xml");
        GenerateMockAppServlet gen2 = GenerateMockAppServlet.getInstance("/xml/MockConfigFiles/MockConfig1.xml");
        assertEquals(gen1, gen2);

        System.out.println(gen1.getFilePath() + " || " + gen2.getFilePath());

        assertEquals(fileExpected, gen1.getFilePath());
        assertEquals(fileExpected, gen2.getFilePath());
        System.out.println("================================");
    }

    @Test
    public void testGeneratorSingletonWithDiffFiles() throws Exception {
        final String fileExpected = "/xml/MockConfigFiles/MockConfig1.xml";
        GenerateMockAppServlet gen1 = GenerateMockAppServlet.getInstance("/xml/MockConfigFiles/MockConfig1.xml");

        System.out.println(gen1.getFilePath());

        assertEquals(fileExpected, gen1.getFilePath());

        GenerateMockAppServlet gen2 = GenerateMockAppServlet.getInstance("/xml/MockConfigFiles/MockConfig2.xml");

        System.out.println(gen2.getFilePath());
        assertEquals(fileExpected, gen2.getFilePath());
        System.out.println("================================");
    }

    @Test
    public void testGeneratorMockIsNotNull() throws Exception{
        final String file = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF\\MockConfigFiles\\MockConfig1.xml";
        GenerateMockAppServlet gen1 = GenerateMockAppServlet.getInstance(file);
        gen1.init();

        assertNotNull(gen1.getMockConfig());
    }

    @Test
    public void testGatewayContextGenerator() throws Exception {
        final String expectedIN = "<inbound-gateway id=\"jmsinAMRLiRT\" request-channel=\"IN.AMRLiRT.1\" reply-channel=\"OUT.AMRLiRT.1\"/>/n/n<inbound-gateway id=\"jmsinCRM\" request-channel=\"IN2\" reply-channel=\"OUT2\"/>/n/n";
        final String expectedOUT = "<outbound-gateway id=\"jmsoutAMRLiRT\" request-channel=\"IN.AMRLiRT.2\" reply-channel=\"OUT.AMRLiRT.2\"/>/n/n<outbound-gateway id=\"jmsoutCRM\" request-channel=\"IN1\" reply-channel=\"OUT1\"/>/n/n";

        final String file = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF\\MockConfigFiles\\MockConfig1.xml";
        GenerateMockAppServlet gen1 = GenerateMockAppServlet.getInstance(file);
        gen1.init();

        GatewayContextGenerator gcg = new GatewayContextGenerator(gen1.getMockConfig().getListOfSystems());
        gcg.putChannelsToMap();
        System.out.println(gcg.getInboundAndOutboundGateway());
    }
}
