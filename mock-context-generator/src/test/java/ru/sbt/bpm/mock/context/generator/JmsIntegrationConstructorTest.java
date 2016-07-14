package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Beans;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.context.generator.service.constructors.BeansConstructor;
import ru.sbt.bpm.mock.context.generator.service.constructors.JmsIntegrationConstructor;

import javax.xml.bind.JAXBException;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */
public class JmsIntegrationConstructorTest extends AbstractConfigGenerator {

	BeansConstructor beansConstructor = new BeansConstructor();

	JmsIntegrationConstructor jmsIntegrationConstructor = new JmsIntegrationConstructor();

	@Test
	public void testCreateInboundGateway () throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
		                  + "<beans xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
		                  + "    <jms:inbound-gateway request-destination=\"inboundMock\" "
		                  + "default-reply-destination=\"defaultReplyChannel\" request-channel=\"inboundRequest\" "
		                  + "reply-channel=\"replyChannel\" id=\"idName\" "
		                  + "connection-factory=\"jndiConnectionFactory\"/>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = jmsIntegrationConstructor.createInboundGateway(beans, "idName", "inboundMock", "inboundRequest",
		                                                       "replyChannel", "defaultReplyChannel",
		                                                       "jndiConnectionFactory");
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), jmsIntegrationConstructor
				.getJmsIntegrationFactory().getClass()) == 0;
	}

	@Test
	public void testCreateOutboundGateway () throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
		                  + "<beans xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
		                  + "    <jms:outbound-gateway id=\"jmsOut\" request-channel=\"outboundDriver\" "
		                  + "reply-channel=\"inboundDriver\" receive-timeout=\"30000\" reply-timeout=\"30000\" "
		                  + "request-destination=\"requestDriver\" reply-destination=\"replyDriver\" "
		                  + "connection-factory=\"connectionFactory\" header-mapper=\"headerMapper\"/>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = jmsIntegrationConstructor.createOutboundGateway(beans, "jmsOut", "requestDriver", "replyDriver",
		                                                        "outboundDriver", "inboundDriver", "headerMapper",
		                                                        "connectionFactory", "30000", "30000");
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), jmsIntegrationConstructor
				.getJmsIntegrationFactory().getClass()) == 0;
	}
}
