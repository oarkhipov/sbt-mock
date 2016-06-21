package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.MockConfigContainer;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;

/**
 * Created by sbt-hodakovskiy-da on 20.06.2016.
 */
@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-test.xml"})
@WebAppConfiguration("/mock-interactive/src/main/webapp")
public class XsdElementsAnalysisServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	DataFileService dataFileService;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	MockConfigContainer container;

	@Autowired
	XsdElementsAnalysisService elementsAnalysisService;

	@Test
	public void test() {
		assertEquals(assertElements(), 0);
	}

	private int assertElements () {
		int assertFail = 0;
		try {
			log.info("==================================================");
			log.info("");
			log.info("               GETTING XSD ELEMENTS");
			log.info("");
			log.info("==================================================");
//			for (String namespace : elementsAnalysisService.getElements().keySet())
//				log.info("Namespace: " +  namespace + ", elements: " +  elementsAnalysisService.getElements().get(namespace).toString());
			Map<String, Set<String>> elements = elementsAnalysisService.getElements();
			for (String namespace : elements.keySet()) {
				log.info("Namespace: " +  namespace + ", elements: " +  elements.get(namespace));
			}
		} catch (IOException | SaxonApiException e) {
			log.error("Unable to get elements! ", e);
			assertFail++;
		}
		return assertFail;
	}
}
