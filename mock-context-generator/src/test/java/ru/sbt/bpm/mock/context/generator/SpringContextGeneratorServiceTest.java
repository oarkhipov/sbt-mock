package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Beans;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.context.generator.service.SpringContextGeneratorService;
import ru.sbt.bpm.mock.context.generator.service.constructors.BeansConstructor;

import javax.xml.bind.JAXBException;

/**
 * Created by sbt-hodakovskiy-da on 08.07.2016.
 */
@Slf4j
@ContextConfiguration({ "/env/mockapp-servlet.xml" })
public class SpringContextGeneratorServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	SpringContextGeneratorService generator;;

	BeansConstructor beansConstructor = new BeansConstructor();

	@Test
	public void testCreateBeans () throws JAXBException {
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "className", "something");
		String actual = generator.toXml(beans);
		log.debug(actual);
	}
}
