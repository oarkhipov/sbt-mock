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
    MockConfigContainer configContainer;

    @Autowired
    DataFileService dataFileService;

    @Autowired
    GroovyService groovyService;
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
        ru.sbt.bpm.mock.config.entities.System system = configContainer.getConfig().getSystems().getSystemByName(systemName);
        IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);
        switch (system.getProtocol()) {
            case JMS:
                return generateJmsMessage(system, integrationPoint, filterMessage);
            case SOAP:
                return generateSoapMessage(system, integrationPoint, messageType);
        }
        throw new IllegalStateException("Reached unreachable code in xml generator");
    }

    public String generateJmsSystemMessage(String systemName) throws Exception {
        ru.sbt.bpm.mock.config.entities.System system = configContainer.getConfig().getSystems().getSystemByName(systemName);
        return generateJmsMessage(system, null, false);
    }

    private String generateJmsMessage(System system, IntegrationPoint integrationPoint, boolean filterMessage) throws Exception {
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
        String rootElementName = elementSelector.getElement();
        String rootElementNamespace = elementSelector!=null?elementSelector.getNamespace():null;

        URL resource = dataFileService.getXsdResource(system.getSystemName(), localRootSchema).getURL();
        assert resource != null;
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
                    "resource:["+resource.toString()+"]\n" +
                    "rootElement:["+rootElement.toString()+"]\n" +
                    "xsModel:["+xsModel.toString()+"]\n" +
                    "message: " + e.getMessage();
            throw new Exception(msg, e);
        }
        if (filterMessage) {
            return filterMessage(integrationPoint, writer);
        }
        return writer.toString();
    }

    //TODO make it work with RQ
    private String filterMessage(IntegrationPoint integrationPoint, StringWriter writer) throws Exception {
        List<ElementSelector> elementSelectors = integrationPoint.getXpathValidatorSelector().getElementSelectors();
        StringBuilder xmlMapElements = new StringBuilder();
        for (ElementSelector selector : elementSelectors) {
            xmlMapElements.append("        add(Tuple.of(\"")
                    .append(selector.getNamespace())
                    .append("\", \"")
                    .append(selector.getElement())
                    .append("\"))\n");
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
