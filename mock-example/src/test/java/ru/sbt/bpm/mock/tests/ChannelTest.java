package ru.sbt.bpm.mock.tests;

import org.custommonkey.xmlunit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.sbt.bpm.mock.service.ChannelService;
import ru.sbt.bpm.mock.utils.XmlUtil;
import ru.sbt.bpm.mock.utils.Xsl20Transformer;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by sbt-bochev-as on 03.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class ChannelTest {
    @Autowired
    ChannelService service;
//    Request message
    private static String MSGRQ;
    private static String MSGRS;
    private static String IN = "in";
    private static String OUT = "out";

//    CRM testing
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

                        testXSLTmock(name, "MockOutboundResponse", resultSubPath + "rq1.xml", resultSubPath + "rs1.xml");
                        System.out.println(name + " part one Done!");
                        testXSLTmock(name, "MockOutboundResponse", resultSubPath + "rq2.xml", resultSubPath + "rs2.xml");
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
    @Test
    public void createTaskTestAMRLiRT_CalculateDebtCapacity2() throws Exception {
        testXSLT("CalculateDebtCapacity", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateDebtCapacity/rq2.xml", "xml/AMRLiRT/CalculateDebtCapacity/rs2.xml");
    }

    /**
     * забираем из папки все xsl
     */
    private void getXSLTFiles(List<File> xslts, File file) {
        if (file.isDirectory()) {
            for(File f : file.listFiles()) {
                if (!f.getName().equals("util")) {
                    getXSLTFiles(xslts, f);
                }
            }
        } else {
            if (file.getName().endsWith(".xsl")) {
                xslts.add(file);
            }
        }
    }
    
//    BBMO testing
    @Test
    public void createSrvGetClientReferenceDataRq1() throws Exception {
        testXSLTmock("SrvGetClientReferenceDataRq", "MockOutboundResponse", "xml/BBMO/SrvGetClientReferenceDataRq/rq1.xml", "xml/BBMO/SrvGetClientReferenceDataRq/rs1.xml");
    }

    @Test
    public void createSrvGetClientReferenceDataRq2() throws Exception {
        testXSLTmock("SrvGetClientReferenceDataRq", "MockOutboundResponse", "xml/BBMO/SrvGetClientReferenceDataRq/rq2.xml", "xml/BBMO/SrvGetClientReferenceDataRq/rs2.xml");
    }

    //CKPIT
    @Test
    public void testUpdateLoan() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXSLTdriver(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\CKPIT\\UpdateLoan.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\CKPIT\\xml\\UpdateLoanData.xml",
                "xml/CKPIT/UpdateLoan/rq1.xml");
    }



////    CRM testing
//    @Test
//    public void createTaskTest1() throws Exception {
//        testXSLT("CreateTask", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/CreateTask/rq1.xml", "xml/CRM/CreateTask/rs1.xml");
//    }
//
//    @Test
//    public void createTaskTest2() throws Exception {
//        testXSLT("CreateTask", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/CreateTask/rq2.xml", "xml/CRM/CreateTask/rs2.xml");
//    }


    private void testXSLTmock(String INStream, String OUTStream, String request, String responce) throws Exception {
        IN= INStream;
        OUT = OUTStream;
        MSGRQ = XmlUtil.docAsString(XmlUtil.createXmlMessageFromResource(request).getPayload());
        MSGRS = XmlUtil.docAsString(XmlUtil.createXmlMessageFromResource(responce).getPayload());

        service.sendMessage(IN, MSGRQ);
        assertTrue(service.getPayloadsCount(OUT)>0);
        int index = service.getPayloadsCount(OUT);
        String result = service.getPayload(OUT, index-1);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        Diff diff = new Diff(MSGRS,result);
        if (!diff.identical()) {
            DetailedDiff detailedDiff = new DetailedDiff(diff);
            List differences = detailedDiff.getAllDifferences();
            for (Object difference : differences) {
                System.out.println("***********************");
                System.out.println(String.valueOf((Difference) difference));
            }

            assertEquals(MSGRS, result);
        }
    }

    protected void checkXSLTdriver (String XSLTFile, String XMLFile, String validateFile ) throws Exception {
        checkXSLTdriver(XSLTFile, XMLFile, validateFile, null);
    }


    protected void checkXSLTdriver (String XSLTFile, String XMLFile, String validateFile, Map<String,String> params ) throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        String result = Xsl20Transformer.transform(XSLTFile, XMLFile, params);
        String validateFileXML = FileUtils.readFileToString(new File(dir+validateFile));

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


}
