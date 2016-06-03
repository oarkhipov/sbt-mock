package ru.sbt.bpm.mock.product.ready;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
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
public class DataFileTest extends AbstractTestNGSpringContextTests {

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
			try {
				log.info("===============================================================================================");
				log.info("");
				log.info(String.format("            DRIVER                      "));
				log.info("");
				log.info("===============================================================================================");
				for (IntegrationPoint point : system.getDriverIntegrationPoints()) {
						log.info("Integration point: " + point.getName());
						String currentMessage = dataFileService.getCurrentMessage(system.getSystemName(), point.getName());
						log.info("Current message: " + currentMessage);
						String currentScript = dataFileService.getCurrentScript(system.getSystemName(), point.getName());
						log.info("Current script: " + currentScript);

	//					groovyService.execute();
				}


				log.info("===============================================================================================");
				log.info("");
				log.info(String.format("            MOCK                      "));
				log.info("");
				log.info("===============================================================================================");
				for (IntegrationPoint point : system.getMockIntegrationPoints()) {
					log.info("Integration point: " + point.getName());
					String currentMessage = dataFileService.getCurrentMessage(system.getSystemName(), point.getName());
					log.info("Current message: " + currentMessage);
					String currentScript = dataFileService.getCurrentScript(system.getSystemName(), point.getName());
					log.info("Current script: " + currentScript);
					String currentTest = dataFileService.getCurrentTest(system.getSystemName(), point.getName());
					log.info("Current test: " + currentTest);


				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}



		assertTrue(assertSuccess);
	}
}
