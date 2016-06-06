package ru.sbt.bpm.mock.product.ready;

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

import java.io.IOException;

import static org.testng.Assert.assertTrue;

/**
 * Created by sbt-hodakovskiy-da on 03.06.2016.
 */

@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-test.xml"})
@WebAppConfiguration("mock-interactive/src/main/webapp")
public class DataTest extends AbstractTestNGSpringContextTests {

	@Autowired
	MockConfigContainer container;

	@Autowired
	DataFileService dataFileService;

	@Autowired
	TestMessageValidationService messageValidationService;

	@Autowired
	XmlGeneratorService xmlGeneratorService;

	@Autowired
	GroovyService groovyService;

	@Test
	public void tesDataFiles() {
		boolean assertSuccess = true;
		for (ru.sbt.bpm.mock.config.entities.System system : container.getConfig().getSystems().getSystems()) {

				log.info("=========================================================================================");
				log.info("");
				log.info("								INIT SYSTEM: " + system.getSystemName());
				log.info("");
				log.info("=========================================================================================");

			try {
				messageValidationService.initValidator(system);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}


			for (IntegrationPoint intPoint : system.getIntegrationPoints().getIntegrationPoints()) {
					try {
						if (intPoint.isDriver()) {
							log.info("===============================================================================================");
							log.info("");
							log.info(String.format("           INTEGRATION POINT DRIVER                      "));
							log.info("");
							log.info("===============================================================================================");

							log.info("Integration point: " + intPoint.getName());
							String currentMessage = dataFileService.getCurrentMessage(system.getSystemName(), intPoint.getName());
							log.info("Current message: " + currentMessage);
							String currentScript = dataFileService.getCurrentScript(system.getSystemName(), intPoint.getName());

							log.info("Current script: " + currentScript);

							currentScript = isEmptyScript(currentScript);

							if (!currentMessage.isEmpty()) {
								String message = groovyService.execute("", currentMessage, currentScript);
								messageValidationService.assertMessageElementName(message, system.getSystemName(), intPoint.getName(), MessageType.RQ);
								messageValidationService.validate(message, system.getSystemName());
							}
						}

						if (intPoint.isMock()) {
							log.info("===============================================================================================");
							log.info("");
							log.info(String.format("           INTEGRATION POINT MOCK                      "));
							log.info("");
							log.info("===============================================================================================");

							log.info("Integration point: " + intPoint.getName());
							String currentMessage = dataFileService.getCurrentMessage(system.getSystemName(), intPoint.getName());
							log.info("Current message: " + currentMessage);
							String currentScript = dataFileService.getCurrentScript(system.getSystemName(), intPoint.getName());
							log.info("Current script: " + currentScript);
							String currentTest = dataFileService.getCurrentTest(system.getSystemName(), intPoint.getName());
							log.info("Current test: " + currentTest);

							currentScript = isEmptyScript(currentScript);
							currentTest = isEmptyMessage(currentTest);

							String message = groovyService.execute(currentTest, currentMessage, currentScript);
							messageValidationService.assertMessageElementName(message, system.getSystemName(), intPoint.getName(), MessageType.RS);
							messageValidationService.validate(message, system.getSystemName());
						}
					} catch (Exception e) {
						assertSuccess = false;
						e.printStackTrace();
					}
				}
		}

		assertTrue(assertSuccess);
	}

	private String isEmptyMessage (String string) {
		if (string.length() == 0 || string.isEmpty())
			return "";
		return string;
	}

	private String isEmptyScript (String string) {
		if (string.length() == 0 || string.isEmpty())
			return "";
		return string;
	}
}
