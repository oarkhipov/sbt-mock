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


    /*@Test
    public void testXSLTtoDataCRMCreateTask() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","CreateTaskRs");
        params.put("RqEntryPointName","CreateTaskRq");
        params.put("tagNameToTakeLinkedTag","comment");
        params.put("rootElementName", "createTaskRs");
        params.put("RqRootElementName", "createTaskRq");
        params.put("xsdBase","CRM.xsd");
        mockTestCycle(dir, "CRM", "CreateTask", "Response", params);
    }
    @Test
    public void testXSLTtoDataCRMForceSignal() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("rootElementName", "forceSignalRq");
        params.put("xsdBase","CRM.xsd");
        mockTestCycle(dir, "CRM", "ForceSignal", "Request", params);
    }
    @Test
    public void testXSLTtoDataCRMGetParticipants() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","PrtspRs");
        params.put("RqEntryPointName","PrtspRq");
        params.put("dataFileName","GetParticipantsData.xml");
        params.put("tagNameToTakeLinkedTag","performer");
        params.put("rootElementName", "prtspRs");
        params.put("RqRootElementName", "prtspRq");
        params.put("xsdBase","CRM.xsd");
        mockTestCycle(dir, "CRM", "GetParticipants", "Response", params);
    }
    @Test
    public void testXSLTtoDataCRMSaveDeal() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","SaveDealRs");
        params.put("RqEntryPointName","SaveDealRq");
        params.put("tagNameToTakeLinkedTag","dealType");
        params.put("rootElementName", "saveDealRs");
        params.put("RqRootElementName", "saveDealRq");
        params.put("xsdBase","CRM.xsd");
        mockTestCycle(dir, "CRM", "SaveDeal", "Response", params);
    }
    @Test
    public void testXSLTtoDataCRMUpdateDeal() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("rootElementName", "updateDealRq");
        params.put("xsdBase","CRM.xsd");
        mockTestCycle(dir, "CRM", "UpdateDeal", "Request",params);
    }
    @Test
    public void testXSLTtoDataCRMUpdateRef() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","UpdateRefRs");
        params.put("RqEntryPointName","UpdateRefRq");
        params.put("tagNameToTakeLinkedTag","referenceItem");
        params.put("rootElementName", "updateRefRs");
        params.put("RqRootElementName", "updateRefRq");
        params.put("xsdBase","CRM.xsd");
        mockTestCycle(dir, "CRM", "UpdateRef", "Response", params);
    }
    @Test
    public void testXSLTtoDataAMRLiRTCalculateDebtCapacity() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","DebtCapacityCalculationResponse");
        params.put("RqEntryPointName","DebtCapacityCalculationRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentXSDPath","../../xsd/AMRLiRT/AMRLIRT.xsd");
        params.put("dataFileName","CalculateDebtCapacityData.xml");
        params.put("tagNameToTakeLinkedTag","model");
        params.put("rootElementName", "calculateDCRs");
        params.put("RqRootElementName", "calculateDCRq");
        params.put("xsdBase","AMRLiRT.xsd");
        mockTestCycle(dir, "AMRLiRT", "CalculateDebtCapacity", "Response", params);
    }
    @Test
    public void testXSLTtoDataAMRLiRTCalculateRating() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","CalcRatingResponse");
        params.put("RqEntryPointName","CalcRatingRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentXSDPath","../../xsd/AMRLiRT/AMRLIRT.xsd");
        params.put("dataFileName","CalculateRatingData.xml");
        params.put("tagNameToTakeLinkedTag","model");
        params.put("rootElementName", "calculateRatingRs");
        params.put("RqRootElementName", "calculateRatingRq");
        params.put("xsdBase","AMRLiRT.xsd");
        mockTestCycle(dir, "AMRLiRT", "CalculateRating", "Response", params);
    }
    @Test
    public void testXSLTtoDataAMRLiRTConfirmRating() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","ConfirmResponse");
        params.put("RqEntryPointName","ConfirmRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentXSDPath","../../xsd/AMRLiRT/AMRLIRT.xsd");
        params.put("dataFileName","ConfirmRatingData.xml");
        params.put("tagNameToTakeLinkedTag","siebelMessage");
        params.put("rootElementName", "confirmRatingRs");
        params.put("RqRootElementName", "confirmRatingRq");
        params.put("xsdBase","AMRLiRT.xsd");
        mockTestCycle(dir, "AMRLiRT", "ConfirmRating", "Response", params);
    }
    @Test
    public void testXSLTtoDataAMRLiRTCorrectRating() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","CorrectResponse");
        params.put("RqEntryPointName","CorrectRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentXSDPath","../../xsd/AMRLiRT/AMRLIRT.xsd");
        params.put("dataFileName","CorrectRatingData.xml");
        params.put("tagNameToTakeLinkedTag","siebelMessage");
        params.put("rootElementName", "correctRatingRs");
        params.put("RqRootElementName", "correctRatingRq");
        params.put("xsdBase","AMRLiRT.xsd");
        mockTestCycle(dir, "AMRLiRT", "CorrectRating", "Response", params);
    }
    @Test
    public void testXSLTtoDataAMRLiRTFinalizeLGD() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","LgdFinalizationResponse");
        params.put("RqEntryPointName","LgdFinalizationRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentXSDPath","../../xsd/AMRLiRT/AMRLIRT.xsd");
        params.put("dataFileName","FinalizeLGDData.xml");
        params.put("tagNameToTakeLinkedTag","type");
        params.put("rootElementName", "finalizeLGDRs");
        params.put("RqRootElementName", "finalizeLGDRq");
        params.put("xsdBase","AMRLiRT.xsd");
        mockTestCycle(dir, "AMRLiRT", "FinalizeLGD", "Response", params);
    }
    @Test
    public void testXSLTtoDataAMRLiRTCalculateLGD() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","LgdCalculationResponse");
        params.put("RqEntryPointName","LgdCalculationRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentXSDPath","../../xsd/AMRLiRT/AMRLIRT.xsd");
        params.put("dataFileName","CalculateLGDData.xml");
        params.put("tagNameToTakeLinkedTag","comment");
        params.put("rootElementName", "calculateLGDRs");
        params.put("RqRootElementName", "calculateLGDRq");
        params.put("xsdBase","AMRLiRT.xsd");
        mockTestCycle(dir, "AMRLiRT", "CalculateLGD", "Response", params);
    }
    @Test
    public void testXSLTtoDataFinRepFinAnalysisImport() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","FinAnalysisImportResponse");
        params.put("RqEntryPointName","FinAnalysisImportRequest");
        params.put("systemName","FinRep");
        params.put("parrentXSDPath","../../xsd/FinRep/ASFO.xsd");
        params.put("tagNameToTakeLinkedTag","dealId");
        params.put("rootElementName", "getFinAnalysisRs");
        params.put("RqRootElementName", "getFinAnalysisRq");
        params.put("xsdBase","ASFO.xsd");
        mockTestCycle(dir, "FinRep", "FinAnalysisImport", "Response", params);
    }
    @Test
    public void testXSLTtoDataFinRepFinReportImport() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","FinReportImportResponse");
        params.put("RqEntryPointName","FinReportImportRequest");
        params.put("systemName","FinRep");
        params.put("parrentXSDPath","../../xsd/FinRep/ASFO.xsd");
        params.put("tagNameToTakeLinkedTag","finReportType");
        params.put("rootElementName", "getFinReportRs");
        params.put("RqRootElementName", "getFinReportRq");
        params.put("xsdBase","ASFO.xsd");
        mockTestCycle(dir, "FinRep", "FinReportImport", "Response", params);
    }
    @Test
    public void testXSLTtoDataFinRepImportRating() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","ImportRatingResponse");
        params.put("RqEntryPointName","ImportRatingRequest");
        params.put("systemName","FinRep");
        params.put("parrentXSDPath","../../xsd/FinRep/ASFO.xsd");
        params.put("tagNameToTakeLinkedTag","entityType");
        params.put("rootElementName", "getRatingsAndFactorsRs");
        params.put("RqRootElementName", "getRatingsAndFactorsRq");
        params.put("xsdBase","ASFO.xsd");
        mockTestCycle(dir, "FinRep", "ImportRating", "Response", params);
    }
    @Test
    public void testXSLTtoDataFinRepFinRepUpdateRating() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entryPointName","UpdateRatingResponse");
        params.put("RqEntryPointName","UpdateRatingRequest");
        params.put("systemName","FinRep");
        params.put("parrentXSDPath","../../xsd/FinRep/ASFO.xsd");
        params.put("tagNameToTakeLinkedTag","status");
        params.put("rootElementName", "updateRatingRs");
        params.put("RqRootElementName", "updateRatingRq");
        params.put("xsdBase","ASFO.xsd");
        mockTestCycle(dir, "FinRep", "UpdateRating", "Response", params);
    }
    @Test
    public void testXSLTtoDataCBBOLSrvPutRemoteLegalAccOperAppRq() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("rootElementName", "SrvPutRemoteLegalAccOperAppRq");
        params.put("operationsXSD", "../../xsd/CBBOL/BBMOOperationElements.xsd");
        params.put("systemName","CBBOL");
        params.put("xsdBase","BBMOOperationElements.xsd");
        params.put("headerType", "KD4");
        mockTestCycle(dir, "CBBOL", "SrvPutRemoteLegalAccOperAppRq", "Request", params);
    }
    @Test
    public void testXSLTtoDataBBMOSrvUpdateClientReferenceDataRq() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("rootElementName", "SrvUpdateClientReferenceDataRq");
        params.put("operationsXSD", "../../xsd/BBMO/BBMOOperationElements.xsd");
        params.put("systemName","BBMO");
        params.put("xsdBase","BBMOOperationElements.xsd");
        params.put("headerType", "KD4");
        mockTestCycle(dir, "BBMO", "SrvUpdateClientReferenceDataRq", "Request", params);
    }
    @Test
    public void testXSLTtoDataCKPITProductsDepositsNSOReq() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("rootElementName", "CKPITProductsDepositsNSOReq");
        params.put("operationsXSD", "../../xsd/CKPIT/ckpit_integration.xsd");
        params.put("systemName","CKPIT");
        params.put("xsdBase","ckpit_integration.xsd");
        params.put("headerType", "KD4");
        mockTestCycle(dir, "CKPIT", "CKPITProductsDepositsNSOReq", "Request", params);
    }
    @Test
    public void testXSLTtoDataCKPITProductsLoansReq() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("rootElementName", "CKPITProductsLoansReq");
        params.put("operationsXSD", "../../xsd/CKPIT/ckpit_integration.xsd");
        params.put("systemName","CKPIT");
        params.put("xsdBase","ckpit_integration.xsd");
        params.put("headerType", "KD4");
        mockTestCycle(dir, "CKPIT", "CKPITProductsLoansReq", "Request", params);
<<<<<<< HEAD
    }*/
    @Test
    public void testXSLTtoDataAMRLiRTcalculationRequest() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("systemName","AMRLiRT");
        params.put("tagNameToTakeLinkedTag","ProductType");
        params.put("rootElementName", "calculationResponse");
        params.put("RqRootElementName", "calculationRequest");
        params.put("xsdBase","LGDServiceSchema1.xsd");
        params.put("operationsXSD", "../../xsd/AMRLiRT/LGDServiceSchema1.xsd");
        params.put("headerType", "KD4");
        mockTestCycle(dir, "AMRLiRT", "calculation", "Response", params);
    }
    @Test
    public void testXSLTtoDataAMRLiRTfinalizationRequest() throws Exception {
        final String dir = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF";
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("systemName","AMRLiRT");
        params.put("tagNameToTakeLinkedTag","Type");
        params.put("rootElementName", "finalizationResponse");
        params.put("RqRootElementName", "finalizationRequest");
        params.put("xsdBase","LGDServiceSchema1.xsd");
        params.put("operationsXSD", "../../xsd/AMRLiRT/LGDServiceSchema1.xsd");
        params.put("headerType", "KD4");
        mockTestCycle(dir, "AMRLiRT", "finalization", "Response", params);
    }



    protected void mockTestCycle(String webinf, String system, String name, String type) throws Exception {
        mockTestCycle(webinf, system, name, type, null);
    }
    protected void mockTestCycle(String webinf, String system, String name, String type, Map<String,String> params) throws Exception
    {
        final String dir = System.getProperty("user.dir");

        if (type.equals("Response")) {

            if (params==null) {
                params = new HashMap<String, String>(1);
            }
            if (!params.containsKey("operationsXSD")) {
                params.put("operationsXSD", "../../xsd/"+system+"/"+name+"Response.xsd");
            }
            Map <String, String> altParams = new HashMap<String, String>(params);
            altParams.put("rootElementName", params.get("RqRootElementName"));
            altParams.put("operationsXSD", "../../xsd/"+system+"/"+name+"Request.xsd");
            altParams.put("operation-name", params.get("rootElementName"));

            System.out.println("xsd");
            String dataXsd = generateDataXSD(webinf,
                    "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                    "\\src\\main\\webapp\\WEB-INF\\data\\"+system+"\\xsd\\"+name+type+"Data.xsd", params);

            assert dataXsd.contains("<xsd:element name=\"SoapHeader\" type=\"Header\" minOccurs=\"0\"/>")
                    : "Data xsd не содержит строку с заголовком";
            assert dataXsd.contains("<xsd:attribute name=\"name\"/>")
                    : "Data xsd не содержит строку c атрибутом имени";

            System.out.println("xsl");
            String xsl = checkXSLT(webinf + "\\xsl\\util\\responceXSDtoXSL.xsl",
                    webinf + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
//                    "\\src\\main\\webapp\\WEB-INF\\xsl\\" + system + "\\" + name + ".xsl", params);
                    null, params);

            System.out.println("create rq example 1");
            String exampleRq1 = checkXSLT(webinf + "\\xsl\\util\\NCPSoapMSG.xsl",
                    webinf + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                    null, altParams);
//                    "\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq1.xml", altParams);

            if (params.containsKey("tagNameToTakeLinkedTag")) {
                altParams.put("useLinkedTagValue","true");
                altParams.put("tagNameToTakeLinkedTag", params.get("tagNameToTakeLinkedTag"));
            }

            System.out.println("create rq example 2");
            altParams.put("showOptionalTags", "false");
            String exampleRq2 = checkXSLT(webinf + "\\xsl\\util\\NCPSoapMSG.xsl",
                    webinf + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                    null, altParams);
//                    "\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq2.xml", altParams);

            assert !exampleRq1.contains("<!--not known type-->")
                    : "В примере xml заполены известны не все типы";

            System.out.println("create rs example 1");
            String exampleRs1 = checkXSLT(webinf + "\\xsl\\util\\NCPSoapMSG.xsl",
                    webinf + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                    "\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rs1.xml", params);


            System.out.println("create rs example 2");
            params.put("showOptionalTags","false");
            String exampleRs2 = checkXSLT(webinf + "\\xsl\\util\\NCPSoapMSG.xsl",
                    webinf + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                    "\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rs2.xml", params);

            assert !exampleRs1.contains("<!--not known type-->")
                    : "В примере xml заполены известны не все типы";

            System.out.println("check example 1");
            checkXSLT(webinf + "\\xsl\\" + system + "\\" + name + ".xsl",
                    dir + "\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq1.xml",
                    "\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rs1.xml");

            System.out.println("check example 2");
            checkXSLT(webinf + "\\xsl\\" + system + "\\" + name + ".xsl",
                    dir + "\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq2.xml",
                    "\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rs2.xml");
        } else if (type.equals("Request")) {
            if (params==null) {
                params = new HashMap<String, String>(1);
            }
            if (!params.containsKey("operationsXSD")) {
                params.put("operationsXSD", "../../xsd/"+system+"/"+name+"Request.xsd");
            }

            System.out.println("xsd");
            String dataXsd = generateDataXSD(webinf,
                    "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                    "\\src\\main\\webapp\\WEB-INF\\data\\"+system+"\\xsd\\"+name+type+"Data.xsd", params);

            System.out.println("xsl");
            String xsl = checkXSLT(webinf + "\\xsl\\util\\requestXSDtoXSL.xsl",
                    webinf + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                    "\\src\\main\\webapp\\WEB-INF\\xsl\\" + system + "\\" + name + ".xsl", params);
//                    null, params);

            System.out.println("example 1");
            checkXSLT(webinf + "\\xsl\\" + system + "\\" + name + ".xsl",
                    webinf + "\\data\\" + system + "\\xml\\" + name + "Data.xml",
                    "\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq1.xml", params);

            params.put("name","test1");
            System.out.println("example 2");
            checkXSLT(webinf + "\\xsl\\" + system + "\\" + name + ".xsl",
                    webinf + "\\data\\" + system + "\\xml\\" + name + "Data.xml",
                    "\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq2.xml", params);
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

        final String dir = System.getProperty("user.dir");
//        System.out.println(dir);
        String result = Xsl20Transformer.transform(XSLTFile, XMLFile, params);
        if (validateFile!=null) {
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
            } catch (Exception e) {
                System.out.println(result);
                e.printStackTrace();
                assertEquals(validateFileXML, result);
            }
        }
        return result;
    }

    protected void checkXMLDataRowToXMLDataListByString (String XSLTFile, String XMLFile, String validateString ) throws Exception {

        final String dir = System.getProperty("user.dir");
        System.out.println(dir);
        String result = Xsl20Transformer.transform(XSLTFile, XMLFile);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        assertEquals(validateString, result);
    }

}
