package ru.sbt.bpm.mock.spring.service;

import jlibs.xml.sax.XMLDocument;
import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSParser;
import org.apache.xerces.xs.XSModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.ElementSelector;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;
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

    public String generate(String systemName, String integrationPointName) throws Exception {
        ru.sbt.bpm.mock.config.entities.System system = configContainer.getConfig().getSystems().getSystemByName(systemName);
        String rootXSD = system.getRootXSD();
        IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);
        ElementSelector elementSelector = integrationPoint.getRootElement();
        String rootElementName = elementSelector.getElement();
        String rootElementNamespace = elementSelector.getNamespace();

        URL resource = dataFileService.getXsdResource(systemName, rootXSD).getURL();
        assert resource != null;
        XSModel xsModel;
        xsModel = new XSParser().parse(String.valueOf(resource.toURI()));


        XSInstance xsInstance = new XSInstance();
        xsInstance.minimumElementsGenerated = 1;
        xsInstance.maximumElementsGenerated = 1;
        xsInstance.generateDefaultAttributes = true;
        xsInstance.generateOptionalElements = true;
        xsInstance.maximumRecursionDepth = 1;
        xsInstance.generateOptionalAttributes = true;
        xsInstance.generateAllChoices = true;

        xsInstance.showContentModel = false;

        QName rootElement = new QName(rootElementNamespace, rootElementName);
        StringWriter writer = new StringWriter();
        XMLDocument sampleXml;

        sampleXml = new XMLDocument(new StreamResult(writer), true, 4, null);

        xsInstance.generate(xsModel, rootElement, sampleXml);
        //TODO сделать универсальным для любых сообщений
        List<ElementSelector> elementSelectors = integrationPoint.getXpathValidatorSelector().getElementSelectors();
        int elementSize = elementSelectors.size();
        String lastElementAssertion = elementSelectors.get(elementSize-1).getElement();

        String filterScript = "def stringWriter = new StringWriter()\n" +
                "def s = new groovy.xml.Namespace(\"http://sbrf.ru/legal/enquiry/integration\")\n" +
                "def remove = []\n" +
                "def parent\n" +
                "requestDom.each { tag ->\n" +
                "   if(tag.name().localPart != \"" + lastElementAssertion +"\"){\n" +
                "       remove.add tag\n" +
                "       parent = tag.parent()\n" +
                "   }\n" +
                "}\n" +
                "remove.each { parent.remove(it) }\n" +
//                "//response.test=requestDom[s.sendAdditionalInfo][s.clientSystemTaskID].text()\n" +
                "\n" +
                "xmlNodePrinter = new XmlNodePrinter(new PrintWriter(stringWriter))\n" +
                "xmlNodePrinter.with {\n" +
                "   preserveWhitespace = true\n" +
                "}\n" +
                "xmlNodePrinter.print(requestDom)\n" +
                "response.result=stringWriter.toString()\n" +
                "\n";
        return groovyService.compile(writer.toString(), "${result}", filterScript);
    }
}
