/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ru.sbt.bpm.mock.jlibs;

import jlibs.xml.sax.XMLDocument;
import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSParser;
import org.apache.xerces.xs.XSModel;
import org.testng.annotations.Test;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.net.URL;

import static org.testng.Assert.assertTrue;

/**
 * @author sbt-bochev-as on 23.11.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */

public class XmlGeneratorTest {

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
        assertTrue(generatedXml.contains("Envelope"));
        assertTrue(generatedXml.contains("sendReferenceData"));
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
        assertTrue(generatedXml.contains("Envelope"));
        assertTrue(generatedXml.contains("sendReferenceData"));
    }
}
