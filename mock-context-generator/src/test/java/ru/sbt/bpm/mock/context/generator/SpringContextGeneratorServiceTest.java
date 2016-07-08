package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.context.generator.service.constructors.BeansConstructor;
import ru.sbt.bpm.mock.context.generator.service.SpringContextGeneratorService;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.UnsupportedEncodingException;

/**
 * Created by sbt-hodakovskiy-da on 08.07.2016.
 */
@ContextConfiguration({ "/env/mockapp-servlet.xml" })
public class SpringContextGeneratorServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	SpringContextGeneratorService generator;

	BeansConstructor beansConstructor = new BeansConstructor();

	@Test
	public void testCreateBeans () throws JAXBException, UnsupportedEncodingException, TransformerException,
	                                      XMLStreamException {
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "className", "something");
		System.out.println(generator.toXml(beans));
	}
}
