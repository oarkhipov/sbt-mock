package ru.sbt.bpm.mock.spring.context.generator;

import generated.springframework.beans.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.spring.context.generator.service.constructors.BeansConstructor;
import ru.sbt.bpm.mock.spring.context.generator.service.constructors.JmsIntegrationConstructor;

import javax.xml.bind.JAXBException;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */
public class JmsIntegrationConstructorTest extends AbstractConfigGenerator {

	@Autowired
	BeansConstructor beansConstructor;

	@Autowired
	JmsIntegrationConstructor jmsIntegrationConstructor;

	@Test
	public void testCreateInboundGateway () throws JAXBException {
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
		                  + "<jms:inbound-gateway request-destination=\"inboundMock\" "
		                  + "default-reply-destination=\"defaultReplyChannel\" request-channel=\"inboundRequest\" "
		                  + "reply-channel=\"replyChannel\" id=\"idName\" "
		                  + "connection-factory=\"jndiConnectionFactory\"/>\n"
		                  + "</beans>\n";
		Beans beans = beansConstructor.createBeans();
		beans = jmsIntegrationConstructor.createInboundGateway(beans, "idName", "inboundMock", "inboundRequest",
		                                                       "replyChannel", "defaultReplyChannel",
		                                                       "jndiConnectionFactory");
		compareResults(expected, beans, "JmsIntegrationConstructorTest :: testCreateInboundGateway");
	}

	@Test
	public void testCreateOutboundGateway () throws JAXBException {
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
		                  + "<jms:outbound-gateway id=\"jmsOut\" request-channel=\"outboundDriver\" "
		                  + "reply-channel=\"inboundDriver\" receive-timeout=\"30000\" reply-timeout=\"30000\" "
		                  + "request-destination=\"requestDriver\" reply-destination=\"replyDriver\" "
		                  + "connection-factory=\"connectionFactory\" header-mapper=\"headerMapper\"/>\n"
		                  + "</beans>";
		Beans beans = beansConstructor.createBeans();
		beans = jmsIntegrationConstructor.createOutboundGateway(beans, "jmsOut", "requestDriver", "replyDriver",
		                                                        "outboundDriver", "inboundDriver", "headerMapper",
		                                                        "connectionFactory", "30000", "30000");
		compareResults(expected, beans, "JmsIntegrationConstructorTest :: testCreateOutboundGateway");
	}
}
