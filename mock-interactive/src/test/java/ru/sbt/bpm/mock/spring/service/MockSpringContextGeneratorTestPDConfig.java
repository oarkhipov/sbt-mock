package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.context.generator.service.SpringContextGeneratorService;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.*;

import static org.testng.Assert.assertEquals;

/**
 * Created by sbt-hodakovskiy-da on 11.07.2016.
 */

@Slf4j
@ContextConfiguration({ "/env/mockapp-servlet-test-context-generator-services-pd.xml" })
public class MockSpringContextGeneratorTestPDConfig extends AbstractTestNGSpringContextTests {

	@Autowired
	MockSpringContextGeneratorService generator;

	@Autowired
	SpringContextGeneratorService generatorService;

	@Test
	public void testGettingData () throws JAXBException, IOException, TransformerException, XMLStreamException {
		String actual = generatorService.toXml(generator.generateContext());
		log.debug(actual);
		assertEquals(actual, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		                     + "<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
		                     + "       xmlns:jms=\"http://www.springframework.org/schema/integration/jms\"\n"
		                     + "       xmlns:int=\"http://www.springframework.org/schema/integration\"\n"
		                     + "       xmlns=\"http://www.springframework.org/schema/beans\"\n"
		                     + "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www"
		                     + ".springframework.org/schema/beans/spring-beans.xsd         http://www.springframework"
		                     + ".org/schema/integration http://www.springframework.org/schema/integration/spring-integration"
		                     + ".xsd         http://www.springframework.org/schema/integration/jms http://www.springframework"
		                     + ".org/schema/integration/jms/spring-integration-jms.xsd\">\n"
		                     + "   <import resource=\"../contextConfigs/base-config.xml\"/>\n"
		                     + "   <import resource=\"../contextConfigs/logging-config.xml\"/>\n"
		                     + "   <bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n"
		                     + "         id=\"javaCompEnvJmsQLegaLCf_jndiConnectionFactory\">\n"
		                     + "      <property name=\"jndiName\" value=\"java:comp/env/jms/Q.LEGAL.CF\"/>\n"
		                     + "   </bean>\n"
		                     + "   <bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n"
		                     + "         id=\"javaCompEnvJmsQLegaLToCrmorgSyncreSp_queue\">\n"
		                     + "      <property name=\"jndiName\" value=\"java:comp/env/jms/Q.LEGAL.TO.CRMORG.SYNCRESP\"/>\n"
		                     + "   </bean>\n"
		                     + "   <bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n"
		                     + "         id=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_queue\">\n"
		                     + "      <property name=\"jndiName\" value=\"java:comp/env/jms/Q.LEGAL.FROM.CRMORG.SYNCRESP\"/>\n"
		                     + "   </bean>\n"
		                     + "   <bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n"
		                     + "         id=\"javaCompEnvJmsQLegaLFromCrmorg_queue\">\n"
		                     + "      <property name=\"jndiName\" value=\"java:comp/env/jms/Q.LEGAL.FROM.CRMORG\"/>\n"
		                     + "   </bean>\n"
		                     + "   <bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n"
		                     + "         id=\"javaCompEnvJmsQLegaLToCrmorg_queue\">\n"
		                     + "      <property name=\"jndiName\" value=\"java:comp/env/jms/Q.LEGAL.TO.CRMORG\"/>\n"
		                     + "   </bean>\n"
		                     + "   <int:channel id=\"javaCompEnvJmsQLegaLToCrmorgSyncreSp_channel\"/>\n"
		                     + "   <int:channel id=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_channel\"/>\n"
		                     + "   <jms:inbound-gateway request-destination=\"javaCompEnvJmsQLegaLToCrmorgSyncreSp_queue\"\n"
		                     + "                        "
		                     + "default-reply-destination=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_queue\"\n"
		                     + "                        request-channel=\"javaCompEnvJmsQLegaLToCrmorgSyncreSp_channel\"\n"
		                     + "                        reply-channel=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_channel\"\n"
		                     + "                        id=\"javaCompEnvJmsQLegaLCf_jmsin\"\n"
		                     + "                        connection-factory=\"javaCompEnvJmsQLegaLCf_jndiConnectionFactory"
		                     + "\"/>\n"
		                     + "   <int:channel id=\"javaCompEnvJmsQLegaLFromCrmorg_channel\"/>\n"
		                     + "   <int:channel id=\"javaCompEnvJmsQLegaLToCrmorg_channel\"/>\n"
		                     + "   <jms:outbound-gateway id=\"javaCompEnvJmsQLegaLCf_jmsout\"\n"
		                     + "                         request-channel=\"javaCompEnvJmsQLegaLFromCrmorg_channel\"\n"
		                     + "                         reply-channel=\"javaCompEnvJmsQLegaLToCrmorg_channel\"\n"
		                     + "                         receive-timeout=\"30000\"\n"
		                     + "                         reply-timeout=\"30000\"\n"
		                     + "                         request-destination=\"javaCompEnvJmsQLegaLFromCrmorg_queue\"\n"
		                     + "                         reply-destination=\"javaCompEnvJmsQLegaLToCrmorg_queue\"\n"
		                     + "                         connection-factory=\"javaCompEnvJmsQLegaLCf_jndiConnectionFactory\"\n"
		                     + "                         header-mapper=\"defHeaderMapper\"/>\n"
		                     + "   <int:service-activator output-channel=\"MockInboundRequestAggregated\"\n"
		                     + "                          input-channel=\"javaCompEnvJmsQLegaLToCrmorgSyncreSp_channel\"\n"
		                     + "                          method=\"aggregate\">\n"
		                     + "      <bean class=\"ru.sbt.bpm.mock.spring.bean.MessageAggregator\">\n"
		                     + "         <constructor-arg type=\"ru.sbt.bpm.mock.config.enums.Protocol\" value=\"JMS\"/>\n"
		                     + "         <constructor-arg type=\"java.lang.String\" value=\"java:comp/env/jms/Q.LEGAL.CF\"/>\n"
		                     + "         <constructor-arg type=\"java.lang.String\"\n"
		                     + "                          value=\"java:comp/env/jms/Q.LEGAL.TO.CRMORG.SYNCRESP\"/>\n"
		                     + "      </bean>\n"
		                     + "   </int:service-activator>\n"
		                     + "   <int:channel id=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_routerChannel\"/>\n"
		                     + "   <int:router input-channel=\"MockOutboundRouterResponse\"\n"
		                     + "               expression=\"payload.jmsConnectionFactoryName+'_'+payload.queue\"\n"
		                     + "               id=\"MockRouter\">\n"
		                     + "      <int:mapping value=\"java:comp/env/jms/Q.LEGAL.CF_java:comp/env/jms/Q.LEGAL.FROM.CRMORG"
		                     + ".SYNCRESP\"\n"
		                     + "                   channel=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_routerChannel\"/>\n"
		                     + "   </int:router>\n"
		                     + "   <int:service-activator output-channel=\"java:comp/env/jms/Q.LEGAL.FROM.CRMORG.SYNCRESP\"\n"
		                     + "                          input-channel=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_routerChannel"
		                     + "\"\n"
		                     + "                          expression=\"payload.payload\"/>\n"
		                     + "   <int:channel id=\"javaCompEnvJmsQLegaLFromCrmorg_routerChannel\"/>\n"
		                     + "   <int:router input-channel=\"DriverRouterChannelInput\"\n"
		                     + "               expression=\"payload.jmsConnectionFactoryName+'_'+payload.queue\"\n"
		                     + "               id=\"DriverRouter\">\n"
		                     + "      <int:mapping value=\"java:comp/env/jms/Q.LEGAL.CF_java:comp/env/jms/Q.LEGAL.FROM"
		                     + ".CRMORG\"\n"
		                     + "                   channel=\"javaCompEnvJmsQLegaLFromCrmorg_routerChannel\"/>\n"
		                     + "   </int:router>\n"
		                     + "   <int:service-activator output-channel=\"DriverOutboundRequest\"\n"
		                     + "                          input-channel=\"javaCompEnvJmsQLegaLFromCrmorg_routerChannel\"\n"
		                     + "                          expression=\"payload.payload\"/>\n"
		                     + "</beans>\n");
	}

	private String readFileWithoutBOM(File file) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new BOMInputStream(new FileInputStream(file), false)));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null)
			sb.append(line);
		return sb.toString();
	}

}
