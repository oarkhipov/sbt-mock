package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import reactor.tuple.Tuple2;
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
	public void getXsdNamespaceTest () {
		assertEquals(assertXsdNamespaceTest(), 0);
	}

	@Test
	public void getElementsTest () {
		assertEquals(assertElementsTest(), 0);
	}

	@Test void getElementsForSystemTest() {
		assertEquals(assertElementsForSystemTest(), 0);
	}

	private int assertXsdNamespaceTest () {
		int assertSuccess = 0;
		log.debug("==================================================");
		log.debug("");
		log.debug("               GETTING XSD NAMESPACES");
		log.debug("");
		log.debug("==================================================");
		for (ru.sbt.bpm.mock.config.entities.System system : configContainer.getConfig().getSystems().getSystems()) {
			String systemName = system.getSystemName();
			log.debug("Init system: " + systemName);
			Set<String> setXsdNamespace = xsdAnalysisService.getNamespaceFromXsd(systemName);
			for (String namespace : setXsdNamespace)
				log.debug(String.format("Namespace: %s", namespace));
		}
		return assertSuccess;
	}

	private int assertElementsTest () {
		int assertFail = 0;

		log.debug("==================================================");
		log.debug("");
		log.debug("               GETTING XSD ELEMENTS");
		log.debug("");
		log.debug("==================================================");

		for (ru.sbt.bpm.mock.config.entities.System system : configContainer.getConfig().getSystems().getSystems()) {
			log.debug(String.format("System name: %s", system.getSystemName()));
			for (String namespace : xsdAnalysisService.getNamespaceFromXsd(system.getSystemName())) {
				log.debug(String.format("Namespace name: %s", namespace));
				if (xsdAnalysisService.getElementsForSystem(system.getSystemName(), namespace) != null)
					for (Tuple2<String, String> element : xsdAnalysisService.getElementsForSystem(system.getSystemName
							(), namespace))
						log.debug(String.format("Namespace: %s || Element: %s", element.getT1(), element.getT2()));
			}
		}
		return assertFail;
	}

	private int assertElementsForSystemTest() {
		int assertFail = 0;
		log.debug("==================================================");
		log.debug("");
		log.debug("               GETTING XSD ELEMENTS");
		log.debug("");
		log.debug("==================================================");
		for (ru.sbt.bpm.mock.config.entities.System system : configContainer.getConfig().getSystems().getSystems()) {
			log.debug(String.format("System name: %s", system.getSystemName()));
			for (Tuple2<String, String> element : xsdAnalysisService.getElementsForSystem(system.getSystemName()))
				log.debug(String.format("Namespace: %s || Element: %s", element.getT1(), element.getT2()));
		}
		return assertFail;
	}
}
