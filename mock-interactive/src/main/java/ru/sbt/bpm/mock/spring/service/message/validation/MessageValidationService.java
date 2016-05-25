package ru.sbt.bpm.mock.spring.service.message.validation;

import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.WsdlContentPart;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlTestSuite;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockService;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.registry.WsdlTestRequestStepFactory;
import com.eviware.soapui.model.iface.Operation;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.entities.Systems;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.JmsMessageValidationException;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.MessageValidationException;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.SoapMessageValidationException;
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

@Slf4j
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
         * FileResourceResolver добавлен для корректной работы с XSD с одиннаковыми именами, но в разных директориях
         */
        validator = new HashMap<String, MessageValidator>();


        Systems systemsContainer = configContainer.getConfig().getSystems();
        if (systemsContainer != null) {
            List<System> systems = systemsContainer.getSystems();
            if (systems != null) {
                for (System system : systems) {
                    initValidator(system);
                }
            }
        }
    }

    /**
     * Init the validator
     *
     * @param system name of system, for which validator need to be init
     * @throws IOException
     * @throws SAXException
     */
    private void initValidator(System system) throws IOException, SAXException {
        File systemXsdDirectory = dataFileService.getSystemXsdDirectoryResource(system.getSystemName()).getFile();
        String remoteRootSchema = system.getRemoteRootSchema();
        Protocol protocol = system.getProtocol();
        switch (protocol) {
            case JMS:
                if (remoteRootSchema.toLowerCase().startsWith("http://")) {
                    String requestPath = remoteRootSchema.split("//")[1];
                    String basePath = requestPath.substring(requestPath.indexOf("/"), requestPath.lastIndexOf("/") + 1);
                    String relativePath = requestPath.substring(requestPath.indexOf("/") + 1, requestPath.length());
                    system.setLocalRootSchema(relativePath);

                    String absoluteSystemRootSchemaDir = dataFileService.getSystemXsdDirectoryResource(system.getSystemName()).getFile().getAbsolutePath() + basePath;
                    absoluteSystemRootSchemaDir = absoluteSystemRootSchemaDir.replace("/", File.separator);
                    validator.put(system.getSystemName(), new XsdValidator(remoteRootSchema, absoluteSystemRootSchemaDir));
                } else {
                    system.setLocalRootSchema(system.getRemoteRootSchema());
                    List<File> xsdFiles = dataFileService.searchFiles(systemXsdDirectory, ".xsd");
                    validator.put(system.getSystemName(), new XsdValidator(xsdFiles));
                }
                break;
            case SOAP:
                WsdlValidator wsdlValidator = new WsdlValidator(remoteRootSchema);
                WsdlProject wsdlProject = wsdlValidator.getWsdlProject();
                configContainer.getWsdlProjectMap().put(system.getSystemName(), wsdlProject);
                //create testsuite
                WsdlTestSuite testSuite = wsdlProject.addNewTestSuite("TestSuite");
                WsdlTestCase testCase = testSuite.addNewTestCase("TestCase");

                WsdlMockService mockService = wsdlProject.addNewMockService("MockService");
                //init test steps for all operations
                for (Operation operation : wsdlProject.getInterfaceList().get(0).getOperationList()) {
                    WsdlOperation wsdlOperation = (WsdlOperation) operation;
                    TestStepConfig testStepConfig = WsdlTestRequestStepFactory.createConfig(wsdlOperation, wsdlOperation.getName());
                    testCase.addTestStep(testStepConfig);
                    mockService.addNewMockOperation(operation);
                }

                validator.put(system.getSystemName(), wsdlValidator);
                break;
            default:
                throw new RuntimeException("Unknown system protocol!");
        }
    }

    /**
     * Валидирует xml на соответствие схем
     *
     * @param xml        спец имя xml
     * @param systemName подпапка из директорий xsd и data, по которым будет производится ваидация
     * @return признак валидности
     */
    public List<String> validate(String xml, String systemName) {
        return validator.get(systemName).validate(xml);
    }


    /**
     * Get element by xpath and asserts, that it exists
     *
     * @param xml                  xml to search into
     * @param systemName           name of system, which xml belongs
     * @param integrationPointName name of integration point, which message represents
     * @param messageType          request or response message
     * @return true if one or more elements match the xpath
     * @throws XPathExpressionException
     * @throws SaxonApiException
     */
    public boolean assertMessageElementName(String xml, String systemName, String integrationPointName, MessageType messageType) throws XPathExpressionException, SaxonApiException, XmlException, MessageValidationException {
        System system = configContainer.getSystemByName(systemName);
        IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);
        return assertMessageElementName(xml, system, integrationPoint, messageType);
    }

    public boolean assertMessageElementName(String xml, System system, IntegrationPoint integrationPoint, MessageType messageType) throws SaxonApiException, XmlException, MessageValidationException {
        Protocol protocol = system.getProtocol();
        if (protocol == Protocol.JMS) {
            return assertByXpath(xml, system, integrationPoint, messageType);
        }
        if (protocol == Protocol.SOAP) {
            return assertByOperation(xml, system, integrationPoint, messageType);
        }
        throw new IllegalStateException("Unknown protocol: " + messageType.name());
    }

    private boolean assertByOperation(String xml, System system, IntegrationPoint integrationPoint, MessageType messageType) throws XmlException, SoapMessageValidationException {
        WsdlProject wsdlProject = configContainer.getWsdlProjectMap().get(system.getSystemName());
        WsdlOperation operation = (WsdlOperation) wsdlProject.getInterfaceList().get(0).getOperationByName(integrationPoint.getName());
        String elementName = getSoapMessageElementName(XpathUtils.compactXml(xml));
        assert !elementName.isEmpty();
        if (messageType == MessageType.RQ) {
            String expectedElementName = ((WsdlContentPart) operation.getDefaultRequestParts()[0]).getPartElementName().getLocalPart();
            boolean validationResult = elementName.equals(expectedElementName);
            if (validationResult) {
                return true;
            } else {
                throw new SoapMessageValidationException(expectedElementName, elementName);
            }
        }
        if (messageType == MessageType.RS) {
            String expectedElementName = ((WsdlContentPart) operation.getDefaultResponseParts()[0]).getPartElementName().getLocalPart();
            boolean validationResult = elementName.equals(expectedElementName);
            if (validationResult) {
                return true;
            } else {
                throw new SoapMessageValidationException(expectedElementName, elementName);
            }
        }
        throw new IllegalStateException("Unknown message type: " + messageType.name());
    }

    private boolean assertByXpath(String xml, System system, IntegrationPoint integrationPoint, MessageType messageType) throws SaxonApiException, JmsMessageValidationException {
        if (messageType == MessageType.RQ) {
            //ipSelector+ipName
            String integrationPointSelectorXpath = system.getIntegrationPointSelector().toXpath();
            String integrationPointName = integrationPoint.getName();
            XdmValue value = XpathUtils.evaluateXpath(xml, integrationPointSelectorXpath);
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
            boolean validationResult = XpathUtils.evaluateXpath(xml, xpathWithFullNamespaceString).size() != 0;
            if (validationResult) {
                return true;
            } else {
                throw new JmsMessageValidationException(xpathWithFullNamespaceString);
            }
        }
        throw new IllegalStateException("Unknown message type: " + messageType.name());
    }

    public String getSoapMessageElementName(String compactXml) throws XmlException {
        final String BODY = "Body";
        final String ENVELOPE = "Envelope";
        final Envelope envelope = Envelope.Factory.parse(compactXml);
        final Node node = envelope.getDomNode();
        final Node envelopeNode = node.getFirstChild();
        Node bodyNode = envelopeNode.getFirstChild();
        String messageType = null;

        while (BODY.equals(bodyNode.getLocalName()) == false && bodyNode != null) {

            bodyNode = bodyNode.getNextSibling();
        }

        if (ENVELOPE.equals(envelopeNode.getLocalName()) && bodyNode != null && BODY.equals(bodyNode.getLocalName())) {
            final Node nodeType = bodyNode.getFirstChild();
            messageType = nodeType.getLocalName();
        }
        return messageType;
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
