package ru.sbt.bpm.mock.spring.context.generator;

import generated.springframework.beans.Beans;
import lombok.extern.slf4j.Slf4j;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.spring.context.generator.service.SpringContextGeneratorService;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

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
		log.info("Class and method name: " + classAndMethodName);
		log.info("Generated config: " + actual);
		log.debug("");
		log.debug("==============================================");
	}

	protected void compareResults (String expected, Beans beans, String classAndMethodName) throws JAXBException {
		String actual = generator.toXml(beans).replaceAll("&#10;","");
		printActual(actual, classAndMethodName);
		String actualSting = compactXml(actual);
		String expectedString = compactXml(expected);
		try {
			Diff diff = new Diff(actualSting, expectedString);
			XMLUnit.setIgnoreWhitespace(true);
			XMLUnit.setIgnoreComments(true);
			if (!diff.similar()) {
				assertEquals(expected, actual);
			}
		} catch (SAXException e ) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String compactXml(String xml) {
		BufferedReader bufferedReader = new BufferedReader(new StringReader(xml));
		String line;
		StringWriter   stringWriter = new StringWriter();

		try {
			while ((line = bufferedReader.readLine()) != null) {
				stringWriter.append(line.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		xml = stringWriter.toString();
		log.debug("Parsed xml: " + xml);
		return xml;
	}

}
