package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Beans;
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

	BeansConstructor beansConstructor = new BeansConstructor();

	@Test
	public void testCreateBeans () throws JAXBException {
		Beans beans = beansConstructor.createBeans();

		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
		                  + "<beans xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>\n";

		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateImports() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
		                  + "<beans xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
		                  + "    <import resource=\"../contextConfigs/base-config.xml\"/>\n"
		                  + "    <import resource=\"../contextConfigs/logging-config.xml\"/>\n"
		                  + "    <import resource=\"../contextConfigs/test-import1.xml\"/>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createImports(beans, Arrays.asList("../contextConfigs/base-config.xml",
		                                                            "../contextConfigs/logging-config.xml",
		                                                            "../contextConfigs/test-import1.xml"));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithClassName() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
		                  + "<beans xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
		                  + "    <bean class=\"java.lang.String\"/>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String");
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithClassNameAndId() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
		                  + "<beans xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
		                  + "    <bean class=\"java.lang.String\" id=\"connectionFactoryString\"/>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString");
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithOneConstructorArgs() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
		                  + "<beans xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
		                  + "    <bean class=\"java.lang.String\" id=\"connectionFactoryString\">\n"
		                  + "        <constructor-arg value=\"jms/Q.APKKB\"/>\n"
		                  + "    </bean>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString", Arrays.asList(Tuple.of("jms/Q.APKKB", "")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithMultiConstructorArgs() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
		                  + "<beans xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
		                  + "    <bean class=\"java.lang.String\" id=\"connectionFactoryString\">\n"
		                  + "        <constructor-arg value=\"jms/Q.APKKB\"/>\n"
		                  + "        <constructor-arg value=\"jms/ESB.APKKB.OUT\"/>\n"
		                  + "        <constructor-arg value=\"jms/ESB.APKKB.IN\"/>\n"
		                  + "    </bean>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "connectionFactoryString", Arrays.asList(Tuple.of("jms/Q.APKKB", ""), Tuple.of("jms/ESB.APKKB.OUT", ""), Tuple.of("jms/ESB.APKKB.IN", "")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithMultiConstructorArgsTypes() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
		                  + "<beans xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
		                  + "    <bean class=\"java.lang.String\">\n"
		                  + "        <constructor-arg type=\"java.lang.String\" value=\"jms/Q.APKKB\"/>\n"
		                  + "        <constructor-arg type=\"java.lang.String\" value=\"jms/ESB.APKKB.OUT\"/>\n"
		                  + "        <constructor-arg type=\"java.lang.String\" value=\"jms/ESB.APKKB.IN\"/>\n"
		                  + "    </bean>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", "", Arrays.asList(Tuple.of("jms/Q.APKKB", "java.lang.String"), Tuple.of("jms/ESB.APKKB.OUT", "java.lang.String"), Tuple.of("jms/ESB.APKKB.IN", "java.lang.String")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithClassAndConstructorAgr() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
		                  + "<beans xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
		                  + "    <bean class=\"java.lang.String\">\n"
		                  + "        <constructor-arg type=\"java.lang.String\" value=\"jms/Q.APKKB\"/>\n"
		                  + "        <constructor-arg type=\"java.lang.String\" value=\"jms/ESB.APKKB.OUT\"/>\n"
		                  + "        <constructor-arg type=\"java.lang.String\" value=\"jms/ESB.APKKB.IN\"/>\n"
		                  + "    </bean>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = beansConstructor.createBean(beans, "java.lang.String", Arrays.asList(Tuple.of("jms/Q.APKKB", "java.lang.String"), Tuple.of("jms/ESB.APKKB.OUT", "java.lang.String"), Tuple.of("jms/ESB.APKKB.IN", "java.lang.String")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass()) == 0;
	}

	@Test
	public void testCreateBeanWithProperty() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
		                  + "<beans xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
		                  + "    <bean class=\"java.lang.String\" id=\"connectionFactoryString\">\n"
		                  + "        <property name=\"prop1\" value=\"value1\"/>\n"
		                  + "    </bean>\n"
		                  + "</beans>\n";
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
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
		                  + "<beans xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
		                  + "    <bean class=\"java.lang.String\" id=\"connectionFactoryString\">\n"
		                  + "        <property name=\"prop1\" value=\"value1\"/>\n"
		                  + "        <property name=\"prop2\" value=\"value2\"/>\n"
		                  + "        <property name=\"prop3\" value=\"value3\"/>\n"
		                  + "    </bean>\n"
		                  + "</beans>\n";
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
