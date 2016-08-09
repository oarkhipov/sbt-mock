package ru.sbt.bpm.mock.spring.service.message.validation;

import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.WsdlContentPart;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlTestSuite;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockService;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.registry.WsdlTestRequestStepFactory;
import com.eviware.soapui.model.iface.Interface;
import com.eviware.soapui.model.iface.Operation;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import ru.sbt.bpm.mock.spring.utils.XmlUtils;

import javax.annotation.PostConstruct;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
    @Autowired
    private JMSValidationService jmsMessageValidationService;
    @Autowired
    private SOAPValidationService soapValidationService;

    private Map<String, MessageValidator> validator = new HashMap<String, MessageValidator>();


    @PostConstruct
    protected void init() throws IOException, SAXException {

        /*
         * FileResourceResolver добавлен для корректной работы с XSD с одиннаковыми именами, но в разных директориях
         */
        Systems systemsContainer = configContainer.getConfig().getSystems();
        if (systemsContainer != null) {
            Set<System> systems = systemsContainer.getSystems();
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
    public void initValidator(System system) throws IOException, SAXException {
        String systemName = system.getSystemName();
        File systemXsdDirectory = dataFileService.getSystemXsdDirectoryFile(systemName);
        String remoteRootSchema = system.getRemoteRootSchema();
        String localRootSchema = system.getLocalRootSchema();
        Protocol protocol = system.getProtocol();
        String remoteSchemaInLowerCase = remoteRootSchema.toLowerCase();
        log.info(String.format("Init [%s] validator for system [%s]", protocol, systemName));
        switch (protocol) {
            case JMS:
                jmsMessageValidationService.init(validator, system, systemName, systemXsdDirectory, remoteRootSchema, remoteSchemaInLowerCase);
                break;
            case SOAP:
                soapValidationService.init(validator, system, systemName, remoteRootSchema, localRootSchema, remoteSchemaInLowerCase);
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
     * @throws SaxonApiException if xml cannot be parsed
     */
    public boolean assertMessageElementName(String xml, String systemName, String integrationPointName, MessageType messageType) throws SaxonApiException, XmlException, MessageValidationException {
        System system = configContainer.getSystemByName(systemName);
        IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);
        return assertMessageElementName(xml, system, integrationPoint, messageType);
    }

    public boolean assertMessageElementName(String xml, System system, IntegrationPoint integrationPoint, MessageType messageType) throws SaxonApiException, XmlException, MessageValidationException {
        Protocol protocol = system.getProtocol();
        log.info(String.format("Assert xml [%s] for system: [%s]", protocol, system.getSystemName()));
        if (protocol == Protocol.JMS) {
            return jmsMessageValidationService.assertByXpath(xml, system, integrationPoint, messageType);
        }
        if (protocol == Protocol.SOAP) {
            return soapValidationService.assertByOperation(xml, system, integrationPoint, messageType);
        }
        throw new IllegalStateException("Unknown protocol: " + messageType.name());
    }

    /**
     * reInitialize validator by it's system name if xsd data was changed
     *
     * @param systemName name of system to re-init
     * @throws IOException
     * @throws SAXException
     */
    public void reInitValidator(String systemName) throws IOException, SAXException {
        removeValidator(systemName);
        System system = configContainer.getConfig().getSystems().getSystemByName(systemName);
        initValidator(system);
    }

    public void removeValidator(String systemName) {
        validator.remove(systemName);
    }


}
