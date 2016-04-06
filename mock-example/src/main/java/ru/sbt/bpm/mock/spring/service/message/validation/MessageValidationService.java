package ru.sbt.bpm.mock.spring.service.message.validation;

import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlTestSuite;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.registry.WsdlTestRequestStepFactory;
import com.eviware.soapui.model.iface.Operation;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.Protocol;
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

        for (System system : configContainer.getConfig().getSystems().getSystems()) {
            initValidator(system);
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
                configContainer.getWsdlProjectMap().put(system, wsdlProject);
                //create testsuite
                WsdlTestSuite testSuite = wsdlProject.addNewTestSuite("TestSuite");
                WsdlTestCase testCase = testSuite.addNewTestCase("TestCase");
                //init test steps for all operations
                for (Operation operation : wsdlProject.getInterfaceList().get(0).getOperationList()) {
                    WsdlOperation wsdlOperation = (WsdlOperation) operation;
                    TestStepConfig testStepConfig = WsdlTestRequestStepFactory.createConfig(wsdlOperation, wsdlOperation.getName());
                    testCase.addTestStep(testStepConfig);
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
     * @param xml    спец имя xml
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
     * @return true if one or more elements match the xpath
     * @throws XPathExpressionException
     * @throws SaxonApiException
     */
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
