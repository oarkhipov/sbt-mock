package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Beans;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */

public class BeansConstructorTest {

	BeansConstructor beansConstructor = new BeansConstructor();

	@Test
	public void testCreateBeans () throws JAXBException {
		Beans beans = beansConstructor.createBeans();

		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><beans "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3"
		                  + ".org/2001/XMLSchema-instance\"/>";

		assert compareResults(expected, beans) == 0;
	}

	@Test
	public void testCreateImports() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><beans "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3"
		                  + ".org/2001/XMLSchema-instance\"><import resource=\"../contextConfigs/base-config"
		                  + ".xml\"/><import resource=\"../contextConfigs/logging-config.xml\"/><import "
		                  + "resource=\"../contextConfigs/test-import1.xml\"/></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createImports(beans, Arrays.asList("../contextConfigs/base-config.xml",
		                                                                     "../contextConfigs/logging-config.xml",
		                                                                     "../contextConfigs/test-import1.xml"));
		assert compareResults(expected, beans) == 0;
	}

	private Marshaller createMarshaller(Class... classes) throws JAXBException {
		Marshaller marshaller = JAXBContext.newInstance(classes).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://www.springframework.org/schema/beans        http://www.springframework.org/schema/beans/spring-beans.xsd\n" +
		                                                       "        http://www.springframework.org/schema/integration    http://www.springframework.org/schema/integration/spring-integration.xsd\n" +
		                                                       "        http://www.springframework.org/schema/integration http://www.springframework.org/schema/integration/spring-integration.xsd\n" +
		                                                       "        http://www.springframework.org/schema/integration/jms http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd");
		return marshaller;
	}

	private void printActual(String actual) {
		System.out.println("==============================================");
		System.out.println("");
		System.out.println("Generated config: " + actual);
		System.out.println("");
		System.out.println("==============================================");
	}

	private int compareResults (String expected, Beans beans) throws JAXBException {Writer writer = new StringWriter();
		createMarshaller(beans.getClass()).marshal(beans, writer);
		printActual(writer.toString());
		return expected.compareTo(writer.toString());
	}
}
