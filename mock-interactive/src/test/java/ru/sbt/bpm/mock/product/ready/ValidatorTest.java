package ru.sbt.bpm.mock.product.ready;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.config.MockConfigContainer;

import java.io.File;
import java.io.IOException;


/**
 * @author SBT-Bochev-AS on 26.05.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class ValidatorTest extends AbstractTestNGSpringContextTests {

	@Autowired
	TestMessageValidationService validator;

	private String getPath() throws IOException {
		return new File(".").getCanonicalPath();
//		URL resource = this.getClass().getClassLoader().getResource("");
//		assert resource != null;
//		return resource.getFile();
	}


	@Test
	public void testInit () throws IOException, SAXException {
		MockConfigContainer container = MockConfigContainer.getInstance(getPath() + File.separator + "mock-interactive" + File.separator
		                                                                + "src" + File.separator + "test" + File.separator
		                                                                + "resources" + File.separator + "env" + File.separator + "MockConfig.xml");
		container.init();



		for (ru.sbt.bpm.mock.config.entities.System system : container.getConfig().getSystems().getSystems()) {
			validator.initValidator(system);
			log.info("Init validator for system: \"" + system.getSystemName() + "\"");
		}


	}

}
