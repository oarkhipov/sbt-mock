package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Beans;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by sbt-hodakovskiy-da on 06.07.2016.
 */
public abstract class AbstractConfigGenerator {

	protected Marshaller createMarshaller(Class... classes) throws JAXBException {
		Marshaller marshaller = JAXBContext.newInstance(classes).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://www.springframework.org/schema/beans        http://www.springframework.org/schema/beans/spring-beans.xsd\n" +
		                                                       "        http://www.springframework.org/schema/integration    http://www.springframework.org/schema/integration/spring-integration.xsd\n" +
		                                                       "        http://www.springframework.org/schema/integration http://www.springframework.org/schema/integration/spring-integration.xsd\n" +
		                                                       "        http://www.springframework.org/schema/integration/jms http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd");
//		marshaller.setProperty(MockNamespaceMapper.MAPPER_NAMESPACE_PROPERTY, new MockNamespaceMapper());
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		return marshaller;
	}

	protected void printActual(String actual) {
		System.out.println("==============================================");
		System.out.println("");
		System.out.println("Generated config: " + actual);
		System.out.println("");
		System.out.println("==============================================");
	}

	protected int compareResults (String expected, Beans beans, Class... classes) throws JAXBException {
		Writer writer = new StringWriter();
		createMarshaller(classes).marshal(beans, writer);
		printActual(writer.toString());
		return expected.compareTo(writer.toString());
	}

}
