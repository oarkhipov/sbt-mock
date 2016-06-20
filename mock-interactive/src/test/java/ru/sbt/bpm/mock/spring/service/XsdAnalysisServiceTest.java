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

import static org.testng.Assert.assertEquals;

/**
 * Created by sbt-hodakovskiy-da on 14.06.2016.
 */

@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-test.xml"})
@WebAppConfiguration("/mock-interactive/src/main/webapp")
public class XsdAnalysisServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	DataFileService dataFileService;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	MockConfigContainer container;

	@Autowired
	XsdAnalysisService xsdAnalysisService;

	@Test
	public void getXsdByxPathTest() {
		assertEquals(assertXsdTestByxPath(), 0);
	}

	@Test
	public  void getXsdByRegExpTest() {
		assertEquals(assertXsdByRegExp(), 0);
	}

	private int assertXsdByRegExp () {
		int assertSuccess = 0;
		try {
			log.info("==================================================");
			log.info("");
			log.info("               GETTING XSD NAMESPACES");
			log.info("");
			log.info("==================================================");
			for (String s : xsdAnalysisService.getNamespaceFromXSDByRegExp()) {
				log.info(s);
			}
		} catch (IOException e) {
			assertSuccess++;
			log.error("Unable to get xsd schema! ", e);
		}
		return assertSuccess;
	}


	private int assertXsdTestByxPath () {
		int assertSuccess = 0;
		try {
			log.info("==================================================");
			log.info("");
			log.info("               GETTING XSD NAMESPACES");
			log.info("");
			log.info("==================================================");
			for (String s : xsdAnalysisService.getNamespaceFromXSDByxPath()) {
				log.info(s);
			}
		} catch (IOException | SaxonApiException e) {
			assertSuccess++;
			log.error("Unable to get xsd schema! ", e);
		}
		return assertSuccess;
	}
}
