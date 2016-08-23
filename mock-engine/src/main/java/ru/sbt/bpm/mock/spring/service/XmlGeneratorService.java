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

package ru.sbt.bpm.mock.spring.service;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.iface.Interface;
import com.eviware.soapui.support.SoapUIException;
import jlibs.xml.sax.XMLDocument;
import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSParser;
import org.apache.xerces.xs.XSModel;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.ElementSelector;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.MessageType;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;

/**
 * @author sbt-bochev-as on 03.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Service
public class XmlGeneratorService {

    @Autowired
    private MockConfigContainer configContainer;

    @Autowired
    private DataFileService dataFileService;

    @Autowired
    private GroovyService groovyService;

    private XSInstance xsInstance;

    @PostConstruct
    private void init() throws XmlException, IOException, SoapUIException {
        //JMS
        xsInstance = new XSInstance();
        xsInstance.minimumElementsGenerated = 1;
        xsInstance.maximumElementsGenerated = 1;
        xsInstance.generateDefaultAttributes = true;
        xsInstance.generateOptionalElements = true;
        xsInstance.maximumRecursionDepth = 1;
        xsInstance.generateOptionalAttributes = true;
        xsInstance.generateAllChoices = true;

        xsInstance.showContentModel = false;

        //SOAP
        //Already created wsdlProject at validator initialization stage
    }


    public String generate(String systemName, String integrationPointName, MessageType messageType, boolean filterMessage) throws Exception {
        System system = configContainer.getConfig().getSystems().getSystemByName(systemName);
        IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);
        switch (system.getProtocol()) {
            case JMS:
                return generateJmsMessage(system, integrationPoint, messageType, filterMessage);
            case SOAP:
                return generateSoapMessage(system, integrationPoint, messageType);
        }
        throw new IllegalStateException("Reached unreachable code in xml generator");
    }

    public String generateJmsSystemMessage(String systemName) throws Exception {
        System system = configContainer.getConfig().getSystems().getSystemByName(systemName);
        return generateJmsMessage(system, null, null, false);
    }

    private String generateJmsMessage(System system, IntegrationPoint integrationPoint, MessageType messageType, boolean filterMessage) throws Exception {
        String localRootSchema = system.getLocalRootSchema();
        ElementSelector elementSelector = system.getRootElement();
        if (integrationPoint != null) {
            if (integrationPoint.getXsdFile() != null && !integrationPoint.getXsdFile().isEmpty()) {
                localRootSchema = integrationPoint.getXsdFile();
            }
            if (integrationPoint.getRootElement() != null) {
                elementSelector = integrationPoint.getRootElement();
            }
        }
        //if no System and IntegrationPoint rootElement specified
        if (elementSelector == null || (elementSelector.getNamespace().isEmpty() && elementSelector.getElement().isEmpty())) {
            //get first element selector from integration point selector
            elementSelector = system.getIntegrationPointSelector().getElementSelectors().get(0);
        }

        String rootElementName = elementSelector.getElement();
        @SuppressWarnings("ConstantConditions")
        String rootElementNamespace = elementSelector != null ? elementSelector.getNamespace() : null;

        URL resource = dataFileService.getXsdFile(system.getSystemName(), localRootSchema).toURI().toURL();
        XSModel xsModel;
        xsModel = new XSParser().parse(String.valueOf(resource.toURI()));

        QName rootElement = new QName(rootElementNamespace, rootElementName);
        StringWriter writer = new StringWriter();
        XMLDocument sampleXml;

        sampleXml = new XMLDocument(new StreamResult(writer), true, 4, null);

        try {
            xsInstance.generate(xsModel, rootElement, sampleXml);
        } catch (Exception e) {
            String msg = "Failed to generate xml:\n" +
                    "resource:[" + resource.toString() + "]\n" +
                    "rootElement:[" + rootElement.toString() + "]\n\n" +
//                    "xsModel:[" + xsModel.toString() + "]\n" +
                    "message: " + e.getMessage();
            throw new Exception(msg, e);
        }
        if (filterMessage) {
            return filterMessage(system, integrationPoint, messageType, writer);
        }
        return writer.toString();
    }

    private String filterMessage(System system, IntegrationPoint integrationPoint, MessageType messageType, StringWriter writer) throws Exception {
        StringBuilder xmlMapElements = new StringBuilder();
        if (messageType == MessageType.RQ) {
            //mock test & driver message
            List<ElementSelector> elementSelectors = system.getIntegrationPointSelector().getElementSelectors();
            for (int i = 0; i < elementSelectors.size(); i++) {
                ElementSelector selector = elementSelectors.get(i);
                xmlMapElements.append("        add(Tuple.of(\"")
                        .append(selector.getNamespace())
                        .append("\", \"");
                //if last element == null -> last element name = integration point name
                if ((i == elementSelectors.size() - 1) && ((selector.getElement() == null) || (selector.getElement().isEmpty()))) {
                    xmlMapElements.append(integrationPoint.getName()).append("\"))\n");
                } else {
                    xmlMapElements.append(selector.getElement()).append("\"))\n");
                }
            }
        } else {
            List<ElementSelector> elementSelectors = integrationPoint.getXpathValidatorSelector().getElementSelectors();
            for (ElementSelector selector : elementSelectors) {
                xmlMapElements.append("        add(Tuple.of(\"")
                        .append(selector.getNamespace())
                        .append("\", \"")
                        .append(selector.getElement())
                        .append("\"))\n");
            }
        }

        String filterScript =
                "import groovy.xml.QName\n" +
                        "import reactor.tuple.Tuple;\n" +
                        "import reactor.tuple.Tuple2;\n" +
                        "\n" +
                        "def xmlMap = new ArrayList<Tuple2<String, String>>() {\n" +
                        "    {\n" +
                        xmlMapElements +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "//assert first element\n" +
                        "if (((QName) requestDom.name()).namespaceURI == xmlMap.get(0).t1 &&\n" +
                        "        ((QName) requestDom.name()).localPart == xmlMap.get(0).t2) {\n" +
                        "\n" +
                        "    //dom to search in\n" +
                        "    def filterDom = requestDom\n" +
                        "    //new root element\n" +
                        "    def newFilterDom\n" +
                        "    //remove elements array\n" +
                        "    def remove = []\n" +
                        "    //parent element to delete from\n" +
                        "//    def parent\n" +
                        "\n" +
                        "    //drawn to last element\n" +
                        "    for (int i = 1; i < xmlMap.size(); i++) {\n" +
                        "        for (Node child : (List<Node>) filterDom.children()) {\n" +
                        "            def el = (QName) child.name()\n" +
                        "            if (el.namespaceURI == xmlMap.get(i).t1 && el.localPart == xmlMap.get(i).t2) {\n" +
                        "                //if element found - make new root\n" +
                        "                newFilterDom = child\n" +
                        "            } else {\n" +
                        "                // last level of neighbour elements\n" +
                        "                if(i == xmlMap.size()-1) {\n" +
                        "                   //add elements to remove array\n" +
                        "                    remove.add new Tuple(child.parent(), child)\n" +
                        "                    //save parent to remove from\n" +
                        "                    //parent = child.parent()\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "        if (newFilterDom == null) break;\n" +
                        "        if (filterDom != newFilterDom) {\n" +
                        "            //new root found\n" +
                        "            filterDom = newFilterDom\n" +
                        "        } else {\n" +
                        "            //no new root\n" +
                        "            break;\n" +
                        "        }\n" +
                        "    }\n" +
                        "    //remove marked\n" +
                        "    remove.each { it.get(0).remove(it.get(1)) }\n" +
                        "//                \"//response.test=requestDom[s.sendAdditionalInfo][s.clientSystemTaskID].text()\n" +
                        "}\n" +
                        "def stringWriter = new StringWriter()\n" +
                        "xmlNodePrinter = new XmlNodePrinter(new PrintWriter(stringWriter))\n" +
                        "xmlNodePrinter.with {\n" +
                        "    preserveWhitespace = true\n" +
                        "}\n" +
                        "xmlNodePrinter.print(requestDom)\n" +
                        "response.result=stringWriter.toString()";
        return groovyService.execute(writer.toString(), "${result}", filterScript);
    }

    private String generateSoapMessage(System system, IntegrationPoint integrationPoint, MessageType messageType) {
        WsdlProject wsdlProject = configContainer.getWsdlProjectMap().get(system.getSystemName());
        String integrationPointName = integrationPoint.getName();
        for (Interface anInterface : wsdlProject.getInterfaceList()) {
            try {
                WsdlInterface wsdlInterface = (WsdlInterface) anInterface;
                WsdlOperation wsdlOperation = wsdlInterface.getOperationByName(integrationPointName);
                if (messageType.equals(MessageType.RQ)) return wsdlOperation.createRequest(true);
                if (messageType.equals(MessageType.RS)) return wsdlOperation.createResponse(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new IllegalStateException("no such WSDL interface with operation [" + integrationPointName + "]");
    }
}
