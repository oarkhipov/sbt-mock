package ru.sbt.bpm.mock.tests;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.messaging.support.GenericMessage;
import org.w3c.dom.Document;
import ru.sbt.bpm.mock.utils.XmlUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by sbt-bochev-as on 04.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
public class XmlAssertionTest {

    @Test
    public void XmlAssertionTest1() throws Exception {
        Document doc1 = XmlUtil.createXmlMessageFromResource("xmlAssertion/xml1.xml").getPayload();
        Document doc2 = XmlUtil.createXmlMessageFromResource("xmlAssertion/xml2.xml").getPayload();

        XMLUnit.setIgnoreWhitespace(true);

        Diff diff = new Diff(XmlUtil.docAsString(doc1), XmlUtil.docAsString(doc2));
        System.out.println("Identical: " + String.valueOf(diff.identical()));
        System.out.println("Similar: " + String.valueOf(diff.similar()));
    }
}
