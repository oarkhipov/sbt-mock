package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.BOMInputStream;
import org.custommonkey.xmlunit.Diff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.spring.context.generator.service.SpringContextGeneratorService;
import ru.sbt.bpm.mock.spring.utils.XmlUtils;

import javax.xml.bind.JAXBException;
import java.io.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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
	public void testGettingData () throws JAXBException {
		String actual = generatorService.toXml(generator.generateContext());
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				"       xmlns:int=\"http://www.springframework.org/schema/integration\"\n" +
				"       xmlns:int-jms=\"http://www.springframework.org/schema/integration/jms\"\n" +
				"       xmlns=\"http://www.springframework.org/schema/beans\"\n" +
				"       xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www.springframework.org/schema/beans/spring-beans.xsd        http://www.springframework.org/schema/integration http://www.springframework.org/schema/integration/spring-integration.xsd        http://www.springframework.org/schema/integration/jms http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd\">\n" +
				"   <import resource=\"../contextConfigs/base-config.xml\"/>\n" +
				"   <import resource=\"../contextConfigs/logging-config.xml\"/>\n" +
				"   <!--Connection Factory for jndi [java:comp/env/jms/Q.LEGAL.CF]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"javaCompEnvJmsQLegaLCf_jndiConnectionFactory\">\n" +
				"      <property name=\"jndiName\" value=\"java:comp/env/jms/Q.LEGAL.CF\"/>\n" +
				"   </bean>\n" +
				"   <!--Mock Inbound Queue for [java:comp/env/jms/Q.LEGAL.CF]:[java:comp/env/jms/Q.LEGAL.TO.CRMORG.SYNCRESP]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"javaCompEnvJmsQLegaLToCrmorgSyncreSp_javaCompEnvJmsQLegaLCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"java:comp/env/jms/Q.LEGAL.TO.CRMORG.SYNCRESP\"/>\n" +
				"   </bean>\n" +
				"   <!--Mock Outbound Queue for [java:comp/env/jms/Q.LEGAL.CF]:[java:comp/env/jms/Q.LEGAL.FROM.CRMORG.SYNCRESP]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_javaCompEnvJmsQLegaLCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"java:comp/env/jms/Q.LEGAL.FROM.CRMORG.SYNCRESP\"/>\n" +
				"   </bean>\n" +
				"   <int:channel id=\"javaCompEnvJmsQLegaLToCrmorgSyncreSp_javaCompEnvJmsQLegaLCf_channel\"/>\n" +
				"   <int:channel id=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_javaCompEnvJmsQLegaLCf_channel\"/>\n" +
				"   <!--Inbound gateway for queues pair [java:comp/env/jms/Q.LEGAL.CF]:[java:comp/env/jms/Q.LEGAL.TO.CRMORG.SYNCRESP] -> [java:comp/env/jms/Q.LEGAL.CF]:[java:comp/env/jms/Q.LEGAL.FROM.CRMORG.SYNCRESP]--><int-jms:inbound-gateway request-destination=\"javaCompEnvJmsQLegaLToCrmorgSyncreSp_javaCompEnvJmsQLegaLCf_queue\"\n" +
				"                            default-reply-destination=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_javaCompEnvJmsQLegaLCf_queue\"\n" +
				"                            request-channel=\"javaCompEnvJmsQLegaLToCrmorgSyncreSp_javaCompEnvJmsQLegaLCf_channel\"\n" +
				"                            reply-channel=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_javaCompEnvJmsQLegaLCf_channel\"\n" +
				"                            id=\"javaCompEnvJmsQLegaLCf_javacompenvjmsqlegaltocrmorgsyncresp_javacompenvjmsqlegalcf_channel_javacompenvjmsqlegalfromcrmorgsyncresp_javacompenvjmsqlegalcf_channel_jmsin\"\n" +
				"                            connection-factory=\"javaCompEnvJmsQLegaLCf_jndiConnectionFactory\"/>\n" +
				"   <!--Driver Request Queue for [java:comp/env/jms/Q.LEGAL.CF]:[java:comp/env/jms/Q.LEGAL.FROM.CRMORG]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"javaCompEnvJmsQLegaLFromCrmorg_javaCompEnvJmsQLegaLCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"java:comp/env/jms/Q.LEGAL.FROM.CRMORG\"/>\n" +
				"   </bean>\n" +
				"   <!--Driver Response Queue for [java:comp/env/jms/Q.LEGAL.CF]:[java:comp/env/jms/Q.LEGAL.TO.CRMORG]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"javaCompEnvJmsQLegaLToCrmorg_javaCompEnvJmsQLegaLCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"java:comp/env/jms/Q.LEGAL.TO.CRMORG\"/>\n" +
				"   </bean>\n" +
				"   <int:channel id=\"javaCompEnvJmsQLegaLFromCrmorg_javaCompEnvJmsQLegaLCf_channel\"/>\n" +
				"   <int-jms:outbound-gateway id=\"javaCompEnvJmsQLegaLCf_javacompenvjmsqlegalfromcrmorg_javacompenvjmsqlegalcf_channel_driverinboundresponse_jmsout\"\n" +
				"                             request-channel=\"javaCompEnvJmsQLegaLFromCrmorg_javaCompEnvJmsQLegaLCf_channel\"\n" +
				"                             reply-channel=\"DriverInboundResponse\"\n" +
				"                             receive-timeout=\"30000\"\n" +
				"                             reply-timeout=\"30000\"\n" +
				"                             request-destination=\"javaCompEnvJmsQLegaLFromCrmorg_javaCompEnvJmsQLegaLCf_queue\"\n" +
				"                             reply-destination=\"javaCompEnvJmsQLegaLToCrmorg_javaCompEnvJmsQLegaLCf_queue\"\n" +
				"                             connection-factory=\"javaCompEnvJmsQLegaLCf_jndiConnectionFactory\"\n" +
				"                             header-mapper=\"defHeaderMapper\"/>\n" +
				"   <!--Aggregator for [java:comp/env/jms/Q.LEGAL.CF]:[java:comp/env/jms/Q.LEGAL.TO.CRMORG.SYNCRESP]--><int:service-activator output-channel=\"MockInboundRequestAggregated\"\n" +
				"                          input-channel=\"javaCompEnvJmsQLegaLToCrmorgSyncreSp_javaCompEnvJmsQLegaLCf_channel\"\n" +
				"                          method=\"aggregate\">\n" +
				"      <bean class=\"ru.sbt.bpm.mock.spring.bean.MessageAggregator\">\n" +
				"         <constructor-arg type=\"ru.sbt.bpm.mock.config.enums.Protocol\" value=\"JMS\"/>\n" +
				"         <constructor-arg type=\"java.lang.String\" value=\"java:comp/env/jms/Q.LEGAL.CF\"/>\n" +
				"         <constructor-arg type=\"java.lang.String\"\n" +
				"                          value=\"java:comp/env/jms/Q.LEGAL.TO.CRMORG.SYNCRESP\"/>\n" +
				"      </bean>\n" +
				"   </int:service-activator>\n" +
				"   <int:channel id=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_routerChannel\"/>\n" +
				"   <!--MOCK ROUTER--><int:router input-channel=\"MockOutboundRouterResponse\"\n" +
				"               expression=\"payload.jmsConnectionFactoryName+'_'+payload.queue\"\n" +
				"               id=\"MockRouter\">\n" +
				"      <int:mapping value=\"java:comp/env/jms/Q.LEGAL.CF_java:comp/env/jms/Q.LEGAL.FROM.CRMORG.SYNCRESP\"\n" +
				"                   channel=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_routerChannel\"/>\n" +
				"   </int:router>\n" +
				"   <!--Extractor service activator for [java:comp/env/jms/Q.LEGAL.CF]:[java:comp/env/jms/Q.LEGAL.FROM.CRMORG.SYNCRESP]--><int:service-activator output-channel=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_javaCompEnvJmsQLegaLCf_channel\"\n" +
				"                          input-channel=\"javaCompEnvJmsQLegaLFromCrmorgSyncreSp_routerChannel\"\n" +
				"                          expression=\"payload.payload\"/>\n" +
				"   <int:channel id=\"javaCompEnvJmsQLegaLFromCrmorg_routerChannel\"/>\n" +
				"   <!--DRIVER ROUTER--><int:router input-channel=\"DriverRouterChannelInput\"\n" +
				"               expression=\"payload.jmsConnectionFactoryName+'_'+payload.queue\"\n" +
				"               id=\"DriverRouter\">\n" +
				"      <int:mapping value=\"java:comp/env/jms/Q.LEGAL.CF_java:comp/env/jms/Q.LEGAL.FROM.CRMORG\"\n" +
				"                   channel=\"javaCompEnvJmsQLegaLFromCrmorg_routerChannel\"/>\n" +
				"   </int:router>\n" +
				"   <!--Extractor service activator for [java:comp/env/jms/Q.LEGAL.CF]:[java:comp/env/jms/Q.LEGAL.FROM.CRMORG]--><int:service-activator output-channel=\"javaCompEnvJmsQLegaLFromCrmorg_javaCompEnvJmsQLegaLCf_channel\"\n" +
				"                          input-channel=\"javaCompEnvJmsQLegaLFromCrmorg_routerChannel\"\n" +
				"                          expression=\"payload.payload\"/>\n" +
				"</beans>\n";
		log.debug(actual);
		String actualString = XmlUtils.compactXml(actual);
		String expectedString = XmlUtils.compactXml(expected);
		//noinspection Duplicates
		try {
			Diff diff = new Diff(actualString, expectedString);
			if (!diff.identical()) {
				assertEquals(actual, expected, diff.toString());
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
