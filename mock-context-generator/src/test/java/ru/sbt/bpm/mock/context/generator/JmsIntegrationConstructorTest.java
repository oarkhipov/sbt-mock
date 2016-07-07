package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Beans;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */
public class JmsIntegrationConstructorTest extends AbstractConfigGenerator {

	BeansConstructor beansConstructor = new BeansConstructor();

	JmsIntegrationConstructor jmsIntegrationConstructor = new JmsIntegrationConstructor();

	@Test
	public void testCreateInboundGateway() throws JAXBException {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><beans "
		                  + "xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                  + ".springframework.org/schema/beans/spring-beans.xsd\n"
		                  + "        http://www.springframework.org/schema/integration    http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration http://www.springframework"
		                  + ".org/schema/integration/spring-integration.xsd\n"
		                  + "        http://www.springframework.org/schema/integration/jms http://www.springframework"
		                  + ".org/schema/integration/jms/spring-integration-jms.xsd\" xmlns=\"http://www"
		                  + ".springframework.org/schema/beans\" xmlns:jms=\"http://www.springframework"
		                  + ".org/schema/integration/jms\" xmlns:int=\"http://www.springframework"
		                  + ".org/schema/integration\" xmlns:xsi=\"http://www.w3"
		                  + ".org/2001/XMLSchema-instance\"><jms:inbound-gateway request-destination=\"inboundMock\" "
		                  + "default-reply-destination=\"defaultReplyChannel\" request-channel=\"inboubdRequest\" "
		                  + "reply-channel=\"replyChannel\" id=\"idName\" "
		                  + "connection-factory=\"jndiConnectionFactory\"/></beans>";
		Beans beans = beansConstructor.createBeans();
		beans = jmsIntegrationConstructor.createInboundGateway(beans, "idName", "inboundMock", "inboubdRequest", "replyChannel", "defaultReplyChannel", "jndiConnectionFactory");
		assert compareResults(expected, beans, beansConstructor.getBeanFactory().getClass(), jmsIntegrationConstructor.getIntegrationFactory().getClass()) == 0;
	}
}
