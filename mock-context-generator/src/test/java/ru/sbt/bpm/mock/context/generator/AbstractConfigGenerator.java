package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Beans;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import ru.sbt.bpm.mock.context.generator.service.SpringContextGeneratorService;

import javax.xml.bind.JAXBException;

import static org.testng.Assert.assertEquals;

/**
 * Created by sbt-hodakovskiy-da on 06.07.2016.
 */
@Slf4j
@ContextConfiguration({ "/env/mockapp-servlet.xml" })
public abstract class AbstractConfigGenerator extends AbstractTestNGSpringContextTests {

	@Autowired
	SpringContextGeneratorService generator;

	protected void printActual(String actual, String classAndMethodName) {
		log.debug("==============================================");
		log.debug("");
		log.debug("Class and method name: " + classAndMethodName);
		log.debug("Generated config: " + actual);
		log.debug("");
		log.debug("==============================================");
	}

	protected void compareResults (String expected, Beans beans, String classAndMethodName) throws JAXBException {
		String actual = generator.toXml(beans);
		printActual(actual, classAndMethodName);
		assertEquals(actual.replaceAll("\\s", ""), expected.replaceAll("\\s", ""));
	}

}
