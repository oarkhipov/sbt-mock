package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;

import static org.testng.Assert.assertEquals;

/**
 * Created by sbt-hodakovskiy-da on 20.06.2016.
 */
@Slf4j
@ContextConfiguration({ "/env/mockapp-servlet-test-xsd-services.xml" })
public class XsdElementsAnalysisServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	DataFileService dataFileService;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	MockConfigContainer container;

	@Autowired
	XsdAnalysisService xsdAnalysisService;

	@Autowired
	XsdElementsAnalysisService elementsAnalysisService;

	@Test
	public void test () {
		assertEquals(assertElements(), 0);
	}

	private int assertElements () {
		int assertFail = 0;

		log.info("==================================================");
		log.info("");
		log.info("               GETTING XSD ELEMENTS");
		log.info("");
		log.info("==================================================");

		for (System system : container.getConfig().getSystems().getSystems()) {
			log.info(String.format("System name: %s", system.getSystemName()));
			for (String namespace : xsdAnalysisService.getNamespaceFromXsdByXpathForSystem(system.getSystemName())) {
				log.info(String.format("Namespace name: %s", namespace));
				if (elementsAnalysisService.getElementsForNamespace(system.getSystemName(), namespace) != null)
					for (String element : elementsAnalysisService.getElementsForNamespace(system.getSystemName(), namespace)) {
						log.info(String.format("Element: %s", element));
					}
			}
		}
		return assertFail;
	}
}
