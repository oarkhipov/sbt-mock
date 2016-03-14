package ru.sbt.bpm.mock.jlibs;

import jlibs.xml.sax.XMLDocument;
import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSParser;
import org.apache.xerces.xs.XSModel;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.net.URL;

/**
 * @author sbt-bochev-as on 23.11.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class GeneratorTest {

    @Test
    public void generateXml() throws Exception {
        URL resource = this.getClass().getClassLoader().getResource("WEB-INF/xsd/CRM/CRMIntegrationSchema.xsd");
        assert resource != null;
        XSModel xsModel = new XSParser().parse(String.valueOf(resource.toURI()));

        XSInstance xsInstance = new XSInstance();
        xsInstance.minimumElementsGenerated = 0;
        xsInstance.maximumElementsGenerated = 0;
        xsInstance.generateDefaultAttributes = true;
        xsInstance.generateOptionalElements = true;
        xsInstance.maximumRecursionDepth = 0;
        xsInstance.generateOptionalAttributes = true;
        xsInstance.generateAllChoices = true;

        xsInstance.showContentModel = false;


        QName rootElement = new QName("http://sbrf.ru/legal/enquiry/integration","Envelope");
        StringWriter writer = new StringWriter();
        XMLDocument sampleXml = new XMLDocument(new StreamResult(writer), true, 4 ,null);
        xsInstance.generate(xsModel, rootElement, sampleXml);
        String generatedXml = writer.toString();
        Assert.assertTrue(generatedXml.contains("Envelope"));
        Assert.assertTrue(generatedXml.contains("sendReferenceData"));
    }

    @Test
    public void generateXmlFromMain() throws Exception {
        URL resource = this.getClass().getClassLoader().getResource("WEB-INF/xsd/CRM/CRMIntegrationSchema.xsd");
        String namespaceURI = "http://sbrf.ru/legal/enquiry/integration";
        String rootElementName = "Envelope";

        assert resource != null;
        XSModel xsModel = new XSParser().parse(String.valueOf(resource.toURI()));

        XSInstance xsInstance = new XSInstance();
        xsInstance.minimumElementsGenerated = 0;
        xsInstance.maximumElementsGenerated = 0;
        xsInstance.generateDefaultAttributes = true;
        xsInstance.generateOptionalElements = true;
        xsInstance.maximumRecursionDepth = 0;
        xsInstance.generateOptionalAttributes = true;
        xsInstance.generateAllChoices = true;

        xsInstance.showContentModel = false;

        QName rootElement = new QName(namespaceURI, rootElementName);
        StringWriter writer = new StringWriter();
        XMLDocument sampleXml = new XMLDocument(new StreamResult(writer), true, 4 ,null);
        xsInstance.generate(xsModel, rootElement, sampleXml);
        String generatedXml = writer.toString();
        assert generatedXml.contains("Envelope") : generatedXml;
        assert generatedXml.contains("sendReferenceData") : generatedXml;
    }
}
