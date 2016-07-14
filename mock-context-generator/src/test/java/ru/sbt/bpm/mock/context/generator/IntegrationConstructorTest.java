package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Bean;
import generated.springframework.beans.Beans;
import org.testng.annotations.Test;
import reactor.tuple.Tuple;
import ru.sbt.bpm.mock.context.generator.service.constructors.BeansConstructor;
import ru.sbt.bpm.mock.context.generator.service.constructors.IntegrationConstructor;

import javax.xml.bind.JAXBException;
import java.util.Arrays;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */
public class IntegrationConstructorTest extends AbstractConfigGenerator {

	BeansConstructor beansConstructor = new BeansConstructor();

	IntegrationConstructor integrationConstructor = new IntegrationConstructor();

	@Test
	public void testCreateChannel () throws JAXBException {
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
		                  + "    <int:channel id=\"channel\"/>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = integrationConstructor.createChannel(beans, "channel");
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor
				.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateMultiChannel () throws JAXBException {
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
		                  + "    <int:channel id=\"channelIn\"/>\n"
		                  + "    <int:channel id=\"channelOut\"/>\n"
		                  + "    <int:channel id=\"channelResponse\"/>\n"
		                  + "    <int:channel id=\"channelRequest\"/>\n"
		                  + "</beans>\n";
		Beans    beans   = beansConstructor.createBeans();
		String[] strings = { "channelIn", "channelOut", "channelResponse", "channelRequest" };
		for (String string : strings)
			beans = integrationConstructor.createChannel(beans, string);
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor
				.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateChannelWithWireTap () throws JAXBException {
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
		                  + "    <int:channel id=\"channel\">\n"
		                  + "        <int:interceptors>\n"
		                  + "            <int:wire-tap channel=\"wireTapChannel\"/>\n"
		                  + "        </int:interceptors>\n"
		                  + "    </int:channel>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = integrationConstructor.createChannel(beans, "channel", Arrays.asList("wireTapChannel"));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor
				.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateServiceActivatorWithBean () throws JAXBException {
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
		                  + "    <int:service-activator output-channel=\"outputChannel\" "
		                  + "input-channel=\"inputChannel\" method=\"method\">\n"
		                  + "        <bean class=\"java.lang.String\">\n"
		                  + "            <constructor-arg type=\"type1\" value=\"class1\"/>\n"
		                  + "            <constructor-arg type=\"type2\" value=\"class2\"/>\n"
		                  + "            <constructor-arg type=\"type3\" value=\"class3\"/>\n"
		                  + "        </bean>\n"
		                  + "    </int:service-activator>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		Bean bean = beansConstructor.createBean("java.lang.String", Arrays.asList(Tuple.of("class1", "type1"), Tuple
				.of("class2", "type2"), Tuple.of("class3", "type3")));
		beans = integrationConstructor.createServiceActivator(beans, "inputChannel", "outputChannel", "method", bean);
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor
				.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateServiceActivatorWithExpressions () throws JAXBException {
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
		                  + "    <int:service-activator output-channel=\"outputChannel\" "
		                  + "input-channel=\"inputChannel\" expression=\"expressions\"/>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = integrationConstructor.createServiceActivator(beans, "inputChannel", "outputChannel", "expressions");
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor
				.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateGateway () throws JAXBException {
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
		                  + "    <int:gateway id=\"systemEntry\" service-interface=\"ru.sbt.bpm.mock.spring"
		                  + ".integration.gateway.Service\" error-channel=\"error-channel\" "
		                  + "default-request-timeout=\"30000\" default-reply-timeout=\"30000\">\n"
		                  + "        <int:method name=\"sendMockMessage\" request-channel=\"requestChannel\" "
		                  + "reply-channel=\"replyChannel\"/>\n"
		                  + "    </int:gateway>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = integrationConstructor.createGateway(beans, "systemEntry", "error-channel", "ru.sbt.bpm.mock.spring"
		                                                                                    + ".integration.gateway"
		                                                                                    + ".Service", "30000",
		                                             "30000", Arrays.asList(Tuple.of("sendMockMessage",
		                                                                             "requestChannel",
		                                                                             "replyChannel")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor
				.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateRouterWithOneMapping () throws JAXBException {
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
		                  + "    <int:router input-channel=\"inputChannel\" expression=\"payload\" id=\"router\">\n"
		                  + "        <int:mapping value=\"value\" channel=\"channel\"/>\n"
		                  + "    </int:router>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = integrationConstructor.createRouter(beans, "router", "payload", "inputChannel", Arrays.asList(Tuple.of
				("value", "channel")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor
				.getIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateRouterWithMultiMappings () throws JAXBException {
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
		                  + "    <int:router input-channel=\"inputChannel\" expression=\"payload\" id=\"router\">\n"
		                  + "        <int:mapping value=\"value1\" channel=\"channel1\"/>\n"
		                  + "        <int:mapping value=\"value2\" channel=\"channel2\"/>\n"
		                  + "        <int:mapping value=\"value3\" channel=\"channel3\"/>\n"
		                  + "        <int:mapping value=\"value4\" channel=\"channel5\"/>\n"
		                  + "    </int:router>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = integrationConstructor.createRouter(beans, "router", "payload", "inputChannel", Arrays.asList(Tuple.of
				("value1", "channel1"), Tuple.of("value2", "channel2"), Tuple.of("value3", "channel3"), Tuple.of
				("value4", "channel5")));
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), integrationConstructor
				.getIntegrationFactory().getClass()) == 0;
	}
}
