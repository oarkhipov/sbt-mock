package ru.sbt.bpm.mock.tests;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import ru.sbt.bpm.mock.utils.XmlUtil;
import ru.sbt.bpm.mock.utils.Xsl20Transformer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by sbt-vostrikov-mi on 29.12.2014.
 */
public class Xsl20TransformTest {


    @Test
    public void testForceSignalXSLTtoData() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\util\\xsdToDataXsd.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsd\\CRM\\ForceSignalRequest.xsd",
                "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\CRM\\xsd\\ForceSignalRequestData.xsd");
    }

    @Test
    public void testUpdateDealRequestXSLTtoData() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\util\\xsdToDataXsd.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsd\\CRM\\UpdateDealRequest.xsd",
                "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\CRM\\xsd\\UpdateDealRequestData.xsd");
    }

    @Test
    public void testCreateTaskResponseXSLTtoData() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\util\\xsdToDataXsd.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsd\\CRM\\CreateTaskResponse.xsd",
                "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\CRM\\xsd\\CreateTaskResponseData.xsd");
    }

    @Test
    public void testUpdateRefResponseXSLTtoData() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\util\\xsdToDataXsd.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsd\\CRM\\UpdateRefResponse.xsd",
                "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\CRM\\xsd\\UpdateRefResponseData.xsd");
    }

    @Test
    public void testGetParticipantsResponseXSLTtoData() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);

        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","PrtspRs");

        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\util\\xsdToDataXsd.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsd\\CRM\\GetParticipantsResponse.xsd",
                "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\CRM\\xsd\\GetParticipantsResponseData.xsd", params);
    }

    @Test
    public void testSaveDealXSLTtoData() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\util\\xsdToDataXsd.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsd\\CRM\\SaveDealResponse.xsd",
                "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\CRM\\xsd\\SaveDealResponseData.xsd");
    }

    @Test
    public void testXSDtoXSLForceSignal() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\util\\requestXSDtoXSL.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsd\\CRM\\ForceSignalRequest.xsd",
                "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\CRM\\ForceSignal.xsl");
    }

    @Test
    public void testXSDtoXSLUpdateDeal() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\util\\requestXSDtoXSL.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsd\\CRM\\UpdateDealRequest.xsd",
                "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\CRM\\UpdateDeal.xsl");
    }

    /*Тест устарел
    @Test
    public void testXSDtoXSLCreateTask() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\util\\responceXSDtoXSL.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsd\\CRM\\CreateTaskResponse.xsd",
                "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\CRM\\CreateTask.xsl");
    }*/

    /*Тест устарел
    @Test
    public void testXSDtoXSLGetParticipants() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();


        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","PrtspRs");
        params.put("dataFileName","GetParticipantsData.xml");
        System.out.println(dir);
        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\util\\responceXSDtoXSL.xsl",
                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsd\\CRM\\GetParticipantsResponse.xsd",
                "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\CRM\\GetParticipants.xsl", params);
    }*/

    protected void checkXSLT (String XSLTFile, String XMLFile, String validateFile ) throws Exception {
        checkXSLT (XSLTFile, XMLFile, validateFile, null);
    }


    protected void checkXSLT (String XSLTFile, String XMLFile, String validateFile, Map<String,String> params ) throws Exception {

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

    protected void checkXMLDataRowToXMLDataListByString (String XSLTFile, String XMLFile, String validateString ) throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        String result = Xsl20Transformer.transform(XSLTFile, XMLFile);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        assertEquals(validateString, result);
    }




}
