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


/**
 * @author SBT-Bochev-AS on 26.05.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
@WebAppConfiguration("mock-interactive/src/main/webapp")
@ContextConfiguration({"/env/mockapp-servlet-test.xml"})
public class ValidatorTest extends AbstractTestNGSpringContextTests {

	@Autowired
	MockConfigContainer container;

	@Autowired
	TestMessageValidationService validator;

	@Test
	public void testInit () throws IOException, SAXException {
		for (ru.sbt.bpm.mock.config.entities.System system : container.getConfig().getSystems().getSystems()) {
			validator.initValidator(system);
		}
	}
}
