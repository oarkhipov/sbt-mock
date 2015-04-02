package ru.sbt.bpm.mock.tests;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import ru.sbt.bpm.mock.generator.spring.integration.GatewayContextGenerator;
import ru.sbt.bpm.mock.generator.spring.integration.GenerateMockAppServlet;
import ru.sbt.bpm.mock.spring.utils.Xsl20Transformer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
// тест генератора контекста на чтение
public class GeneratorTest {

    @Test
    public void testAllMockxsl() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        String rootpath = dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl";
        File xslDir = new File(rootpath);

        assert xslDir.isDirectory() : "попали не в ту папку";
        File[] files = xslDir.listFiles();
        for(File f : files) {
            List<File> xslt = new ArrayList<File>();
            if (!f.getName().equals("util") && f.isDirectory()) {
                getXSLTFiles(xslt, f);

                for(File fx : xslt)
                {
                    String name = FilenameUtils.removeExtension(fx.getName());
                    String system = f.getName();
                    if (checkFileIsMockOrDriver(fx)) {
                        String resultSubPath = "xml/" + f.getName() + "/" + name + "/";

                        testXSLTmock(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\" + system + "\\" + name + ".xsl",
                                dir + "\\..\\..\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq1.xml",
                                "xml/" + system + "/" + name + "/rs1.xml");
                        System.out.println(name + " part one Done!");

                        testXSLTmock(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\"+system+"\\"+name+".xsl",
                                dir + "\\..\\..\\src\\test\\resources\\xml\\"+system+"\\"+name+"\\rq2.xml",
                                "xml/"+system+"/"+name+"/rs2.xml");
                        System.out.println(name + " part two Done!");

                        System.out.println();
                        System.out.println(name + " Mock Done!");
                        System.out.println();
                    } else {

                        checkXSLTdriver(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\"+system+"\\"+name+".xsl",
                                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\"+system+"\\xml\\"+name+"Data.xml",
                                "xml/"+system+"/"+name+"/rq1.xml");
                        System.out.println(name + " part one Done!");

                        Map<String, String> params = new HashMap<String, String>(1);
                        params.put("name","test1");
                        checkXSLTdriver(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\"+system+"\\"+name+".xsl",
                                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\"+system+"\\xml\\"+name+"Data.xml",
                                "xml/"+system+"/"+name+"/rq2.xml", params);
                        System.out.println(name + " part two Done!");

                        System.out.println();
                        System.out.println(name + " Driver Done!");
                        System.out.println();
                    }
                }
            }
        }
    }

    /**
     * проверяем, явлется ли xsl драйвером или заглушкой
     * @throws Exception
     */
    private boolean checkFileIsMockOrDriver(File f) throws Exception {
        String xml = FileUtils.readFileToString(f);
        if (xml.contains("<xsl:template match=\"/\">")) {
            return false; //драйвер
        } else if (xml.contains("<xsl:template match=\"soap:Envelope\">")) {
            return true; //заглушка
        }
        throw new IllegalArgumentException("file is nor driver nor mock");
    }

    /**
     * получить все xsl-файлы из папки
     * @param xslts Out выходной список файлов
     * @param file входной файл/дирректория
     */
    private void getXSLTFiles(List<File> xslts, File file) {
        if (file.isDirectory()) {
            for(File f : file.listFiles()) {
                getXSLTFiles(xslts, f);
            }
        } else {
            if (file.getName().endsWith(".xsl")) {
                xslts.add(file);
            }
        }
    }

    private void testXSLTmock(String XSLTFile, String XMLFile, String validateFile) throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        String result = Xsl20Transformer.transform(XSLTFile, XMLFile);
        String validateFileXML = FileUtils.readFileToString(new File(dir + validateFile));

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        Diff diff = new Diff(validateFileXML,result);
        if (!diff.identical()) {
            DetailedDiff detailedDiff = new DetailedDiff(diff);
            List differences = detailedDiff.getAllDifferences();
            for (Object difference : differences) {
                System.out.println("***********************");
                System.out.println(String.valueOf((Difference) difference));
            }

            assertEquals(validateFileXML, result);
        }

    }

    protected void checkXSLTdriver (String XSLTFile, String XMLFile, String validateFile ) throws Exception {
        checkXSLTdriver(XSLTFile, XMLFile, validateFile, null);
    }

    protected void checkXSLTdriver (String XSLTFile, String XMLFile, String validateFile, Map<String,String> params ) throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        String result = Xsl20Transformer.transform(XSLTFile, XMLFile, params);
        String validateFileXML = FileUtils.readFileToString(new File(dir + validateFile));

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        Diff diff = new Diff(validateFileXML,result);
        if (!diff.identical()) {
            DetailedDiff detailedDiff = new DetailedDiff(diff);
            List differences = detailedDiff.getAllDifferences();
            for (Object difference : differences) {
                System.out.println("***********************");
                System.out.println(String.valueOf((Difference) difference));
            }

            assertEquals(validateFileXML, result);
        }
    }

    //    @Test
//    public void testGeneratorSingletonWithOutFile() throws Exception {
//        GenerateMockAppServlet gen1 = GenerateMockAppServlet.getInstance();
//        GenerateMockAppServlet gen2 = GenerateMockAppServlet.getInstance();
//        assertEquals(gen1, gen2);
//        System.out.println(gen1.getaFilePath() + " || " + gen2.getaFilePath());
//        assertEquals(gen1.getaFilePath(), gen2.getaFilePath());
//        System.out.println("================================");
//    }

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
        gen1.setFilePath(file);
        gen1.init();

        assertNotNull(gen1.getMockConfig());
    }

    @Test
    public void testGatewayContextGenerator() throws Exception {
        final String expectedIN = "<inbound-gateway id=\"jmsinAMRLiRT\" request-channel=\"IN.AMRLiRT.1\" reply-channel=\"OUT.AMRLiRT.1\"/>/n/n<inbound-gateway id=\"jmsinCRM\" request-channel=\"IN2\" reply-channel=\"OUT2\"/>/n/n";
        final String expectedOUT = "<outbound-gateway id=\"jmsoutAMRLiRT\" request-channel=\"IN.AMRLiRT.2\" reply-channel=\"OUT.AMRLiRT.2\"/>/n/n<outbound-gateway id=\"jmsoutCRM\" request-channel=\"IN1\" reply-channel=\"OUT1\"/>/n/n";

        final String file = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF\\MockConfigFiles\\MockConfig1.xml";
        GenerateMockAppServlet gen1 = GenerateMockAppServlet.getInstance(file);
        gen1.setFilePath(file);
        gen1.init();

        GatewayContextGenerator gcg = new GatewayContextGenerator(gen1.getMockConfig().getListOfSystems());
        gcg.putChannelsToMap();
        System.out.println(gcg.getInboundAndOutboundGateway());

//        assertEquals(pair.getaFirst(), expectedIN);
//        assertEquals(pair.getaSecond(), expectedOUT);
    }
}
