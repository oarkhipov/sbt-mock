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
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;

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
    DataService dataService;

    public String generate(String systemName, String integrationPointName) throws URISyntaxException, IOException, TransformerConfigurationException {
        ru.sbt.bpm.mock.config.entities.System system = configContainer.getConfig().getSystems().getSystemByName(systemName);
        String rootXSD = system.getRootXSD();
        IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);
        ElementSelector elementSelector = integrationPoint.getRootElement();
        String rootElementName = elementSelector.getElement();
        String rootElementNamespace = elementSelector.getNamespace();

        URL resource = dataService.getXsdResource(systemName, rootXSD).getURL();
        assert resource != null;
        XSModel xsModel;
        xsModel = new XSParser().parse(String.valueOf(resource.toURI()));


        XSInstance xsInstance = new XSInstance();
        xsInstance.minimumElementsGenerated = 0;
        xsInstance.maximumElementsGenerated = 0;
        xsInstance.generateDefaultAttributes = true;
        xsInstance.generateOptionalElements = true;
        xsInstance.maximumRecursionDepth = 0;
        xsInstance.generateOptionalAttributes = true;
        xsInstance.generateAllChoices = true;

        xsInstance.showContentModel = false;

        QName rootElement = new QName(rootElementNamespace, rootElementName);
        StringWriter writer = new StringWriter();
        XMLDocument sampleXml;

        sampleXml = new XMLDocument(new StreamResult(writer), true, 4, null);

        xsInstance.generate(xsModel, rootElement, sampleXml);
        return writer.toString();
    }
}
