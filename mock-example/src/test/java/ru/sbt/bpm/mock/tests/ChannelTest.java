package ru.sbt.bpm.mock.tests;

import org.custommonkey.xmlunit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import ru.sbt.bpm.mock.service.ChannelService;
import ru.sbt.bpm.mock.utils.XmlUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.StringReader;
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

    @Test
    public void createTaskTest() throws Exception {
        testXSLT("CreateTask", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/createTaskRQ.xml", "xml/CRM/createTaskRS.xml");
    }

    @Test
    public void createTaskTest2() throws Exception {
        testXSLT("CreateTask", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/createTaskRQ2.xml", "xml/CRM/createTaskRS2.xml");
    }

    @Test
    public void getParticipants() throws Exception {
        testXSLT("GetParticipants", "ESB.BPM.NCP.OUT.MOCK", "xml/CRM/GetParticipantsRQ.xml", "xml/CRM/GetParticipantsRS.xml");
    }

    @Test
    public void createTaskTestAMRLiRT_SrvCalcDebtCapacity() throws Exception {
        testXSLT("SrvCalcDebtCapacity", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateDebtCapacityRequest.xml", "xml/AMRLiRT/CalculateDebtCapacityResponse.xml");
    }

    @Test
    public void createTaskTestAMRLiRT_SrvCalcLGD() throws Exception {
        testXSLT("SrvCalcLGD", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateLGDRequest.xml", "xml/AMRLiRT/CalculateLGDResponse.xml");
    }

    @Test
    public void createTaskTestAMRLiRT_SrvCalcRating() throws Exception {
        testXSLT("SrvCalcRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CalculateRatingRequest.xml", "xml/AMRLiRT/CalculateRatingResponse.xml");
    }

    @Test
    public void createTaskTestAMRLiRT_SrvConfirmRating() throws Exception {
        testXSLT("SrvConfirmRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/ConfirmRatingRequest.xml", "xml/AMRLiRT/ConfirmRatingResponse.xml");
    }

    @Test
    public void createTaskTestAMRLiRT_SrvFinalizeLGD() throws Exception {
        testXSLT("SrvFinalizeLGD", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/FinalizeLGDRequest.xml", "xml/AMRLiRT/FinalizeLGDResponse.xml");
    }

    @Test
    public void createTaskTestAMRLiRT_SrvUpdateRating() throws Exception {
        testXSLT("SrvUpdateRating", "ESB.BPM.NCP.OUT.MOCK", "xml/AMRLiRT/CorrectRatingRequest.xml", "xml/AMRLiRT/CorrectRatingResponse.xml");
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
