package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import reactor.tuple.Tuple;
import ru.sbt.bpm.mock.context.generator.service.constructors.BeansConstructor;

import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */

public class BeansConstructorTest extends AbstractConfigGenerator {

	@Autowired
	BeansConstructor beansConstructor;

	@Test
	public void testCreateBeans () throws JAXBException {
		Beans beans = beansConstructor.createBeans();

		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><beans xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		                  + "xmlns:jms=\"http://www.springframework.org/schema/integration/jms\" "
		                  + "xmlns:int=\"http://www.springframework.org/schema/integration\" "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd         http://www.springframework"
		                  + ".org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd         http://www.springframework"
		                  + ".org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\"/>";

		compareResults(expected, beans, "BeansConstructorTest :: testCreateBeans");
	}

	@Test
	public void testCreateImports() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><beans xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		                  + "xmlns:jms=\"http://www.springframework.org/schema/integration/jms\" "
		                  + "xmlns:int=\"http://www.springframework.org/schema/integration\" "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd         http://www.springframework"
		                  + ".org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd         http://www.springframework"
		                  + ".org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\">\n"
		                  + "<import resource=\"../contextConfigs/base-config.xml\"/>\n"
		                  + "<import resource=\"../contextConfigs/logging-config.xml\"/>\n"
		                  + "<import resource=\"../contextConfigs/test-import1.xml\"/>\n"
		                  + "</beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createImports(beans, Arrays.asList("../contextConfigs/base-config.xml",
		                                                            "../contextConfigs/logging-config.xml",
		                                                            "../contextConfigs/test-import1.xml"));
		compareResults(expected, beans, "BeansConstructorTest :: testCreateImports");
	}

	@Test
	public void testCreateBeanWithClassName() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><beans xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		                  + "xmlns:jms=\"http://www.springframework.org/schema/integration/jms\" "
		                  + "xmlns:int=\"http://www.springframework.org/schema/integration\" "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd         http://www.springframework"
		                  + ".org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd         http://www.springframework"
		                  + ".org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\">\n"
		                  + "<bean class=\"java.lang.String\"/>\n"
		                  + "</beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String");
		compareResults(expected, beans, "BeansConstructorTest :: testCreateBeanWithClassName");
	}

	@Test
	public void testCreateBeanWithClassNameAndId() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><beans xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		                  + "xmlns:jms=\"http://www.springframework.org/schema/integration/jms\" "
		                  + "xmlns:int=\"http://www.springframework.org/schema/integration\" "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd         http://www.springframework"
		                  + ".org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd         http://www.springframework"
		                  + ".org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\">\n"
		                  + "<bean class=\"java.lang.String\" id=\"connectionFactoryString\"/>\n"
		                  + "</beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString");
		compareResults(expected, beans, "BeansConstructorTest :: testCreateBeanWithClassNameAndId");
	}

	@Test
	public void testCreateBeanWithOneConstructorArgs() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><beans xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		                  + "xmlns:jms=\"http://www.springframework.org/schema/integration/jms\" "
		                  + "xmlns:int=\"http://www.springframework.org/schema/integration\" "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd         http://www.springframework"
		                  + ".org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd         http://www.springframework"
		                  + ".org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\">\n"
		                  + "<bean class=\"java.lang.String\" id=\"connectionFactoryString\">\n"
		                  + "<constructor-arg value=\"jms/Q.APKKB\"/>\n"
		                  + "</bean>\n"
		                  + "</beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString", Arrays.asList(Tuple.of("jms/Q.APKKB", "")));
		compareResults(expected, beans, "BeansConstructorTest :: testCreateBeanWithOneConstructorArgs");
	}

	@Test
	public void testCreateBeanWithMultiConstructorArgs() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><beans xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		                  + "xmlns:jms=\"http://www.springframework.org/schema/integration/jms\" "
		                  + "xmlns:int=\"http://www.springframework.org/schema/integration\" "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd         http://www.springframework"
		                  + ".org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd         http://www.springframework"
		                  + ".org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\">\n"
		                  + "<bean class=\"java.lang.String\" id=\"connectionFactoryString\">\n"
		                  + "<constructor-arg value=\"jms/Q.APKKB\"/>\n"
		                  + "<constructor-arg value=\"jms/ESB.APKKB.OUT\"/>\n"
		                  + "<constructor-arg value=\"jms/ESB.APKKB.IN\"/>\n"
		                  + "</bean>\n"
		                  + "</beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString", Arrays.asList(Tuple.of("jms/Q.APKKB", ""), Tuple.of("jms/ESB.APKKB.OUT", ""), Tuple.of("jms/ESB.APKKB.IN", "")));
		compareResults(expected, beans, "BeansConstructorTest :: testCreateBeanWithMultiConstructorArgs");
	}

	@Test
	public void testCreateBeanWithMultiConstructorArgsTypes() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><beans xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		                  + "xmlns:jms=\"http://www.springframework.org/schema/integration/jms\" "
		                  + "xmlns:int=\"http://www.springframework.org/schema/integration\" "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd         http://www.springframework"
		                  + ".org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd         http://www.springframework"
		                  + ".org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\">\n"
		                  + "<bean class=\"java.lang.String\">\n"
		                  + "<constructor-arg type=\"java.lang.String\" value=\"jms/Q.APKKB\"/>\n"
		                  + "<constructor-arg type=\"java.lang.String\" value=\"jms/ESB.APKKB.OUT\"/>\n"
		                  + "<constructor-arg type=\"java.lang.String\" value=\"jms/ESB.APKKB.IN\"/>\n"
		                  + "</bean>\n"
		                  + "</beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "", Arrays.asList(Tuple.of("jms/Q.APKKB", "java.lang.String"), Tuple.of("jms/ESB.APKKB.OUT", "java.lang.String"), Tuple.of("jms/ESB.APKKB.IN", "java.lang.String")));
		compareResults(expected, beans, "BeansConstructorTest :: testCreateBeanWithMultiConstructorArgsTypes");
	}

	@Test
	public void testCreateBeanWithClassAndConstructorAgr() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><beans xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		                  + "xmlns:jms=\"http://www.springframework.org/schema/integration/jms\" "
		                  + "xmlns:int=\"http://www.springframework.org/schema/integration\" "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd         http://www.springframework"
		                  + ".org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd         http://www.springframework"
		                  + ".org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\">\n"
		                  + "<bean class=\"java.lang.String\">\n"
		                  + "<constructor-arg type=\"java.lang.String\" value=\"jms/Q.APKKB\"/>\n"
		                  + "<constructor-arg type=\"java.lang.String\" value=\"jms/ESB.APKKB.OUT\"/>\n"
		                  + "<constructor-arg type=\"java.lang.String\" value=\"jms/ESB.APKKB.IN\"/>\n"
		                  + "</bean>\n"
		                  + "</beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", Arrays.asList(Tuple.of("jms/Q.APKKB", "java.lang.String"), Tuple.of("jms/ESB.APKKB.OUT", "java.lang.String"), Tuple.of("jms/ESB.APKKB.IN", "java.lang.String")));
		compareResults(expected, beans, "BeansConstructorTest :: testCreateBeanWithClassAndConstructorAgr");
	}

	@Test
	public void testCreateBeanWithProperty() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><beans xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		                  + "xmlns:jms=\"http://www.springframework.org/schema/integration/jms\" "
		                  + "xmlns:int=\"http://www.springframework.org/schema/integration\" "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd         http://www.springframework"
		                  + ".org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd         http://www.springframework"
		                  + ".org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\">\n"
		                  + "<bean class=\"java.lang.String\" id=\"connectionFactoryString\">\n"
		                  + "<property name=\"prop1\" value=\"value1\"/>\n"
		                  + "</bean>\n"
		                  + "</beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString", new HashMap<String,
				String>() {
			{
				put("prop1", "value1");
			}
		});
		compareResults(expected, beans, "BeansConstructorTest :: testCreateBeanWithProperty");
	}

	@Test
	public void testCreateBeanWithProperties() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><beans xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		                  + "xmlns:jms=\"http://www.springframework.org/schema/integration/jms\" "
		                  + "xmlns:int=\"http://www.springframework.org/schema/integration\" "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd         http://www.springframework"
		                  + ".org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd         http://www.springframework"
		                  + ".org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\">\n"
		                  + "<bean class=\"java.lang.String\" id=\"connectionFactoryString\">\n"
		                  + "<property name=\"prop1\" value=\"value1\"/>\n"
		                  + "<property name=\"prop2\" value=\"value2\"/>\n"
		                  + "<property name=\"prop3\" value=\"value3\"/>\n"
		                  + "</bean>\n"
		                  + "</beans>";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString", new LinkedHashMap<String,
						String>() {
			{
				put("prop1", "value1");
				put("prop2", "value2");
				put("prop3", "value3");
			}
		});
		compareResults(expected, beans, "BeansConstructorTest :: testCreateBeanWithProperties");
	}
}
