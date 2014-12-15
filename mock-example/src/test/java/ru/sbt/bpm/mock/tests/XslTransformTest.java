package ru.sbt.bpm.mock.tests;


import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import ru.sbt.bpm.mock.utils.XmlUtil;
import ru.sbt.bpm.mock.utils.XslTransformer;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringWriter;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sbt-bochev-as on 12.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
public class XslTransformTest {

    /*@Test
    public void firstTest() throws TransformerException {
        System.out.println(
                XslTransformer.transform("C:\\work\\IdeaProjects\\GitProjects\\Mock\\mock-example\\src\\main\\webapp\\WEB-INF\\xsl\\CRM\\ForceSignal.xsl",
                        "C:\\work\\IdeaProjects\\GitProjects\\Mock\\mock-example\\src\\test\\resources\\xml\\CRM\\ForceSignal\\rq1.xml")
        );
    }*/

    @Test
    public void testForceSignalXSLT() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\CRM\\ForceSignal.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\CRM\\xml\\ForceSignalData.xml",
                "xml/CRM/ForceSignal/rq1.xml");
    }

    @Test
    public void testUpdateDealXSLT() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\CRM\\UpdateDeal.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\CRM\\xml\\UpdateDealData.xml",
                 "xml/CRM/UpdateDeal/rq1.xml");
    }


    @Test
    public void testXMLDataRowToXMLDataList() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXMLDataRowToXMLDataListByString(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\DataRowToDataList.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\CRM\\xml\\UpdateDealData.xml",
                "\r\ndefault\r\nERROR");


        checkXMLDataRowToXMLDataListByString(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\DataRowToDataList.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\AMRLiRT\\xml\\SrvCalcLGDData.xml",
                "\r\ndefault\r\ntestError");


        checkXMLDataRowToXMLDataListByString(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\DataRowToDataList.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\FinRep\\xml\\SrvGetFinReport.xml",
                "\r\ndefault\r\ntestError");
    }

    @Test
    public void testXMLDataRowToXMLDataListWithParam () throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);

        String XSLTFile = dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\DataRowToDataList.xsl";
        String XMLFile = dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\FinRep\\xml\\SrvGetFinReport.xml";
        String validateString = "<response xmlns=\"http://sbrf.ru/NCP/ASFO/GetFinReport/Data\" name=\"testError\">\r\n" +
                "        <errorCode>e</errorCode>\r\n" +
                "        <!--Optional:-->\r\n" +
                "        <errorMessage>testing error message SrvGetFinReport.xml</errorMessage>\r\n" +
                "        <nonCurrentAssetsNFRS>testing error message SrvGetFinReport.xml</nonCurrentAssetsNFRS>\r\n" +
                "    </response>";

        String result = XslTransformer.transform(XSLTFile, XMLFile, "name", "testError");

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        assertEquals(validateString, result);
    }


    protected void checkXSLT (String XSLTFile, String XMLFile, String validateFile ) throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        String result = XslTransformer.transform(XSLTFile, XMLFile);
        String validateFileXML = XmlUtil.docAsString(XmlUtil.createXmlMessageFromResource(validateFile).getPayload());

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

    protected void checkXMLDataRowToXMLDataListByString (String XSLTFile, String XMLFile, String validateString ) throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        String result = XslTransformer.transform(XSLTFile, XMLFile);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        assertEquals(validateString, result);
    }


}
