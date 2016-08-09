package ru.sbt.bpm.mock.product.ready;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.mocked.service.TestMessageValidationService;
import ru.sbt.bpm.mock.spring.service.XmlGeneratorService;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;

import static org.testng.Assert.assertTrue;

/**
 * Created by sbt-hodakovskiy-da on 03.06.2016.
 */

@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-test.xml"})
@WebAppConfiguration
public class GeneratorTest extends AbstractTestNGSpringContextTests {

    @Autowired
    MockConfigContainer container;

    @Autowired
    XmlGeneratorService generatorService;

    @Autowired
    MessageValidationService messageValidationService;

    @Test
    public void testGenerateAndValidateRsMessage() {
        boolean assertSuccess = true;
        for (ru.sbt.bpm.mock.config.entities.System system : container.getConfig().getSystems().getSystems())
            for (String intPointName : system.getIntegrationPointNames()) {
                try {
                    log.info("===============================================================================================");
                    log.info("");
                    log.info(String.format("                        Generate RS XML message for integration point: [%s] of system: [%s]", intPointName, system.getSystemName()));
                    log.info("");
                    log.info("===============================================================================================");
                    String xmlMessage = generatorService.generate(system.getSystemName(), intPointName, MessageType.RS, false);
                    log.info(String.format("XML Message: \n[%s]", xmlMessage));


                    log.info("===============================================================================================");
                    log.info("");
                    log.info(String.format("                         INIT SYSTEM: [%s]", system.getSystemName()));
                    log.info("");
                    log.info("===============================================================================================");
                    messageValidationService.initValidator(system);
                    messageValidationService.assertMessageElementName(xmlMessage, system.getSystemName(), intPointName, MessageType.RS);
                    messageValidationService.validate(xmlMessage, system.getSystemName());
                } catch (Exception e) {
                    assertSuccess = false;
                    e.printStackTrace();
                }
                assertTrue(assertSuccess);
            }
    }

}
