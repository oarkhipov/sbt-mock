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
    public void createTaskTest() throws Exception {
        testXSLT("CreateTask", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/CreateTask/rq1.xml", "xml/CRM/CreateTask/rs1.xml");
    }

    @Test
    public void createTaskTest2() throws Exception {
        testXSLT("CreateTask", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/CreateTask/rq2.xml", "xml/CRM/CreateTask/rs2.xml");
    }

    @Test
    public void getParticipants() throws Exception {
        testXSLT("GetParticipants", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/GetParticipants/rq1.xml", "xml/CRM/GetParticipants/rs1.xml");
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
    public void createTaskTestAMRLiRT_SrvUpdateRating() throws Exception {
        testXSLT("SrvUpdateRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CorrectRating/rq1.xml", "xml/AMRLiRT/CorrectRating/rs1.xml");
    }
    @Test
    public void createTaskTestAMRLiRT_SrvUpdateRating2() throws Exception {
        testXSLT("SrvUpdateRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CorrectRating/rq2.xml", "xml/AMRLiRT/CorrectRating/rs2.xml");
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

        Diff diff = new Diff(MSGRS,result);
        if (!diff.identical()) {
            assertEquals(MSGRS, result);
        }
    }

}
