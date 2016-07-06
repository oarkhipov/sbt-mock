package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Beans;
import org.testng.annotations.Test;
import reactor.tuple.Tuple;

import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */

public class BeansConstructorTest extends AbstractConfigGenerator {

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

		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
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
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithClassName() throws JAXBException {
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
		                  + ".org/2001/XMLSchema-instance\"><bean class=\"java.lang.String\"/></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String");
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithClassNameAndId() throws JAXBException {
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
		                  + ".org/2001/XMLSchema-instance\"><bean class=\"java.lang.String\" "
		                  + "id=\"connectionFactoryString\"/></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString");
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithOneConstructorArgs() throws JAXBException {
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
		                  + ".org/2001/XMLSchema-instance\"><bean class=\"java.lang.String\" "
		                  + "id=\"connectionFactoryString\"><constructor-arg value=\"jms/Q.APKKB\"/></bean></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString", Arrays.asList(Tuple.of("jms/Q.APKKB", "")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithMultiConstructorArgs() throws JAXBException {
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
		                  + ".org/2001/XMLSchema-instance\"><bean class=\"java.lang.String\" "
		                  + "id=\"connectionFactoryString\"><constructor-arg value=\"jms/Q.APKKB\"/><constructor-arg "
		                  + "value=\"jms/ESB.APKKB.OUT\"/><constructor-arg value=\"jms/ESB.APKKB.IN\"/></bean></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString", Arrays.asList(Tuple.of("jms/Q.APKKB", ""), Tuple.of("jms/ESB.APKKB.OUT", ""), Tuple.of("jms/ESB.APKKB.IN", "")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithMultiConstructorArgsTypes() throws JAXBException {
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
		                  + ".org/2001/XMLSchema-instance\"><bean class=\"java.lang.String\"><constructor-arg "
		                  + "type=\"java.lang.String\" value=\"jms/Q.APKKB\"/><constructor-arg type=\"java.lang"
		                  + ".String\" value=\"jms/ESB.APKKB.OUT\"/><constructor-arg type=\"java.lang.String\" "
		                  + "value=\"jms/ESB.APKKB.IN\"/></bean></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "", Arrays.asList(Tuple.of("jms/Q.APKKB", "java.lang.String"), Tuple.of("jms/ESB.APKKB.OUT", "java.lang.String"), Tuple.of("jms/ESB.APKKB.IN", "java.lang.String")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithClassAndConstructorAgr() throws JAXBException {
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
		                  + ".org/2001/XMLSchema-instance\"><bean class=\"java.lang.String\"><constructor-arg "
		                  + "type=\"java.lang.String\" value=\"jms/Q.APKKB\"/><constructor-arg type=\"java.lang"
		                  + ".String\" value=\"jms/ESB.APKKB.OUT\"/><constructor-arg type=\"java.lang.String\" "
		                  + "value=\"jms/ESB.APKKB.IN\"/></bean></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", Arrays.asList(Tuple.of("jms/Q.APKKB", "java.lang.String"), Tuple.of("jms/ESB.APKKB.OUT", "java.lang.String"), Tuple.of("jms/ESB.APKKB.IN", "java.lang.String")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithProperty() throws JAXBException {
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
		                  + ".org/2001/XMLSchema-instance\"><bean class=\"java.lang.String\" "
		                  + "id=\"connectionFactoryString\"><property name=\"prop1\" value=\"value1\"/></bean></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString", new HashMap<String,
				String>() {
			{
				put("prop1", "value1");
			}
		});
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithProperties() throws JAXBException {
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
		                  + ".org/2001/XMLSchema-instance\"><bean class=\"java.lang.String\" "
		                  + "id=\"connectionFactoryString\"><property name=\"prop1\" value=\"value1\"/>"
		                  + "<property name=\"prop2\" value=\"value2\"/><property name=\"prop3\" value=\"value3\"/></bean></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString", new LinkedHashMap<String,
						String>() {
			{
				put("prop1", "value1");
				put("prop2", "value2");
				put("prop3", "value3");
			}
		});
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}
}
