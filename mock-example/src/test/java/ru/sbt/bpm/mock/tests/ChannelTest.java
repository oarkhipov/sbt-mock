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
@ContextConfiguration({"/mockapp-servlet-test.xml"})
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
    public void createTaskTest1() throws Exception {
        testXSLT("CreateTask", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/CreateTask/rq1.xml", "xml/CRM/CreateTask/rs1.xml");
    }

    @Test
    public void createTaskTest2() throws Exception {
        testXSLT("CreateTask", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/CreateTask/rq2.xml", "xml/CRM/CreateTask/rs2.xml");
    }

    @Test
    public void getParticipantsTest1() throws Exception {
        testXSLT("GetParticipants", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/GetParticipants/rq1.xml", "xml/CRM/GetParticipants/rs1.xml");
    }

    @Test
    public void getParticipantsTest2() throws Exception {
        testXSLT("GetParticipants", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/GetParticipants/rq2.xml", "xml/CRM/GetParticipants/rs2.xml");
    }

    @Test
    public void saveDealTest1() throws Exception {
        testXSLT("SaveDeal", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/SaveDeal/rq1.xml", "xml/CRM/SaveDeal/rs1.xml");
    }

    @Test
    public void saveDealTest2() throws Exception {
        testXSLT("SaveDeal", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/SaveDeal/rq2.xml", "xml/CRM/SaveDeal/rs2.xml");
    }

    @Test
    public void updateRefTest1() throws Exception {
        testXSLT("UpdateRef", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/UpdateRef/rq1.xml", "xml/CRM/UpdateRef/rs1.xml");
    }

    @Test
    public void updateRefTest2() throws Exception {
        testXSLT("UpdateRef", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/UpdateRef/rq2.xml", "xml/CRM/UpdateRef/rs2.xml");
    }



//  AMRLiRT testing
    @Test
    public void createTaskTestAMRLiRT_SrvCalcDebtCapacity() throws Exception {
        testXSLT("SrvCalcDebtCapacity", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateDebtCapacity/rq1.xml", "xml/AMRLiRT/CalculateDebtCapacity/rs1.xml");
    }
    @Test
    public void createTaskTestAMRLiRT_SrvCalcDebtCapacity2() throws Exception {
        testXSLT("SrvCalcDebtCapacity", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateDebtCapacity/rq2.xml", "xml/AMRLiRT/CalculateDebtCapacity/rs2.xml");
    }

    @Test
    public void createTaskTestAMRLiRT_SrvCalcLGD() throws Exception {
        testXSLT("SrvCalcLGD", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateLGD/rq1.xml", "xml/AMRLiRT/CalculateLGD/rs1.xml");
    }

    @Test
    public void createTaskTestAMRLiRT_SrvCalcLGD2() throws Exception {
        testXSLT("SrvCalcLGD", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateLGD/rq2.xml", "xml/AMRLiRT/CalculateLGD/rs2.xml");
    }


    @Test
    public void createTaskTestAMRLiRT_SrvCalcRating() throws Exception {
        testXSLT("SrvCalcRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateRating/rq1.xml", "xml/AMRLiRT/CalculateRating/rs1.xml");
    }


    @Test
    public void createTaskTestAMRLiRT_SrvCalcRating2() throws Exception {
        testXSLT("SrvCalcRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateRating/rq2.xml", "xml/AMRLiRT/CalculateRating/rs2.xml");
    }

    @Test
    public void createTaskTestAMRLiRT_SrvConfirmRating() throws Exception {
        testXSLT("SrvConfirmRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/ConfirmRating/rq1.xml", "xml/AMRLiRT/ConfirmRating/rs1.xml");
    }

    @Test
    public void createTaskTestAMRLiRT_SrvConfirmRating2() throws Exception {
        testXSLT("SrvConfirmRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/ConfirmRating/rq2.xml", "xml/AMRLiRT/ConfirmRating/rs2.xml");
    }

    @Test
    public void createTaskTestAMRLiRT_SrvFinalizeLGD() throws Exception {
        testXSLT("SrvFinalizeLGD", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/FinalizeLGD/rq1.xml", "xml/AMRLiRT/FinalizeLGD/rs1.xml");
    }
    @Test
    public void createTaskTestAMRLiRT_SrvFinalizeLGD2() throws Exception {
        testXSLT("SrvFinalizeLGD", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/FinalizeLGD/rq2.xml", "xml/AMRLiRT/FinalizeLGD/rs2.xml");
    }

    @Test
    public void createTaskTestAMRLiRT_CorrectRating() throws Exception {
        testXSLT("SrvUpdateRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CorrectRating/rq1.xml", "xml/AMRLiRT/CorrectRating/rs1.xml");
    }
    @Test
    public void createTaskTestAMRLiRT_CorrectRating2() throws Exception {
        testXSLT("SrvUpdateRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CorrectRating/rq2.xml", "xml/AMRLiRT/CorrectRating/rs2.xml");
    }


    @Test
    public void createTaskTestFinRep_SrvGetFinReport() throws Exception {
        testXSLT("SrvGetFinReport", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/SrvGetFinReport/rq1.xml", "xml/FinRep/SrvGetFinReport/rs1.xml");
    }
    @Test
    public void createTaskTestFinRep_SrvGetFinAnalysis() throws Exception {
        testXSLT("SrvGetFinAnalysis", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/SrvGetFinAnalysis/rq1.xml", "xml/FinRep/SrvGetFinAnalysis/rs1.xml");
    }
    @Test
    public void createTaskTestFinRep_SrvGetRatingsAndFactors() throws Exception {
        testXSLT("SrvGetRatingsAndFactors", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/SrvGetRatingsAndFactors/rq1.xml", "xml/FinRep/SrvGetRatingsAndFactors/rs1.xml");
    }
    @Test
    public void createTaskTestFinRep_SrvUpdateRating() throws Exception {
        testXSLT("SrvUpdateRating", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/SrvUpdateRating/rq1.xml", "xml/FinRep/SrvUpdateRating/rs1.xml");
    }

    @Test
    public void createTaskTestFinRep_SrvGetFinReport2() throws Exception {
        testXSLT("SrvGetFinReport", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/SrvGetFinReport/rq2.xml", "xml/FinRep/SrvGetFinReport/rs2.xml");
    }
    @Test
    public void createTaskTestFinRep_SrvGetFinAnalysis2() throws Exception {
        testXSLT("SrvGetFinAnalysis", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/SrvGetFinAnalysis/rq2.xml", "xml/FinRep/SrvGetFinAnalysis/rs2.xml");
    }
    @Test
    public void createTaskTestFinRep_SrvGetRatingsAndFactors2() throws Exception {
        testXSLT("SrvGetRatingsAndFactors", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/SrvGetRatingsAndFactors/rq2.xml", "xml/FinRep/SrvGetRatingsAndFactors/rs2.xml");
    }
    @Test
    public void createTaskTestFinRep_SrvUpdateRating2() throws Exception {
        testXSLT("SrvUpdateRating", "ESB.BPM.NCP.OUT.MOCK", "xml/FinRep/SrvUpdateRating/rq2.xml", "xml/FinRep/SrvUpdateRating/rs2.xml");
    }


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
