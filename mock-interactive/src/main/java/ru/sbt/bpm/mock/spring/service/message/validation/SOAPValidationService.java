package ru.sbt.bpm.mock.spring.service.message.validation;

import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlTestSuite;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockService;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.registry.WsdlTestRequestStepFactory;
import com.eviware.soapui.model.iface.Interface;
import com.eviware.soapui.model.iface.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.MessageValidationException;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.SoapMessageValidationException;
import ru.sbt.bpm.mock.spring.service.message.validation.impl.WsdlValidator;
import ru.sbt.bpm.mock.spring.utils.XmlUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author SBT-Bochev-AS on 01.08.2016.
 *         <p>
 *         Company: SBT - Moscow
 */

@Slf4j
@Service
public class SOAPValidationService {

    private MockConfigContainer configContainer;
    private DataFileService dataFileService;

    @Autowired
    public SOAPValidationService(MockConfigContainer configContainer, DataFileService dataFileService) {
        this.configContainer = configContainer;
        this.dataFileService = dataFileService;
    }

    public void init(Map validator, System system, String systemName, String remoteRootSchema, String localRootSchema, String remoteSchemaInLowerCase) throws IOException {
        WsdlValidator wsdlValidator;
        if (localRootSchema != null && !localRootSchema.isEmpty()) {
            wsdlValidator = initLocalWsdlValidator(system, localRootSchema);
        } else {
            if (remoteSchemaInLowerCase.startsWith("http://") || remoteSchemaInLowerCase.startsWith("ftp://")) {
                wsdlValidator = new WsdlValidator(remoteRootSchema);
            } else {
                system.setLocalRootSchema(remoteRootSchema);
                wsdlValidator = initLocalWsdlValidator(system, localRootSchema);
            }

        }

        WsdlProject wsdlProject = wsdlValidator.getWsdlProject();
        configContainer.getWsdlProjectMap().put(systemName, wsdlProject);
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

        validator.put(systemName, wsdlValidator);
    }

    private WsdlValidator initLocalWsdlValidator(System system, String localRootSchema) throws IOException {
        WsdlValidator wsdlValidator;
        wsdlValidator = new WsdlValidator("file:" +
                dataFileService.getSystemXsdDirectoryFile(system.getSystemName()).getAbsolutePath() +
                File.separator +
                localRootSchema);
        return wsdlValidator;
    }

    public boolean assertByOperation(String xml, System system, IntegrationPoint integrationPoint, MessageType messageType) throws XmlException, SoapMessageValidationException {
        WsdlProject wsdlProject = configContainer.getWsdlProjectMap().get(system.getSystemName());
        WsdlOperation operation = (WsdlOperation) wsdlProject.getInterfaceList().get(0).getOperationByName(integrationPoint.getName());
        String elementName = getSoapMessageElementName(XmlUtils.compactXml(xml));
        assert elementName != null;
        assert !elementName.isEmpty();
        String expectedElementName = getDefaultRootElement(operation, messageType);
        boolean validationResult = elementName.equals(expectedElementName);
        if (validationResult) {
            return true;
        } else {
            throw new SoapMessageValidationException(expectedElementName, elementName);
        }
    }

    public String getSoapMessageElementName(String compactXml) throws XmlException {
        final String BODY = "Body";
        final String ENVELOPE = "Envelope";
        final Envelope envelope = Envelope.Factory.parse(compactXml);
        final Node node = envelope.getDomNode();
        final Node envelopeNode = node.getFirstChild();
        Node bodyNode = envelopeNode.getFirstChild();
        String messageType = null;

        while (!BODY.equals(bodyNode.getLocalName())) {
            bodyNode = bodyNode.getNextSibling();
        }

        if (ENVELOPE.equals(envelopeNode.getLocalName()) && BODY.equals(bodyNode.getLocalName())) {
            final NodeList childNodes = bodyNode.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                final String localName = childNodes.item(i).getLocalName();
                if (localName != null && !localName.isEmpty()) {
                    messageType = localName;
                }
            }
        }
        return messageType;
    }

    public String getOperationByElementName(String systemName, String elementName, MessageType messageType) {
        WsdlProject wsdlProject = configContainer.getWsdlProjectMap().get(systemName);
        for (Interface anInterface : wsdlProject.getInterfaceList()) {
            for (Operation operation : anInterface.getOperationList()) {
                if (elementName.equals(getDefaultRootElement(operation, messageType))) {
                    return operation.getName();
                }
            }
        }
        throw new IllegalStateException(String.format("No wsdl such operation with elementName: [%s]", elementName));
    }

    public String getDefaultRootElement(Operation operation, MessageType messageType) {
        if (messageType == MessageType.RQ) {
            try {
                return ((WsdlOperation) operation).getRequestBodyElementQName().getLocalPart();
            } catch (Exception e) {
                throw new MessageValidationException(e.getMessage());
            }
        }
        if (messageType == MessageType.RS) {
            try {
                return ((WsdlOperation) operation).getResponseBodyElementQName().getLocalPart();
            } catch (Exception e) {
                throw new MessageValidationException(e.getMessage());
            }
        }
        throw new IllegalStateException("No such message type handling: " + messageType);
    }
}
