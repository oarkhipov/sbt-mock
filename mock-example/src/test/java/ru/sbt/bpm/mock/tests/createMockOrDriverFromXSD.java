package ru.sbt.bpm.mock.tests;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import ru.sbt.bpm.mock.utils.Xsl20Transformer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by sbt-vostrikov-mi on 12.01.2015.
 */
public class createMockOrDriverFromXSD {


    @Test
    public void testXSLTtoDataCRMCreateTask() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        mockTestCycle(dir, "CRM", "CreateTask", "Response");
    }
    @Test
    public void testXSLTtoDataCRMForceSignal() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        mockTestCycle(dir, "CRM", "ForceSignal", "Request");
    }
    @Test
    public void testXSLTtoDataCRMGetParticipants() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","PrtspRs");
        params.put("RqEntryPointName","PrtspRq");
        params.put("dataFileName","GetParticipantsData.xml");
        mockTestCycle(dir, "CRM", "GetParticipants", "Response", params);
    }
    @Test
    public void testXSLTtoDataCRMSaveDeal() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        mockTestCycle(dir, "CRM", "SaveDeal", "Response");
    }
    @Test
    public void testXSLTtoDataCRMUpdateDeal() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        mockTestCycle(dir, "CRM", "UpdateDeal", "Request");
    }
    @Test
    public void testXSLTtoDataCRMUpdateRef() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        mockTestCycle(dir, "CRM", "UpdateRef", "Response");
    }
    @Test
    public void testXSLTtoDataAMRLiRTCalculateDebtCapacity() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","DebtCapacityCalculationResponse");
        params.put("RqEntryPointName","DebtCapacityCalculationRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        params.put("dataFileName","CalculateDebtCapacityData.xml");
        mockTestCycle(dir, "AMRLiRT", "CalculateDebtCapacity", "Response", params);
    }
    @Test
    public void testXSLTtoDataAMRLiRTCalculateRating() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","CalcRatingResponse");
        params.put("RqEntryPointName","CalcRatingRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        params.put("dataFileName","CalculateRatingData.xml");
        mockTestCycle(dir, "AMRLiRT", "CalculateRating", "Response", params);
    }
    @Test
    public void testXSLTtoDataAMRLiRTConfirmRating() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","ConfirmResponse");
        params.put("RqEntryPointName","ConfirmRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        params.put("dataFileName","ConfirmRatingData.xml");
        mockTestCycle(dir, "AMRLiRT", "ConfirmRating", "Response", params);
    }
    @Test
    public void testXSLTtoDataAMRLiRTCorrectRating() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","CorrectResponse");
        params.put("RqEntryPointName","CorrectRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        params.put("dataFileName","CorrectRatingData.xml");
        mockTestCycle(dir, "AMRLiRT", "CorrectRating", "Response", params);
    }
    @Test
    public void testXSLTtoDataAMRLiRTFinalizeLGD() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","LgdFinalizationResponse");
        params.put("RqEntryPointName","LgdFinalizationRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        params.put("dataFileName","FinalizeLGDData.xml");
        mockTestCycle(dir, "AMRLiRT", "FinalizeLGD", "Response", params);
    }
    @Test
    public void testXSLTtoDataAMRLiRTCalculateLGD() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","LgdCalculationResponse");
        params.put("RqEntryPointName","LgdCalculationRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        params.put("dataFileName","CalculateLGDData.xml");
        mockTestCycle(dir, "AMRLiRT", "CalculateLGD", "Response", params);
    }
    @Test
    public void testXSLTtoDataFinRepFinAnalysisImport() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","FinAnalysisImportResponse");
        params.put("RqEntryPointName","FinAnalysisImportRequest");
        params.put("systemName","FinRep");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        mockTestCycle(dir, "FinRep", "FinAnalysisImport", "Response", params);
    }
    @Test
    public void testXSLTtoDataFinRepFinReportImport() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","FinReportImportResponse");
        params.put("RqEntryPointName","FinReportImportRequest");
        params.put("systemName","FinRep");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        mockTestCycle(dir, "FinRep", "FinReportImport", "Response", params);
    }
    @Test
    public void testXSLTtoDataFinRepImportRating() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","ImportRatingResponse");
        params.put("RqEntryPointName","ImportRatingRequest");
        params.put("systemName","FinRep");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        mockTestCycle(dir, "FinRep", "ImportRating", "Response", params);
    }
    @Test
    public void testXSLTtoDataFinRepFinRepUpdateRating() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","UpdateRatingResponse");
        params.put("RqEntryPointName","UpdateRatingRequest");
        params.put("systemName","FinRep");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        mockTestCycle(dir, "FinRep", "UpdateRating", "Response", params);
    }


    protected void mockTestCycle(String webinf, String system, String name, String type) throws Exception {
        mockTestCycle(webinf, system, name, type, null);
    }
    protected void mockTestCycle(String webinf, String system, String name, String type, Map<String,String> params) throws Exception
    {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();

        System.out.println(system +" "+ name+type);


        System.out.println("xsd");
        String dataXsd = generateDataXSD(webinf,
                "\\xsd\\"+system+"\\"+name+type+".xsd",
                "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\"+system+"\\xsd\\"+name+type+"Data.xsd", params);

        assert dataXsd.contains("<xsd:element name=\"SoapHeader\" type=\"Header\" minOccurs=\"0\"/>")
                : "Data xsd не содержит строку с заголовком";
        assert dataXsd.contains("<xsd:attribute name=\"name\"/>")
                : "Data xsd не содержит строку c атрибутом имени";

        if (type.equals("Response")) {
            System.out.println("xsl");
            String xsl = checkXSLT(webinf + "\\xsl\\util\\responceXSDtoXSL.xsl",
                    webinf + "\\xsd\\" + system + "\\" + name + type + ".xsd",
                    "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\" + system + "\\" + name + ".xsl", params);

            Map<String, String> altParams = null;
            if (params!=null) {
                altParams = new HashMap<String, String>(params);

                if (params.containsKey("entryPointName")) {
                    altParams.put("operation-name", params.get("entryPointName"));
                }
                altParams.put("entryPointName", params.get("RqEntryPointName"));
            } else {
                altParams = new HashMap<String, String>(1);
            }
            altParams.put("omitComments", "true");
            //altParams.put("operation-name", name+"Response");

            System.out.println("create rq example 1");
            String exampleRq1 = checkXSLT(webinf + "\\xsl\\util\\XSDToExampleXML.xsl",
                    webinf + "\\xsd\\" + system + "\\" + name + "Request.xsd",
                    "\\..\\..\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq1.xml", altParams);

            assert !exampleRq1.contains("<!--not known type-->")
                    : "В примере xml заполены известны не все типы";

            System.out.println("create rs example 1");
            String exampleRs1 = checkXSLT(webinf + "\\xsl\\util\\XSDToExampleXML.xsl",
                    webinf + "\\xsd\\" + system + "\\" + name + type + ".xsd",
                    "\\..\\..\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rs1.xml", params);

            assert !exampleRs1.contains("<!--not known type-->")
                    : "В примере xml заполены известны не все типы";

            System.out.println("check example 1");
            checkXSLT(webinf + "\\xsl\\" + system + "\\" + name + ".xsl",
                    dir + "\\..\\..\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq1.xml",
                    "\\..\\..\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rs1.xml");

//            System.out.println("check example ");
//            checkXSLT(webinf + "\\xsl\\" + system + "\\" + name + ".xsl",
//                    dir + "\\..\\..\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq2.xml",
//                    "\\..\\..\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rs2.xml");
        } else if (type.equals("Request")) {
            System.out.println("xsl");
            String xsl = checkXSLT(webinf + "\\xsl\\util\\requestXSDtoXSL.xsl",
                    webinf + "\\xsd\\" + system + "\\" + name + type + ".xsd",
                    "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\" + system + "\\" + name + ".xsl", params);

            System.out.println("example 1");
            checkXSLT(webinf + "\\xsl\\" + system + "\\" + name + ".xsl",
                    webinf + "\\data\\" + system + "\\xml\\" + name + "Data.xml",
                    "\\..\\..\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq1.xml", params);
        } else {
            assert false;
        }
    }

    protected String generateDataXSD(String WebInfPath, String inputXSDFilePath, String checkFile, Map<String,String> params ) throws Exception
    {
        String result = checkXSLT(WebInfPath + "\\xsl\\util\\xsdToDataXsd.xsl",
                WebInfPath + inputXSDFilePath,
                checkFile, params);
        return result;
    }


    protected String checkXSLT (String XSLTFile, String XMLFile, String validateFile ) throws Exception {
        return checkXSLT (XSLTFile, XMLFile, validateFile, null);
    }


    protected String checkXSLT (String XSLTFile, String XMLFile, String validateFile, Map<String,String> params ) throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
//        System.out.println(dir);
        String result = Xsl20Transformer.transform(XSLTFile, XMLFile, params);
        String validateFileXML = FileUtils.readFileToString(new File(dir + validateFile));

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        try {
            Diff diff = new Diff(validateFileXML, result);
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
        catch (Exception e) {
            System.out.println(result);
            e.printStackTrace();
            assertEquals(validateFileXML, result);
        }
        return result;
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
