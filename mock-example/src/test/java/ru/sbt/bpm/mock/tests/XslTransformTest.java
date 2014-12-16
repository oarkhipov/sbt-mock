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

    @Test
    public void testXMLDataRowToXMLDataListWithParam2 () throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);

        String XSLTFile = dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\DataRowToDataList.xsl";
        String XMLFile = dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\CRM\\xml\\ForceSignalData.xml";
        String validateString = "<request xmlns=\"http://sbrf.ru/NCP/CRM/ForceSignalRq/Data/\" name=\"test1\">\r\n" +
                "        <contractID>string1-2</contractID>\r\n" +
                "        <contractBPMID>string2-2</contractBPMID>\r\n" +
                "        <status>string3-2</status>\r\n" +
                "        <comment>string4-2</comment>\r\n" +
                "        <requestType>string5-2</requestType>\r\n" +
                "        <fullNameOfResponsiblePerson>string6-2</fullNameOfResponsiblePerson>\r\n" +
                "        <!--Zero or more repetitions:-->\r\n" +
                "        <participantsGroup>\r\n" +
                "            <id>string1-2</id>\r\n" +
                "            <label>string2-2</label>\r\n" +
                "            <status>string3-2</status>\r\n" +
                "            <updateDate>2008-09-04</updateDate>\r\n" +
                "            <approvalDate>2014-09-05</approvalDate>\r\n" +
                "            <topLevelGroupName>string6-2</topLevelGroupName>\r\n" +
                "        </participantsGroup>\r\n" +
                "    </request>";

        String result = XslTransformer.transform(XSLTFile, XMLFile, "name", "test1");

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


    @Test
    public void testApplyRowToDataList () throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);

        String XSLTFile = dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\ApplyRowToDataList.xsl";
        String XMLFile = dir + "\\..\\..\\src\\test\\resources\\xmlAssertion\\dataSingleNode.xml";
        String validateString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><data>\r\n" +
                "<request xmlns=\"http://sbrf.ru/NCP/CRM/ForceSignalRq/Data/\" name=\"default\">\r\n" +
                "        <contractID>string1</contractID>\r\n" +
                "        <contractBPMID>string2</contractBPMID>\r\n" +
                "        <status>string3</status>\r\n" +
                "        <comment>string4</comment>\r\n" +
                "        <requestType>string5</requestType>\r\n" +
                "        <fullNameOfResponsiblePerson>string6</fullNameOfResponsiblePerson>\r\n" +
                "        <!--Zero or more repetitions:-->\r\n" +
                "        <participantsGroup>\r\n" +
                "            <id>string1</id>\r\n" +
                "            <label>string2</label>\r\n" +
                "            <status>string3</status>\r\n" +
                "            <updateDate>2008-09-04</updateDate>\r\n" +
                "            <approvalDate>2014-09-05</approvalDate>\r\n" +
                "            <topLevelGroupName>string6</topLevelGroupName>\r\n" +
                "        </participantsGroup>\r\n" +
                "    </request>\r\n" +
                "<request xmlns=\"http://sbrf.ru/NCP/CRM/ForceSignalRq/Data/\" name=\"test1\">\r\n" +
                "        <contractID>string1-2</contractID>\r\n" +
                "        <contractBPMID>string2-2</contractBPMID>\r\n" +
                "        <status>string3-2</status>\r\n" +
                "        <comment>string4-2</comment>\r\n" +
                "        <requestType>string5-2</requestType>\r\n" +
                "        <fullNameOfResponsiblePerson>string6-2</fullNameOfResponsiblePerson>\r\n" +
                "        <!--Zero or more repetitions:-->\r\n" +
                "        <participantsGroup>\r\n" +
                "            <id>string1-2</id>\r\n" +
                "            <label>string2-2</label>\r\n" +
                "            <status>string3-2</status>\r\n" +
                "            <updateDate>2008-09-04</updateDate>\r\n" +
                "            <approvalDate>2014-09-05</approvalDate>\r\n" +
                "            <topLevelGroupName>string6-2</topLevelGroupName>\r\n" +
                "        </participantsGroup>\r\n" +
                "    </request>\r\n" +
                "<request xmlns=\"http://sbrf.ru/NCP/CRM/ForceSignalRq/Data/\" name=\"test2\">\r\n" +
                "    <contractID>string1-2</contractID>\r\n" +
                "    <contractBPMID>string2-2</contractBPMID>\r\n" +
                "    <status>string3-2</status>\r\n" +
                "    <comment>string4-2</comment>\r\n" +
                "    <requestType>string5-2</requestType>\r\n" +
                "    <fullNameOfResponsiblePerson>string6-2</fullNameOfResponsiblePerson>\r\n" +
                "    <!--Zero or more repetitions:-->\r\n" +
                "    <participantsGroup>\r\n" +
                "        <id>string1-2</id>\r\n" +
                "        <label>string2-2</label>\r\n" +
                "        <status>string3-2</status>\r\n" +
                "        <updateDate>2008-09-04</updateDate>\r\n" +
                "        <approvalDate>2014-09-05</approvalDate>\r\n" +
                "        <topLevelGroupName>string6-2</topLevelGroupName>\r\n" +
                "    </participantsGroup>\r\n" +
                "</request>\r\n" +
                "</data>\r\n";

        String result = XslTransformer.transform(XSLTFile, XMLFile, "dataFile", "CRM\\xml\\ForceSignalData.xml");

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        assertEquals(validateString, result);
    }

    @Test
    public void testApplyRowToDataList2 () throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);

        String XSLTFile = dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\ApplyRowToDataList.xsl";
        String XMLFile = dir + "\\..\\..\\src\\test\\resources\\xmlAssertion\\dataSingleNode.xml";
        String validateString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><data>\r\n" +
                "<response xmlns=\"http://sbrf.ru/NCP/AMRLIRT/CalculateDCRs/Data\" name=\"default\">\r\n" +
                "        <errorCode>s</errorCode>\r\n" +
                "        <!--Optional:-->\r\n" +
                "        <errorMessage>string1-two</errorMessage>\r\n" +
                "        <crmId>string1</crmId>\r\n" +
                "        <!--Optional:-->\r\n" +
                "        <rmk>1000.001</rmk>\r\n" +
                "        <debtCapacity>1000.001</debtCapacity>\r\n" +
                "        <!--Optional:-->\r\n" +
                "        <rmkInDealCurrency>1000.003</rmkInDealCurrency>\r\n" +
                "        <debtCapacityInDealCurrency>1000.001</debtCapacityInDealCurrency>\r\n" +
                "        <!--Optional:-->\r\n" +
                "        <rmkForNextYear>1000.001</rmkForNextYear>\r\n" +
                "        <!--Optional:-->\r\n" +
                "        <debtCapacityForNextYear>1000.001</debtCapacityForNextYear>\r\n" +
                "        <listOfAddParameter>\r\n" +
                "            <!--Zero or more repetitions:-->\r\n" +
                "            <addParameter>\r\n" +
                "                <!--Optional:-->\r\n" +
                "                <order>3</order>\r\n" +
                "                <name>string</name>\r\n" +
                "                <value>string</value>\r\n" +
                "            </addParameter>\r\n" +
                "            <addParameter>\r\n" +
                "                <!--Optional:-->\r\n" +
                "                <order>4</order>\r\n" +
                "                <name>string2</name>\r\n" +
                "                <value>string2</value>\r\n" +
                "            </addParameter>\r\n" +
                "        </listOfAddParameter>\r\n" +
                "        <!--Optional:-->\r\n" +
                "        <amMessage>anyType1</amMessage>\r\n" +
                "    </response>\r\n" +
                "<response xmlns=\"http://sbrf.ru/NCP/AMRLIRT/CalculateDCRs/Data\" name=\"testError\">\r\n" +
                "        <errorCode>e</errorCode>\r\n" +
                "        <!--Optional:-->\r\n" +
                "        <errorMessage>testing error message</errorMessage>\r\n" +
                "    </response>\r\n" +
                "<request xmlns=\"http://sbrf.ru/NCP/CRM/ForceSignalRq/Data/\" name=\"test2\">\r\n" +
                "    <contractID>string1-2</contractID>\r\n" +
                "    <contractBPMID>string2-2</contractBPMID>\r\n" +
                "    <status>string3-2</status>\r\n" +
                "    <comment>string4-2</comment>\r\n" +
                "    <requestType>string5-2</requestType>\r\n" +
                "    <fullNameOfResponsiblePerson>string6-2</fullNameOfResponsiblePerson>\r\n" +
                "    <!--Zero or more repetitions:-->\r\n" +
                "    <participantsGroup>\r\n" +
                "        <id>string1-2</id>\r\n" +
                "        <label>string2-2</label>\r\n" +
                "        <status>string3-2</status>\r\n" +
                "        <updateDate>2008-09-04</updateDate>\r\n" +
                "        <approvalDate>2014-09-05</approvalDate>\r\n" +
                "        <topLevelGroupName>string6-2</topLevelGroupName>\r\n" +
                "    </participantsGroup>\r\n" +
                "</request>\r\n" +
                "</data>\r\n";

        String result = XslTransformer.transform(XSLTFile, XMLFile, "dataFile", "AMRLiRT\\xml\\SrvCalcDebtCapacityData.xml");

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        assertEquals(validateString, result);
    }


}
