package ru.sbt.bpm.mock.spring.service.message.validation;

import lombok.extern.log4j.Log4j;
import net.sf.saxon.s9api.SaxonApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.Protocols;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.message.validation.impl.WsdlValidator;
import ru.sbt.bpm.mock.spring.service.message.validation.impl.XsdValidator;
import ru.sbt.bpm.mock.spring.utils.XpathUtils;

import javax.annotation.PostConstruct;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
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
public class MessageValidationService {

    @Autowired
    private DataFileService dataFileService;

    @Autowired
    private MockConfigContainer configContainer;

    private Map<String, MessageValidator> validator;


    @PostConstruct
    protected void init() throws IOException, SAXException {

        /**
         * ResourceResolver добавлен для корректной работы с XSD с одиннаковыми именами, но в разных директориях
         */
        validator = new HashMap<String, MessageValidator>();

        for (System system : configContainer.getConfig().getSystems().getSystems()) {
            initValidator(system);
        }
    }

    private void initValidator(System system) throws IOException, SAXException {
        File systemXsdDirectory = dataFileService.getSystemXsdDirectoryResource(system.getSystemName()).getFile();
        String rootXSD = system.getRootXSD();
        Protocols protocol = system.getProtocol();
        switch (protocol) {
            case MQ:
                List<File> xsdFiles = dataFileService.searchFiles(systemXsdDirectory, ".xsd");
                validator.put(system.getSystemName(), new XsdValidator(xsdFiles));
                break;
            case SOAP:
                validator.put(system.getSystemName(), new WsdlValidator(rootXSD));
                break;
            default:
                throw new RuntimeException("Unknown system protocol!");
        }
    }

    /**
     * Валидирует xml на соответствие схем
     *
     * @param xml    спец имя xml
     * @param System подпапка из директорий xsd и data, по которым будет производится ваидация
     * @return признак валидности
     */
    public List<String> validate(String xml, String System) {
        return validator.get(System).validate(xml);
    }


    public boolean assertXpath(String xml, String systemName, String integrationPointName) throws XPathExpressionException, SaxonApiException {

        String xpathWithFullNamespaceString = configContainer.getConfig()
                .getSystems().getSystemByName(systemName)
                .getIntegrationPoints().getIntegrationPointByName(integrationPointName)
                .getXpathWithFullNamespaceString();
        log.debug("assert xpath: " + xpathWithFullNamespaceString);

        return XpathUtils.evaluateXpath(xml, xpathWithFullNamespaceString).size() != 0;
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
