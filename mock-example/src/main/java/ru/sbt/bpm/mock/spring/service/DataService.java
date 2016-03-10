package ru.sbt.bpm.mock.spring.service;

import lombok.extern.log4j.Log4j;
import net.sf.saxon.s9api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.spring.utils.ResourceResolver;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by sbt-bochev-as on 13.12.2014.
 *
 * @author sbt-vostrikov-mi
 *         <p/>
 *         Company: SBT - Moscow
 */
@Log4j
@Service
public class DataService {

    @Autowired
    private DataFileService dataFileService;

    @Autowired
    private MockConfigContainer configContainer;

    private Map<String, Validator> validator;


    @PostConstruct
    protected void init() throws IOException, SAXException {

        /**
         * ResourceResolver добавлен для корректной работы с XSD с одиннаковыми именами, но в разных директориях
         */
        validator = new HashMap<String, Validator>();

        for (System system : configContainer.getConfig().getSystems().getSystems()) {
            initValidator(system);
        }
    }

    private void initValidator(System system) throws IOException, SAXException {
        File systemXsdDirectory = dataFileService.getSystemXsdDirectoryResource(system.getSystemName()).getFile();
        String rootXSD = system.getRootXSD();
        if (rootXSD.startsWith("http://")) {
            //http validator
            initHttpValidator(system.getSystemName(), new URL(rootXSD));
        } else {
            //file validator
            List<File> xsdFiles = dataFileService.searchFiles(systemXsdDirectory, ".xsd");
            initSystemValidator(system.getSystemName(), xsdFiles);
        }
    }

    /**
     * заведение валидатора для списка xsd файлов и добавление его в карту валидаторов
     *
     * @param system   имя валидатора - соответсвует имени системы и папке в xsd и data директориях
     * @param xsdFiles список xsd-файлов
     */
    private void initSystemValidator(String system, List<File> xsdFiles) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        StreamSource sources[] = new StreamSource[xsdFiles.size()];

//            Add Xsd files to source
        for (int i = 0; i < xsdFiles.size(); i++) {
//            System.out.println(xsdFiles.get(i).getAbsolutePath());
            sources[i] = new StreamSource(xsdFiles.get(i));
        }
        factory.setResourceResolver(new ResourceResolver());
        Schema schema = factory.newSchema(sources);
        validator.put(system, schema.newValidator());
    }

    private void initHttpValidator(String system, URL schemaUrl) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(schemaUrl);
        validator.put(system, schema.newValidator());
    }

    /**
     * Валидирует xml на соответствие схем
     *
     * @param xml    спец имя xml
     * @param System подпапка из директорий xsd и data, по которым будет производится ваидация
     * @return признак валидности
     * @throws Exception
     */
    public boolean validate(String xml, String System) throws Exception {
        InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        try {
            validator.get(System).validate(new StreamSource(stream));
        } catch (SAXParseException e) {
            SAXParseException saxParseException = new SAXParseException("Validation error on line " + e.getLineNumber() + " and column " + e.getColumnNumber(), null, e);
            log.error(saxParseException);
            throw saxParseException;
        }
        return true;
    }

    public boolean assertXpath(String xml, String systemName, String integrationPointName) throws XPathExpressionException, SaxonApiException {

        String xpathWithFullNamespaceString = configContainer.getConfig()
                .getSystems().getSystemByName(systemName)
                .getIntegrationPoints().getIntegrationPointByName(integrationPointName)
                .getXpathWithFullNamespaceString();
        log.debug("assert xpath: " + xpathWithFullNamespaceString);

        return evaluateXpath(xml, xpathWithFullNamespaceString).size() != 0;
    }

    public XdmValue evaluateXpath(String inputXml, String xpath) throws SaxonApiException {
        Processor processor = new Processor(false);
        XPathCompiler xPathCompiler = processor.newXPathCompiler();
        DocumentBuilder builder = processor.newDocumentBuilder();

        //build doc
        StringReader reader = new StringReader(inputXml);
        XdmNode xdmNode = builder.build(new StreamSource(reader));

        //load xpath
        XPathSelector selector = xPathCompiler.compile(xpath).load();
        selector.setContextItem(xdmNode);
        return selector.evaluate();
    }

    /**
     * reInitialize validator by it's system name if xsd data was changed
     *
     * @param systemName name of system to re-init
     * @throws IOException
     * @throws SAXException
     */
    public void reinitValidator(String systemName) throws IOException, SAXException {
        System system = configContainer.getConfig().getSystems().getSystemByName(systemName);
        validator.remove(systemName);
        initValidator(system);
    }
}
