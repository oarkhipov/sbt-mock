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

import java.util.List;

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
    public void testUpdateContractInfo1() throws Exception {
        testXSLT("UpdateContractInfo", "Q.KKMB.FROM.CRMORG.SYNCRESP", "xml/CRM/UpdateContractInfo/rq1.xml", "xml/CRM/UpdateContractInfo/rs1.xml");
    }

    @Test
    public void testUpdateContractInfo2() throws Exception {
        testXSLT("UpdateContractInfo", "Q.KKMB.FROM.CRMORG.SYNCRESP", "xml/CRM/UpdateContractInfo/rq2.xml", "xml/CRM/UpdateContractInfo/rs2.xml");
    }

//    @Test
//    public void createTaskTest1() throws Exception {
//        testXSLT("CreateTask", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/CreateTask/rs1.xml", "xml/CRM/CreateTask/rs1.xml");
//    }
//
//    @Test
//    public void createTaskTest2() throws Exception {
//        testXSLT("CreateTask", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/CreateTask/rq2.xml", "xml/CRM/CreateTask/rs2.xml");
//    }
//
//    @Test
//    public void getParticipantsTest1() throws Exception {
//        testXSLT("GetParticipants", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/GetParticipants/rs1.xml", "xml/CRM/GetParticipants/rs1.xml");
//    }
//
//    @Test
//    public void getParticipantsTest2() throws Exception {
//        testXSLT("GetParticipants", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/GetParticipants/rq2.xml", "xml/CRM/GetParticipants/rs2.xml");
//    }
//
//    @Test
//    public void saveDealTest1() throws Exception {
//        testXSLT("SaveDeal", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/SaveDeal/rs1.xml", "xml/CRM/SaveDeal/rs1.xml");
//    }
//
//    @Test
//    public void saveDealTest2() throws Exception {
//        testXSLT("SaveDeal", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/SaveDeal/rq2.xml", "xml/CRM/SaveDeal/rs2.xml");
//    }
//
//    @Test
//    public void updateRefTest1() throws Exception {
//        testXSLT("UpdateRef", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/UpdateRef/rs1.xml", "xml/CRM/UpdateRef/rs1.xml");
//    }
//
//    @Test
//    public void updateRefTest2() throws Exception {
//        testXSLT("UpdateRef", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/UpdateRef/rq2.xml", "xml/CRM/UpdateRef/rs2.xml");
//    }
//
//
//
////  AMRLiRT testing
//    @Test
//    public void createTaskTestAMRLiRT_CalculateDebtCapacity() throws Exception {
//        testXSLT("CalculateDebtCapacity", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateDebtCapacity/rq1.xml", "xml/AMRLiRT/CalculateDebtCapacity/rs1.xml");
//    }
//    @Test
//    public void createTaskTestAMRLiRT_CalculateDebtCapacity2() throws Exception {
//        testXSLT("CalculateDebtCapacity", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateDebtCapacity/rq2.xml", "xml/AMRLiRT/CalculateDebtCapacity/rs2.xml");
//    }
//
//    @Test
//    public void createTaskTestAMRLiRT_CalculateLGD() throws Exception {
//        testXSLT("CalculateLGD", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateLGD/rq1.xml", "xml/AMRLiRT/CalculateLGD/rs1.xml");
//    }
//
//    @Test
//    public void createTaskTestAMRLiRT_CalculateLGD2() throws Exception {
//        testXSLT("CalculateLGD", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateLGD/rq2.xml", "xml/AMRLiRT/CalculateLGD/rs2.xml");
//    }
//
//
//    @Test
//    public void createTaskTestAMRLiRT_CalculateRating() throws Exception {
//        testXSLT("CalculateRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateRating/rq1.xml", "xml/AMRLiRT/CalculateRating/rs1.xml");
//    }
//
//
//    @Test
//    public void createTaskTestAMRLiRT_CalculateRating2() throws Exception {
//        testXSLT("CalculateRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateRating/rq2.xml", "xml/AMRLiRT/CalculateRating/rs2.xml");
//    }
//
//    @Test
//    public void createTaskTestAMRLiRT_ConfirmRating() throws Exception {
//        testXSLT("ConfirmRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/ConfirmRating/rq1.xml", "xml/AMRLiRT/ConfirmRating/rs1.xml");
//    }
//
//    @Test
//    public void createTaskTestAMRLiRT_ConfirmRating2() throws Exception {
//        testXSLT("ConfirmRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/ConfirmRating/rq2.xml", "xml/AMRLiRT/ConfirmRating/rs2.xml");
//    }
//
//    @Test
//    public void createTaskTestAMRLiRT_FinalizeLGD() throws Exception {
//        testXSLT("FinalizeLGD", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/FinalizeLGD/rq1.xml", "xml/AMRLiRT/FinalizeLGD/rs1.xml");
//    }
//    @Test
//    public void createTaskTestAMRLiRT_FinalizeLGD2() throws Exception {
//        testXSLT("FinalizeLGD", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/FinalizeLGD/rq2.xml", "xml/AMRLiRT/FinalizeLGD/rs2.xml");
//    }
//
//    @Test
//    public void createTaskTestAMRLiRT_CorrectRating() throws Exception {
//        testXSLT("CorrectRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CorrectRating/rq1.xml", "xml/AMRLiRT/CorrectRating/rs1.xml");
//    }
//    @Test
//    public void createTaskTestAMRLiRT_CorrectRating2() throws Exception {
//        testXSLT("CorrectRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CorrectRating/rq2.xml", "xml/AMRLiRT/CorrectRating/rs2.xml");
//    }
//
//
//    @Test
//    public void createTaskTestFinRep_GetFinReport() throws Exception {
//        testXSLT("FinReportImport", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/FinReportImport/rq1.xml", "xml/FinRep/FinReportImport/rs1.xml");
//    }
//    @Test
//    public void createTaskTestFinRep_GetFinAnalysis() throws Exception {
//        testXSLT("FinAnalysisImport", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/FinAnalysisImport/rq1.xml", "xml/FinRep/FinAnalysisImport/rs1.xml");
//    }
//    @Test
//    public void createTaskTestFinRep_GetRatingsAndFactors() throws Exception {
//        testXSLT("ImportRating", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/ImportRating/rq1.xml", "xml/FinRep/ImportRating/rs1.xml");
//    }
//    @Test
//    public void createTaskTestFinRep_UpdateRating() throws Exception {
//        testXSLT("UpdateRating", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/UpdateRating/rq1.xml", "xml/FinRep/UpdateRating/rs1.xml");
//    }
//
//    @Test
//    public void createTaskTestFinRep_GetFinReport2() throws Exception {
//        testXSLT("FinReportImport", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/FinReportImport/rq2.xml", "xml/FinRep/FinReportImport/rs2.xml");
//    }
//    @Test
//    public void createTaskTestFinRep_GetFinAnalysis2() throws Exception {
//        testXSLT("FinAnalysisImport", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/FinAnalysisImport/rq2.xml", "xml/FinRep/FinAnalysisImport/rs2.xml");
//    }
//    @Test
//    public void createTaskTestFinRep_GetRatingsAndFactors2() throws Exception {
//        testXSLT("ImportRating", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/ImportRating/rq2.xml", "xml/FinRep/ImportRating/rs2.xml");
//    }
//    @Test
//    public void createTaskTestFinRep_UpdateRating2() throws Exception {
//        testXSLT("UpdateRating", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/UpdateRating/rq2.xml", "xml/FinRep/UpdateRating/rs2.xml");
//    }

    private void testXSLT(String INStream, String OUTStream, String request, String responce) throws Exception {
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

}
