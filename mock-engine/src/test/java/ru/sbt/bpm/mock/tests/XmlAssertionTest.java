package ru.sbt.bpm.mock.tests;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import ru.sbt.bpm.mock.spring.utils.XmlUtil;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


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

        String xml1 = XmlUtil.docAsString(doc1);
        String xml2 = XmlUtil.docAsString(doc2);
        Diff diff = new Diff(xml1, xml2);
        assertFalse(diff.identical(), "Identical must be false!");
        assertTrue(diff.similar(), "Similar must be true!");
    }
}
