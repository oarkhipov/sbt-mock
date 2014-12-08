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
        IN="CreateTask";
        OUT = "ESB.BPM.NCP.OUT.MOCK";
        MSGRQ = XmlUtil.docAsString(XmlUtil.createXmlMessageFromResource("xml/CRM/createTaskRQ.xml").getPayload());
        MSGRS = XmlUtil.docAsString(XmlUtil.createXmlMessageFromResource("xml/CRM/createTaskRS.xml").getPayload());

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

    @Test
    public void createTaskTest2() throws Exception {
        IN="CreateTask";
        OUT = "ESB.BPM.NCP.OUT.MOCK";
        MSGRQ = XmlUtil.docAsString(XmlUtil.createXmlMessageFromResource("xml/CRM/createTaskRQ2.xml").getPayload());
        MSGRS = XmlUtil.docAsString(XmlUtil.createXmlMessageFromResource("xml/CRM/createTaskRS2.xml").getPayload());

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
