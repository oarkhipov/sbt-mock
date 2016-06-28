package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.MockConfigContainer;

import java.util.Set;

import static org.testng.Assert.assertEquals;

/**
 * Created by sbt-hodakovskiy-da on 14.06.2016.
 */

@Slf4j
@ContextConfiguration({ "/env/mockapp-servlet-test-xsd-services.xml" })
public class XsdAnalysisServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	MockConfigContainer configContainer;

	@Autowired
	XsdAnalysisService xsdAnalysisService;

	@Test
	public void getXsdByxPathTest () {
		assertEquals(assertXsdTestByxPath(), 0);
	}

	@Test
	public void getXsdByRegExpTest () {
		assertEquals(assertXsdByRegExp(), 0);
	}

	private int assertXsdByRegExp () {
		int assertSuccess = 0;
		log.info("==================================================");
		log.info("");
		log.info("               GETTING XSD NAMESPACES");
		log.info("");
		log.info("==================================================");
		for (ru.sbt.bpm.mock.config.entities.System system : configContainer.getConfig().getSystems().getSystems()) {
			String systemName = system.getSystemName();
			log.info("Init system: " + systemName);
			Set<String> setXsdNamespace = xsdAnalysisService.getNamespaceFromXsdByRegExpForSystem(systemName);
			for (String namespace : setXsdNamespace)
				log.info(String.format("Namespace: %s", namespace));
		}
		return assertSuccess;
	}


	private int assertXsdTestByxPath () {
		int assertSuccess = 0;
		log.info("==================================================");
		log.info("");
		log.info("               GETTING XSD NAMESPACES");
		log.info("");
		log.info("==================================================");
		for (ru.sbt.bpm.mock.config.entities.System system : configContainer.getConfig().getSystems().getSystems()) {
			String systemName = system.getSystemName();
			log.info("Init system: " + systemName);
			Set<String> setXsdNamespace = xsdAnalysisService.getNamespaceFromXsdByXpathForSystem(systemName);
			for (String namespace : setXsdNamespace)
				log.info(String.format("Namespace: %s", namespace));
		}
		return assertSuccess;
	}
}
