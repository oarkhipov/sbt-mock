package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Bean;
import generated.springframework.beans.Beans;
import org.testng.annotations.Test;
import reactor.tuple.Tuple;

import javax.xml.bind.JAXBException;
import java.util.Arrays;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */
public class IntegrationConstructorTest extends AbstractConfigGenerator {

	BeansConstructor beansConstructor = new BeansConstructor();

	IntegrationConstructor integrationConstructor = new IntegrationConstructor();

	@Test
	public void testCreateChannel() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><beans "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns:ns2=\"http://www"
		                  + ".springframework.org/schema/integration\" xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ns2:channel "
		                  + "id=\"channel\"/></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = integrationConstructor.createChannel(beans, "channel");
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateMultiChannel() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><beans "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns:ns2=\"http://www"
		                  + ".springframework.org/schema/integration\" xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ns2:channel "
		                  + "id=\"channelIn\"/><ns2:channel id=\"channelOut\"/><ns2:channel "
		                  + "id=\"channelResponse\"/><ns2:channel id=\"channelRequest\"/></beans>";
		Beans beans = beansConstructor.createBeans();
		String[] strings = { "channelIn", "channelOut", "channelResponse", "channelRequest" };
		for (String string : strings)
			beans = integrationConstructor.createChannel(beans, string);
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateChannelWithWireTap() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><beans "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns:ns2=\"http://www"
		                  + ".springframework.org/schema/integration\" xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ns2:channel "
		                  + "id=\"channel\"><ns2:interceptors><ns2:wire-tap "
		                  + "channel=\"wireTapChannel\"/></ns2:interceptors></ns2:channel></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = integrationConstructor.createChannel(beans, "channel", Arrays.asList("wireTapChannel"));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateServiceActivatorWithBean() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><beans "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns:ns2=\"http://www"
		                  + ".springframework.org/schema/integration\" xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3"
		                  + ".org/2001/XMLSchema-instance\"><ns2:service-activator output-channel=\"outputChannel\" "
		                  + "input-channel=\"inputChannel\" method=\"method\"><bean class=\"java.lang"
		                  + ".String\"><constructor-arg type=\"type1\" value=\"class1\"/><constructor-arg "
		                  + "type=\"type2\" value=\"class2\"/><constructor-arg type=\"type3\" "
		                  + "value=\"class3\"/></bean></ns2:service-activator></beans>";
		Beans beans = beansConstructor.createBeans();
		Bean bean = beansConstructor.createBean("java.lang.String", Arrays.asList(Tuple.of("class1", "type1"), Tuple
				.of("class2", "type2"), Tuple.of("class3", "type3")));
		beans = integrationConstructor.createServiceActivator(beans, "inputChannel", "outputChannel", "method", bean);
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateServiceActivatorWithExpressions() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><beans "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns:ns2=\"http://www"
		                  + ".springframework.org/schema/integration\" xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3"
		                  + ".org/2001/XMLSchema-instance\"><ns2:service-activator output-channel=\"outputChannel\" "
		                  + "input-channel=\"inputChannel\" expression=\"expressions\"/></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = integrationConstructor.createServiceActivator(beans, "inputChannel", "outputChannel", "expressions");
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateGateway() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><beans "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns:ns2=\"http://www"
		                  + ".springframework.org/schema/integration\" xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ns2:gateway "
		                  + "id=\"systemEntry\" service-interface=\"ru.sbt.bpm.mock.spring.integration.gateway"
		                  + ".Service\" error-channel=\"error-channel\" default-request-timeout=\"30000\" "
		                  + "default-reply-timeout=\"30000\"><ns2:method name=\"sendMockMessage\" "
		                  + "request-channel=\"requestChannel\" reply-channel=\"replyChannel\"/></ns2:gateway></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = integrationConstructor.createGateway(beans, "systemEntry", "error-channel", "ru.sbt.bpm.mock.spring"
		                                                                                    + ".integration.gateway"
		                                                                                    + ".Service", "30000",
		                                             "30000", Arrays.asList(Tuple.of("sendMockMessage",
		                                                                             "requestChannel",
		                                                                             "replyChannel")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateRouterWithOneMapping () throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><beans "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns:ns2=\"http://www"
		                  + ".springframework.org/schema/integration\" xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ns2:router "
		                  + "input-channel=\"inputChannel\" expression=\"payload\" id=\"router\"><ns2:mapping "
		                  + "value=\"value\" channel=\"channel\"/></ns2:router></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = integrationConstructor.createRouter(beans, "router", "payload", "inputChannel", Arrays.asList(Tuple.of("value", "channel")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateRouterWithMultiMappings () throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><beans "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns:ns2=\"http://www"
		                  + ".springframework.org/schema/integration\" xmlns=\"http://www.springframework"
		                  + ".org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ns2:router "
		                  + "input-channel=\"inputChannel\" expression=\"payload\" id=\"router\"><ns2:mapping "
		                  + "value=\"value1\" channel=\"channel1\"/><ns2:mapping value=\"value2\" "
		                  + "channel=\"channel2\"/><ns2:mapping value=\"value3\" channel=\"channel3\"/><ns2:mapping "
		                  + "value=\"value4\" channel=\"channel5\"/></ns2:router></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = integrationConstructor.createRouter(beans, "router", "payload", "inputChannel", Arrays.asList(Tuple.of("value1", "channel1"), Tuple.of("value2", "channel2"), Tuple.of("value3", "channel3"), Tuple.of("value4", "channel5")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor.getIntegrationFactory().getClass()) == 0;
	}

}
