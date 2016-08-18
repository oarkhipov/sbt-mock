package ru.sbt.bpm.mock.product.ready;

import ru.sbt.bpm.mock.config.entities.System;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.GroovyService;
import ru.sbt.bpm.mock.spring.service.XmlGeneratorService;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

/**
 * Created by sbt-hodakovskiy-da on 03.06.2016.
 */

@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-test.xml"})
@WebAppConfiguration("classpath:.")
public class DataTest extends AbstractTestNGSpringContextTests {

    @Autowired
    MockConfigContainer container;

    @Autowired
    DataFileService dataFileService;

    @Autowired
    MessageValidationService messageValidationService;

    @Autowired
    XmlGeneratorService xmlGeneratorService;

    @Autowired
    GroovyService groovyService;

    @Test(enabled = false)
    public void tesDataFiles() {
        assertEquals(validateAllSystems(), 0);
    }

    private int validateAllSystems() {
        int assertionErrors = 0;
        for (System system : container.getConfig().getSystems().getSystems()) {
            String systemName = system.getSystemName();

            log.info("=========================================================================================");
            log.info("");
            log.info("								INIT SYSTEM: [" + systemName + "]");
            log.info("");
            log.info("=========================================================================================");

            try {
                messageValidationService.initValidator(system);
            } catch (IOException e) {
                log.error("Unable to initialize validator for system " + systemName + "!", e);
            } catch (SAXException e) {
                log.error("Unable to initialize validator for system " + systemName + "!", e);
            }

            for (IntegrationPoint intPoint : system.getIntegrationPoints().getIntegrationPoints()) {
                try {
                    if (intPoint.isDriver()) {
                        validateDriver(system, intPoint);
                    }

                    if (intPoint.isMock()) {
                        validateMock(system, intPoint);
                    }
                } catch (Exception e) {
                    assertionErrors++;
                    log.error("Unable to validate integration point " + intPoint.getName() + "!", e);
                }
            }
        }
        return assertionErrors;
    }

    private void validateMock(System system, IntegrationPoint intPoint) throws Exception {
        String systemName = system.getSystemName();
        String integrationPointName = intPoint.getName();

        log.info("===============================================================================================");
        log.info("");
        log.info("                                INTEGRATION POINT MOCK [" + integrationPointName + "]");
        log.info("");
        log.info("===============================================================================================");

        String currentMessage = dataFileService.getDefaultMessage(systemName, integrationPointName);
        log.info("Current message: " + currentMessage);
        String currentScript = dataFileService.getDefaultScript(systemName, integrationPointName);
        log.info("Current script: " + currentScript);
        String currentTest = dataFileService.getDefaultTest(systemName, integrationPointName);
        log.info("Current test: " + currentTest);

        currentScript = isEmpty(currentScript);
        currentTest = isEmpty(currentTest);

        String message = groovyService.execute(currentTest, currentMessage, currentScript);
        messageValidationService.assertMessageElementName(message, systemName, integrationPointName, MessageType.RS);
        messageValidationService.validate(message, systemName);
    }

    private void validateDriver(System system, IntegrationPoint intPoint) throws Exception {
        String integrationPointName = intPoint.getName();
        String systemName = system.getSystemName();

        log.info("===============================================================================================");
        log.info("");
        log.info("                                 INTEGRATION POINT DRIVER  [" + integrationPointName + "]");
        log.info("");
        log.info("===============================================================================================");

        String currentMessage = dataFileService.getDefaultMessage(systemName, integrationPointName);
        log.info("Current message: " + currentMessage);
        String currentScript = dataFileService.getDefaultScript(systemName, integrationPointName);
        log.info("Current script: " + currentScript);

        currentScript = isEmpty(currentScript);

        if (!currentMessage.isEmpty()) {
            String message = groovyService.execute("", currentMessage, currentScript);
            messageValidationService.assertMessageElementName(message, systemName, integrationPointName, MessageType.RQ);
            messageValidationService.validate(message, systemName);
        }
    }

    private String isEmpty(String string) {
        if (string == null || string.isEmpty())
            return "";
        return string;
    }
}
