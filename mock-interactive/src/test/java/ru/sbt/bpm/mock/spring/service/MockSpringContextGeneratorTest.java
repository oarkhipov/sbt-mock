package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.context.generator.service.SpringContextGeneratorService;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.UnsupportedEncodingException;

/**
 * Created by sbt-hodakovskiy-da on 11.07.2016.
 */

@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-test-context-generator-services.xml"})
public class MockSpringContextGeneratorTest extends AbstractTestNGSpringContextTests {

	@Autowired
	MockSpringContextGeneratorService generator;

	@Autowired
	SpringContextGeneratorService generatorService;

	@Test
	public void testGettingData () throws JAXBException, UnsupportedEncodingException, TransformerException, XMLStreamException {
		log.info(generatorService.toXml(generator.generateContext()));
	}

}
