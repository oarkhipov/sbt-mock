package ru.sbt.bpm.mock.product.ready;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.config.MockConfigContainer;

import java.io.IOException;

import static org.testng.Assert.assertTrue;


/**
 * @author SBT-Bochev-AS on 26.05.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
@WebAppConfiguration("classpath:.")
@ContextConfiguration({"/env/mockapp-servlet-test.xml"})
public class ValidatorTest extends AbstractTestNGSpringContextTests {

	@Autowired
	MockConfigContainer container;

	@Autowired
	TestMessageValidationService validator;

	@Test
	public void testInit () throws IOException, SAXException {
		boolean assertSucceed = true;
		for (ru.sbt.bpm.mock.config.entities.System system : container.getConfig().getSystems().getSystems()) {
			try {
				log.info("=========================================================================================");
				log.info("");
				log.info("								INIT SYSTEM: " + system.getSystemName());
				log.info("");
				log.info("=========================================================================================");
				validator.initValidator(system);
			} catch (IOException e) {
				assertSucceed = false;
				log.error("Failed to init validator!", e);
			} catch (SAXException e) {
				assertSucceed = false;
				log.error("Failed to init validator!", e);
			}
		}
		assertTrue(assertSucceed,"One of validators did not initialized!!!!");
	}
}
