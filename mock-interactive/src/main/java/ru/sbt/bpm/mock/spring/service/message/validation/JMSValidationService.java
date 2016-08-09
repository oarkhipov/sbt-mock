package ru.sbt.bpm.mock.spring.service.message.validation;

import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.JmsMessageValidationException;
import ru.sbt.bpm.mock.spring.service.message.validation.impl.XsdValidator;
import ru.sbt.bpm.mock.spring.utils.XmlUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author SBT-Bochev-AS on 01.08.2016.
 *         <p>
 *         Company: SBT - Moscow
 */
@Slf4j
@Service
public class JMSValidationService {

    @Autowired
    private DataFileService dataFileService;

    public void init(Map validator, System system, String systemName, File systemXsdDirectory, String remoteRootSchema, String remoteSchemaInLowerCase) throws IOException, SAXException {
        if (remoteSchemaInLowerCase.startsWith("http://") || remoteSchemaInLowerCase.startsWith("ftp://")) {
            log.info(String.format("Loading schema %s", remoteRootSchema));
            String requestPath = remoteRootSchema.split("//")[1];
            String basePath = requestPath.substring(requestPath.indexOf("/"), requestPath.lastIndexOf("/") + 1);
            String relativePath = requestPath.substring(requestPath.indexOf("/") + 1, requestPath.length());
            system.setLocalRootSchema(relativePath);

            String absoluteSystemRootSchemaDir = dataFileService.getSystemXsdDirectoryFile(systemName)
                    .getAbsolutePath() + basePath;
            absoluteSystemRootSchemaDir = absoluteSystemRootSchemaDir.replace("/", File.separator);
            validator.put(systemName, new XsdValidator(remoteRootSchema, absoluteSystemRootSchemaDir));
        } else {
            system.setLocalRootSchema(remoteRootSchema);
            List<File> xsdFiles = dataFileService.searchFiles(systemXsdDirectory, ".xsd");
            log.info(String.format("Loading files %s", ArrayUtils.toString(xsdFiles)));
            validator.put(systemName, new XsdValidator(xsdFiles));
        }
    }

    public boolean assertByXpath(String xml, System system, IntegrationPoint integrationPoint, MessageType messageType) throws SaxonApiException, JmsMessageValidationException {
        log.info(String.format("Assert xml message type [%s]", messageType));
        if (messageType == MessageType.RQ) {
            //ipSelector+ipName
            String integrationPointSelectorXpath = system.getIntegrationPointSelector().toXpath();
            String integrationPointName = integrationPoint.getName();
            log.info(String.format("For integration point:  [%s] xPath %s ", integrationPointName, integrationPointSelectorXpath));
            XdmValue value = XmlUtils.evaluateXpath(xml, integrationPointSelectorXpath);
            String elementName = ((XdmNode) value).getNodeName().getLocalName();
            boolean validationResult = elementName.equals(integrationPointName);
            if (validationResult) {
                return true;
            } else {
                throw new JmsMessageValidationException(integrationPointSelectorXpath, integrationPointName, elementName);
            }
        }
        if (messageType == MessageType.RS) {
            //xpathSelector
            String xpathWithFullNamespaceString = integrationPoint.getXpathWithFullNamespaceString();
            log.debug("assert xpath: " + xpathWithFullNamespaceString);
            boolean validationResult = XmlUtils.evaluateXpath(xml, xpathWithFullNamespaceString).size() != 0;
            if (validationResult) {
                return true;
            } else {
                throw new JmsMessageValidationException(xpathWithFullNamespaceString);
            }
        }
        throw new IllegalStateException("Unknown message type: " + messageType.name());
    }
}
