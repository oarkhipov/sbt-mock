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
@ContextConfiguration({ "/env/mockapp-servlet-test-context-generator-services-mzp.xml" })
public class MockSpringContextGeneratorTestMZPConfig extends AbstractTestNGSpringContextTests {

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
				"       xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www.springframework.org/schema/beans/spring-beans.xsd&#xA;        http://www.springframework.org/schema/integration http://www.springframework.org/schema/integration/spring-integration.xsd&#xA;        http://www.springframework.org/schema/integration/jms http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd\">\n" +
				"   <import resource=\"../contextConfigs/base-config.xml\"/>\n" +
				"   <import resource=\"../contextConfigs/logging-config.xml\"/>\n" +
				"   <!--Connection Factory for jndi [jms/ESB.PROMETHEUS.CRM.CF]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusCrmCf_jndiConnectionFactory\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.CRM.CF\"/>\n" +
				"   </bean>\n" +
				"   <!--Mock Inbound Queue for [jms/ESB.PROMETHEUS.CRM.CF]:[jms/ESB.PROMETHEUS.CRM.IN]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusCrmIn_jmsEsbPrometheusCrmCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.CRM.IN\"/>\n" +
				"   </bean>\n" +
				"   <!--Mock Outbound Queue for [jms/ESB.PROMETHEUS.CRM.CF]:[jms/ESB.PROMETHEUS.CRM.OUT]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusCrmOut_jmsEsbPrometheusCrmCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.CRM.OUT\"/>\n" +
				"   </bean>\n" +
				"   <int:channel id=\"jmsEsbPrometheusCrmIn_jmsEsbPrometheusCrmCf_channel\"/>\n" +
				"   <int:channel id=\"jmsEsbPrometheusCrmOut_jmsEsbPrometheusCrmCf_channel\"/>\n" +
				"   <!--Inbound gateway for queues pair [jms/ESB.PROMETHEUS.CRM.CF]:[jms/ESB.PROMETHEUS.CRM.IN] -> [jms/ESB.PROMETHEUS.CRM.CF]:[jms/ESB.PROMETHEUS.CRM.OUT]--><int-jms:inbound-gateway request-destination=\"jmsEsbPrometheusCrmIn_jmsEsbPrometheusCrmCf_queue\"\n" +
				"                            default-reply-destination=\"jmsEsbPrometheusCrmOut_jmsEsbPrometheusCrmCf_queue\"\n" +
				"                            request-channel=\"jmsEsbPrometheusCrmIn_jmsEsbPrometheusCrmCf_channel\"\n" +
				"                            reply-channel=\"jmsEsbPrometheusCrmOut_jmsEsbPrometheusCrmCf_channel\"\n" +
				"                            id=\"jmsEsbPrometheusCrmCf_jmsesbprometheuscrmin_jmsesbprometheuscrmcf_channel_jmsesbprometheuscrmout_jmsesbprometheuscrmcf_channel_jmsin\"\n" +
				"                            connection-factory=\"jmsEsbPrometheusCrmCf_jndiConnectionFactory\"/>\n" +
				"   <!--Driver Request Queue for [jms/ESB.PROMETHEUS.CRM.CF]:[jms/ESB.PROMETHEUS.CRM.RESPONSE]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusCrmResponse_jmsEsbPrometheusCrmCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.CRM.RESPONSE\"/>\n" +
				"   </bean>\n" +
				"   <!--Driver MessageTemplate Queue for [jms/ESB.PROMETHEUS.CRM.CF]:[jms/ESB.PROMETHEUS.CRM.REQUEST]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusCrmRequest_jmsEsbPrometheusCrmCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.CRM.REQUEST\"/>\n" +
				"   </bean>\n" +
				"   <int:channel id=\"jmsEsbPrometheusCrmResponse_jmsEsbPrometheusCrmCf_channel\"/>\n" +
				"   <int-jms:outbound-gateway id=\"jmsEsbPrometheusCrmCf_jmsout\"\n" +
				"                             request-channel=\"jmsEsbPrometheusCrmResponse_jmsEsbPrometheusCrmCf_channel\"\n" +
				"                             reply-channel=\"DriverInboundResponse\"\n" +
				"                             receive-timeout=\"30000\"\n" +
				"                             reply-timeout=\"30000\"\n" +
				"                             request-destination=\"jmsEsbPrometheusCrmResponse_jmsEsbPrometheusCrmCf_queue\"\n" +
				"                             reply-destination=\"jmsEsbPrometheusCrmRequest_jmsEsbPrometheusCrmCf_queue\"\n" +
				"                             connection-factory=\"jmsEsbPrometheusCrmCf_jndiConnectionFactory\"\n" +
				"                             header-mapper=\"defHeaderMapper\"/>\n" +
				"   <!--Connection Factory for jndi [jms/Q.PROMETHEUS.JUPITER-SA.CF]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsQPrometheusJupiterSaCf_jndiConnectionFactory\">\n" +
				"      <property name=\"jndiName\" value=\"jms/Q.PROMETHEUS.JUPITER-SA.CF\"/>\n" +
				"   </bean>\n" +
				"   <!--Mock Inbound Queue for [jms/Q.PROMETHEUS.JUPITER-SA.CF]:[jms/Q.PROMETHEUS.JUPITER-SA.IN]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsQPrometheusJupiterSaIn_jmsQPrometheusJupiterSaCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/Q.PROMETHEUS.JUPITER-SA.IN\"/>\n" +
				"   </bean>\n" +
				"   <!--Mock Outbound Queue for [jms/Q.PROMETHEUS.JUPITER-SA.CF]:[jms/Q.PROMETHEUS.JUPITER-SA.OUT]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsQPrometheusJupiterSaOut_jmsQPrometheusJupiterSaCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/Q.PROMETHEUS.JUPITER-SA.OUT\"/>\n" +
				"   </bean>\n" +
				"   <int:channel id=\"jmsQPrometheusJupiterSaIn_jmsQPrometheusJupiterSaCf_channel\"/>\n" +
				"   <int:channel id=\"jmsQPrometheusJupiterSaOut_jmsQPrometheusJupiterSaCf_channel\"/>\n" +
				"   <!--Inbound gateway for queues pair [jms/Q.PROMETHEUS.JUPITER-SA.CF]:[jms/Q.PROMETHEUS.JUPITER-SA.IN] -> [jms/Q.PROMETHEUS.JUPITER-SA.CF]:[jms/Q.PROMETHEUS.JUPITER-SA.OUT]--><int-jms:inbound-gateway request-destination=\"jmsQPrometheusJupiterSaIn_jmsQPrometheusJupiterSaCf_queue\"\n" +
				"                            default-reply-destination=\"jmsQPrometheusJupiterSaOut_jmsQPrometheusJupiterSaCf_queue\"\n" +
				"                            request-channel=\"jmsQPrometheusJupiterSaIn_jmsQPrometheusJupiterSaCf_channel\"\n" +
				"                            reply-channel=\"jmsQPrometheusJupiterSaOut_jmsQPrometheusJupiterSaCf_channel\"\n" +
				"                            id=\"jmsQPrometheusJupiterSaCf_jmsqprometheusjupitersain_jmsqprometheusjupitersacf_channel_jmsqprometheusjupitersaout_jmsqprometheusjupitersacf_channel_jmsin\"\n" +
				"                            connection-factory=\"jmsQPrometheusJupiterSaCf_jndiConnectionFactory\"/>\n" +
				"   <!--Driver Request Queue for [jms/Q.PROMETHEUS.JUPITER-SA.CF]:[jms/Q.PROMETHEUS.JUPITER-REG.IN]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsQPrometheusJupiterRegIn_jmsQPrometheusJupiterSaCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/Q.PROMETHEUS.JUPITER-REG.IN\"/>\n" +
				"   </bean>\n" +
				"   <!--Driver MessageTemplate Queue for [jms/Q.PROMETHEUS.JUPITER-SA.CF]:[jms/Q.PROMETHEUS.JUPITER-REG.OUT]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsQPrometheusJupiterRegOut_jmsQPrometheusJupiterSaCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/Q.PROMETHEUS.JUPITER-REG.OUT\"/>\n" +
				"   </bean>\n" +
				"   <int:channel id=\"jmsQPrometheusJupiterRegIn_jmsQPrometheusJupiterSaCf_channel\"/>\n" +
				"   <int-jms:outbound-gateway id=\"jmsQPrometheusJupiterSaCf_jmsout\"\n" +
				"                             request-channel=\"jmsQPrometheusJupiterRegIn_jmsQPrometheusJupiterSaCf_channel\"\n" +
				"                             reply-channel=\"DriverInboundResponse\"\n" +
				"                             receive-timeout=\"30000\"\n" +
				"                             reply-timeout=\"30000\"\n" +
				"                             request-destination=\"jmsQPrometheusJupiterRegIn_jmsQPrometheusJupiterSaCf_queue\"\n" +
				"                             reply-destination=\"jmsQPrometheusJupiterRegOut_jmsQPrometheusJupiterSaCf_queue\"\n" +
				"                             connection-factory=\"jmsQPrometheusJupiterSaCf_jndiConnectionFactory\"\n" +
				"                             header-mapper=\"defHeaderMapper\"/>\n" +
				"   <!--Connection Factory for jndi [jms/ESB.PROMETHEUS.MDM.CF]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusMdMCf_jndiConnectionFactory\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.MDM.CF\"/>\n" +
				"   </bean>\n" +
				"   <!--Mock Inbound Queue for [jms/ESB.PROMETHEUS.MDM.CF]:[jms/ESB.PROMETHEUS.MDM.IN]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusMdMIn_jmsEsbPrometheusMdMCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.MDM.IN\"/>\n" +
				"   </bean>\n" +
				"   <!--Mock Outbound Queue for [jms/ESB.PROMETHEUS.MDM.CF]:[jms/ESB.PROMETHEUS.MDM.OUT]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusMdMOut_jmsEsbPrometheusMdMCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.MDM.OUT\"/>\n" +
				"   </bean>\n" +
				"   <int:channel id=\"jmsEsbPrometheusMdMIn_jmsEsbPrometheusMdMCf_channel\"/>\n" +
				"   <int:channel id=\"jmsEsbPrometheusMdMOut_jmsEsbPrometheusMdMCf_channel\"/>\n" +
				"   <!--Inbound gateway for queues pair [jms/ESB.PROMETHEUS.MDM.CF]:[jms/ESB.PROMETHEUS.MDM.IN] -> [jms/ESB.PROMETHEUS.MDM.CF]:[jms/ESB.PROMETHEUS.MDM.OUT]--><int-jms:inbound-gateway request-destination=\"jmsEsbPrometheusMdMIn_jmsEsbPrometheusMdMCf_queue\"\n" +
				"                            default-reply-destination=\"jmsEsbPrometheusMdMOut_jmsEsbPrometheusMdMCf_queue\"\n" +
				"                            request-channel=\"jmsEsbPrometheusMdMIn_jmsEsbPrometheusMdMCf_channel\"\n" +
				"                            reply-channel=\"jmsEsbPrometheusMdMOut_jmsEsbPrometheusMdMCf_channel\"\n" +
				"                            id=\"jmsEsbPrometheusMdMCf_jmsesbprometheusmdmin_jmsesbprometheusmdmcf_channel_jmsesbprometheusmdmout_jmsesbprometheusmdmcf_channel_jmsin\"\n" +
				"                            connection-factory=\"jmsEsbPrometheusMdMCf_jndiConnectionFactory\"/>\n" +
				"   <int-jms:outbound-gateway id=\"jmsEsbPrometheusMdMCf_jmsout\"\n" +
				"                             request-channel=\"jmsEsbPrometheusMdMIn_jmsEsbPrometheusMdMCf_channel\"\n" +
				"                             reply-channel=\"DriverInboundResponse\"\n" +
				"                             receive-timeout=\"30000\"\n" +
				"                             reply-timeout=\"30000\"\n" +
				"                             request-destination=\"jmsEsbPrometheusMdMIn_jmsEsbPrometheusMdMCf_queue\"\n" +
				"                             reply-destination=\"jmsEsbPrometheusMdMOut_jmsEsbPrometheusMdMCf_queue\"\n" +
				"                             connection-factory=\"jmsEsbPrometheusMdMCf_jndiConnectionFactory\"\n" +
				"                             header-mapper=\"defHeaderMapper\"/>\n" +
				"   <!--Connection Factory for jndi [jms/ESB.PROMETHEUS.ASYNC.CF]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusAsyncCf_jndiConnectionFactory\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.ASYNC.CF\"/>\n" +
				"   </bean>\n" +
				"   <!--Mock Inbound Queue for [jms/ESB.PROMETHEUS.ASYNC.CF]:[jms/ESB.PROMETHEUS.ASYNC.IN]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusAsyncIn_jmsEsbPrometheusAsyncCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.ASYNC.IN\"/>\n" +
				"   </bean>\n" +
				"   <!--Mock Outbound Queue for [jms/ESB.PROMETHEUS.ASYNC.CF]:[jms/ESB.PROMETHEUS.ASYNC.OUT]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusAsyncOut_jmsEsbPrometheusAsyncCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.ASYNC.OUT\"/>\n" +
				"   </bean>\n" +
				"   <int:channel id=\"jmsEsbPrometheusAsyncIn_jmsEsbPrometheusAsyncCf_channel\"/>\n" +
				"   <int:channel id=\"jmsEsbPrometheusAsyncOut_jmsEsbPrometheusAsyncCf_channel\"/>\n" +
				"   <!--Inbound gateway for queues pair [jms/ESB.PROMETHEUS.ASYNC.CF]:[jms/ESB.PROMETHEUS.ASYNC.IN] -> [jms/ESB.PROMETHEUS.ASYNC.CF]:[jms/ESB.PROMETHEUS.ASYNC.OUT]--><int-jms:inbound-gateway request-destination=\"jmsEsbPrometheusAsyncIn_jmsEsbPrometheusAsyncCf_queue\"\n" +
				"                            default-reply-destination=\"jmsEsbPrometheusAsyncOut_jmsEsbPrometheusAsyncCf_queue\"\n" +
				"                            request-channel=\"jmsEsbPrometheusAsyncIn_jmsEsbPrometheusAsyncCf_channel\"\n" +
				"                            reply-channel=\"jmsEsbPrometheusAsyncOut_jmsEsbPrometheusAsyncCf_channel\"\n" +
				"                            id=\"jmsEsbPrometheusAsyncCf_jmsesbprometheusasyncin_jmsesbprometheusasynccf_channel_jmsesbprometheusasyncout_jmsesbprometheusasynccf_channel_jmsin\"\n" +
				"                            connection-factory=\"jmsEsbPrometheusAsyncCf_jndiConnectionFactory\"/>\n" +
				"   <!--Driver Request Queue for [jms/ESB.PROMETHEUS.ASYNC.CF]:[jms/ESB.PROMETHEUS.ASYNC.REQUEST]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusAsyncRequest_jmsEsbPrometheusAsyncCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.ASYNC.REQUEST\"/>\n" +
				"   </bean>\n" +
				"   <!--Driver MessageTemplate Queue for [jms/ESB.PROMETHEUS.ASYNC.CF]:[jms/ESB.PROMETHEUS.ASYNC.RESPONSE]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsEsbPrometheusAsyncResponse_jmsEsbPrometheusAsyncCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/ESB.PROMETHEUS.ASYNC.RESPONSE\"/>\n" +
				"   </bean>\n" +
				"   <int:channel id=\"jmsEsbPrometheusAsyncRequest_jmsEsbPrometheusAsyncCf_channel\"/>\n" +
				"   <int-jms:outbound-gateway id=\"jmsEsbPrometheusAsyncCf_jmsout\"\n" +
				"                             request-channel=\"jmsEsbPrometheusAsyncRequest_jmsEsbPrometheusAsyncCf_channel\"\n" +
				"                             reply-channel=\"DriverInboundResponse\"\n" +
				"                             receive-timeout=\"30000\"\n" +
				"                             reply-timeout=\"30000\"\n" +
				"                             request-destination=\"jmsEsbPrometheusAsyncRequest_jmsEsbPrometheusAsyncCf_queue\"\n" +
				"                             reply-destination=\"jmsEsbPrometheusAsyncResponse_jmsEsbPrometheusAsyncCf_queue\"\n" +
				"                             connection-factory=\"jmsEsbPrometheusAsyncCf_jndiConnectionFactory\"\n" +
				"                             header-mapper=\"defHeaderMapper\"/>\n" +
				"   <!--Connection Factory for jndi [jms/Q.PROMETHEUS.JUPITER-REG.CF]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsQPrometheusJupiterRegCf_jndiConnectionFactory\">\n" +
				"      <property name=\"jndiName\" value=\"jms/Q.PROMETHEUS.JUPITER-REG.CF\"/>\n" +
				"   </bean>\n" +
				"   <!--Mock Inbound Queue for [jms/Q.PROMETHEUS.JUPITER-REG.CF]:[jms/Q.PROMETHEUS.JUPITER-REG.IN]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsQPrometheusJupiterRegIn_jmsQPrometheusJupiterRegCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/Q.PROMETHEUS.JUPITER-REG.IN\"/>\n" +
				"   </bean>\n" +
				"   <!--Mock Outbound Queue for [jms/Q.PROMETHEUS.JUPITER-REG.CF]:[jms/Q.PROMETHEUS.JUPITER-REG.OUT]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsQPrometheusJupiterRegOut_jmsQPrometheusJupiterRegCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/Q.PROMETHEUS.JUPITER-REG.OUT\"/>\n" +
				"   </bean>\n" +
				"   <int:channel id=\"jmsQPrometheusJupiterRegIn_jmsQPrometheusJupiterRegCf_channel\"/>\n" +
				"   <int:channel id=\"jmsQPrometheusJupiterRegOut_jmsQPrometheusJupiterRegCf_channel\"/>\n" +
				"   <!--Inbound gateway for queues pair [jms/Q.PROMETHEUS.JUPITER-REG.CF]:[jms/Q.PROMETHEUS.JUPITER-REG.IN] -> [jms/Q.PROMETHEUS.JUPITER-REG.CF]:[jms/Q.PROMETHEUS.JUPITER-REG.OUT]--><int-jms:inbound-gateway request-destination=\"jmsQPrometheusJupiterRegIn_jmsQPrometheusJupiterRegCf_queue\"\n" +
				"                            default-reply-destination=\"jmsQPrometheusJupiterRegOut_jmsQPrometheusJupiterRegCf_queue\"\n" +
				"                            request-channel=\"jmsQPrometheusJupiterRegIn_jmsQPrometheusJupiterRegCf_channel\"\n" +
				"                            reply-channel=\"jmsQPrometheusJupiterRegOut_jmsQPrometheusJupiterRegCf_channel\"\n" +
				"                            id=\"jmsQPrometheusJupiterRegCf_jmsqprometheusjupiterregin_jmsqprometheusjupiterregcf_channel_jmsqprometheusjupiterregout_jmsqprometheusjupiterregcf_channel_jmsin\"\n" +
				"                            connection-factory=\"jmsQPrometheusJupiterRegCf_jndiConnectionFactory\"/>\n" +
				"   <!--Driver Request Queue for [jms/Q.PROMETHEUS.JUPITER-REG.CF]:[jms/Q.PROMETHEUS.JUPITER.RESPONSE]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsQPrometheusJupiterResponse_jmsQPrometheusJupiterRegCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/Q.PROMETHEUS.JUPITER.RESPONSE\"/>\n" +
				"   </bean>\n" +
				"   <!--Driver MessageTemplate Queue for [jms/Q.PROMETHEUS.JUPITER-REG.CF]:[jms/Q.PROMETHEUS.JUPITER.REQUEST]--><bean class=\"org.springframework.jndi.JndiObjectFactoryBean\"\n" +
				"         id=\"jmsQPrometheusJupiterRequest_jmsQPrometheusJupiterRegCf_queue\">\n" +
				"      <property name=\"jndiName\" value=\"jms/Q.PROMETHEUS.JUPITER.REQUEST\"/>\n" +
				"   </bean>\n" +
				"   <int:channel id=\"jmsQPrometheusJupiterResponse_jmsQPrometheusJupiterRegCf_channel\"/>\n" +
				"   <int-jms:outbound-gateway id=\"jmsQPrometheusJupiterRegCf_jmsout\"\n" +
				"                             request-channel=\"jmsQPrometheusJupiterResponse_jmsQPrometheusJupiterRegCf_channel\"\n" +
				"                             reply-channel=\"DriverInboundResponse\"\n" +
				"                             receive-timeout=\"30000\"\n" +
				"                             reply-timeout=\"30000\"\n" +
				"                             request-destination=\"jmsQPrometheusJupiterResponse_jmsQPrometheusJupiterRegCf_queue\"\n" +
				"                             reply-destination=\"jmsQPrometheusJupiterRequest_jmsQPrometheusJupiterRegCf_queue\"\n" +
				"                             connection-factory=\"jmsQPrometheusJupiterRegCf_jndiConnectionFactory\"\n" +
				"                             header-mapper=\"defHeaderMapper\"/>\n" +
				"   <!--Aggregator for [jms/ESB.PROMETHEUS.CRM.CF]:[jms/ESB.PROMETHEUS.CRM.IN]--><int:service-activator output-channel=\"MockInboundRequestAggregated\"\n" +
				"                          input-channel=\"jmsEsbPrometheusCrmIn_jmsEsbPrometheusCrmCf_channel\"\n" +
				"                          method=\"aggregate\">\n" +
				"      <bean class=\"ru.sbt.bpm.mock.spring.bean.MessageAggregator\">\n" +
				"         <constructor-arg type=\"ru.sbt.bpm.mock.config.enums.Protocol\" value=\"JMS\"/>\n" +
				"         <constructor-arg type=\"java.lang.String\" value=\"jms/ESB.PROMETHEUS.CRM.CF\"/>\n" +
				"         <constructor-arg type=\"java.lang.String\" value=\"jms/ESB.PROMETHEUS.CRM.IN\"/>\n" +
				"      </bean>\n" +
				"   </int:service-activator>\n" +
				"   <int:channel id=\"jmsEsbPrometheusCrmOut_routerChannel\"/>\n" +
				"   <!--MOCK ROUTER--><int:router input-channel=\"MockOutboundRouterResponse\"\n" +
				"               expression=\"payload.jmsConnectionFactoryName+'_'+payload.queue\"\n" +
				"               id=\"MockRouter\">\n" +
				"      <int:mapping value=\"jms/ESB.PROMETHEUS.CRM.CF_jms/ESB.PROMETHEUS.CRM.OUT\"\n" +
				"                   channel=\"jmsEsbPrometheusCrmOut_routerChannel\"/>\n" +
				"      <int:mapping value=\"jms/Q.PROMETHEUS.JUPITER-SA.CF_jms/Q.PROMETHEUS.JUPITER-SA.OUT\"\n" +
				"                   channel=\"jmsQPrometheusJupiterSaOut_routerChannel\"/>\n" +
				"      <int:mapping value=\"jms/ESB.PROMETHEUS.MDM.CF_jms/ESB.PROMETHEUS.MDM.OUT\"\n" +
				"                   channel=\"jmsEsbPrometheusMdMOut_routerChannel\"/>\n" +
				"      <int:mapping value=\"jms/ESB.PROMETHEUS.ASYNC.CF_jms/ESB.PROMETHEUS.ASYNC.OUT\"\n" +
				"                   channel=\"jmsEsbPrometheusAsyncOut_routerChannel\"/>\n" +
				"      <int:mapping value=\"jms/Q.PROMETHEUS.JUPITER-REG.CF_jms/Q.PROMETHEUS.JUPITER-REG.OUT\"\n" +
				"                   channel=\"jmsQPrometheusJupiterRegOut_routerChannel\"/>\n" +
				"   </int:router>\n" +
				"   <!--Extractor service activator for [jms/ESB.PROMETHEUS.CRM.CF]:[jms/ESB.PROMETHEUS.CRM.OUT]--><int:service-activator output-channel=\"jmsEsbPrometheusCrmOut_jmsEsbPrometheusCrmCf_channel\"\n" +
				"                          input-channel=\"jmsEsbPrometheusCrmOut_routerChannel\"\n" +
				"                          expression=\"payload.payload\"/>\n" +
				"   <int:channel id=\"jmsEsbPrometheusCrmResponse_routerChannel\"/>\n" +
				"   <!--DRIVER ROUTER--><int:router input-channel=\"DriverRouterChannelInput\"\n" +
				"               expression=\"payload.jmsConnectionFactoryName+'_'+payload.queue\"\n" +
				"               id=\"DriverRouter\">\n" +
				"      <int:mapping value=\"jms/ESB.PROMETHEUS.CRM.CF_jms/ESB.PROMETHEUS.CRM.RESPONSE\"\n" +
				"                   channel=\"jmsEsbPrometheusCrmResponse_routerChannel\"/>\n" +
				"      <int:mapping value=\"jms/Q.PROMETHEUS.JUPITER-SA.CF_jms/Q.PROMETHEUS.JUPITER-REG.IN\"\n" +
				"                   channel=\"jmsQPrometheusJupiterRegIn_routerChannel\"/>\n" +
				"      <int:mapping value=\"jms/ESB.PROMETHEUS.MDM.CF_jms/ESB.PROMETHEUS.MDM.IN\"\n" +
				"                   channel=\"jmsEsbPrometheusMdMIn_routerChannel\"/>\n" +
				"      <int:mapping value=\"jms/ESB.PROMETHEUS.ASYNC.CF_jms/ESB.PROMETHEUS.ASYNC.REQUEST\"\n" +
				"                   channel=\"jmsEsbPrometheusAsyncRequest_routerChannel\"/>\n" +
				"      <int:mapping value=\"jms/Q.PROMETHEUS.JUPITER-REG.CF_jms/Q.PROMETHEUS.JUPITER.RESPONSE\"\n" +
				"                   channel=\"jmsQPrometheusJupiterResponse_routerChannel\"/>\n" +
				"   </int:router>\n" +
				"   <!--Extractor service activator for [jms/ESB.PROMETHEUS.CRM.CF]:[jms/ESB.PROMETHEUS.CRM.RESPONSE]--><int:service-activator output-channel=\"jmsEsbPrometheusCrmResponse_jmsEsbPrometheusCrmCf_channel\"\n" +
				"                          input-channel=\"jmsEsbPrometheusCrmResponse_routerChannel\"\n" +
				"                          expression=\"payload.payload\"/>\n" +
				"   <!--Aggregator for [jms/Q.PROMETHEUS.JUPITER-SA.CF]:[jms/Q.PROMETHEUS.JUPITER-SA.IN]--><int:service-activator output-channel=\"MockInboundRequestAggregated\"\n" +
				"                          input-channel=\"jmsQPrometheusJupiterSaIn_jmsQPrometheusJupiterSaCf_channel\"\n" +
				"                          method=\"aggregate\">\n" +
				"      <bean class=\"ru.sbt.bpm.mock.spring.bean.MessageAggregator\">\n" +
				"         <constructor-arg type=\"ru.sbt.bpm.mock.config.enums.Protocol\" value=\"JMS\"/>\n" +
				"         <constructor-arg type=\"java.lang.String\" value=\"jms/Q.PROMETHEUS.JUPITER-SA.CF\"/>\n" +
				"         <constructor-arg type=\"java.lang.String\" value=\"jms/Q.PROMETHEUS.JUPITER-SA.IN\"/>\n" +
				"      </bean>\n" +
				"   </int:service-activator>\n" +
				"   <int:channel id=\"jmsQPrometheusJupiterSaOut_routerChannel\"/>\n" +
				"   <!--Extractor service activator for [jms/Q.PROMETHEUS.JUPITER-SA.CF]:[jms/Q.PROMETHEUS.JUPITER-SA.OUT]--><int:service-activator output-channel=\"jmsQPrometheusJupiterSaOut_jmsQPrometheusJupiterSaCf_channel\"\n" +
				"                          input-channel=\"jmsQPrometheusJupiterSaOut_routerChannel\"\n" +
				"                          expression=\"payload.payload\"/>\n" +
				"   <int:channel id=\"jmsQPrometheusJupiterRegIn_routerChannel\"/>\n" +
				"   <!--Extractor service activator for [jms/Q.PROMETHEUS.JUPITER-SA.CF]:[jms/Q.PROMETHEUS.JUPITER-REG.IN]--><int:service-activator output-channel=\"jmsQPrometheusJupiterRegIn_jmsQPrometheusJupiterSaCf_channel\"\n" +
				"                          input-channel=\"jmsQPrometheusJupiterRegIn_routerChannel\"\n" +
				"                          expression=\"payload.payload\"/>\n" +
				"   <!--Aggregator for [jms/ESB.PROMETHEUS.MDM.CF]:[jms/ESB.PROMETHEUS.MDM.IN]--><int:service-activator output-channel=\"MockInboundRequestAggregated\"\n" +
				"                          input-channel=\"jmsEsbPrometheusMdMIn_jmsEsbPrometheusMdMCf_channel\"\n" +
				"                          method=\"aggregate\">\n" +
				"      <bean class=\"ru.sbt.bpm.mock.spring.bean.MessageAggregator\">\n" +
				"         <constructor-arg type=\"ru.sbt.bpm.mock.config.enums.Protocol\" value=\"JMS\"/>\n" +
				"         <constructor-arg type=\"java.lang.String\" value=\"jms/ESB.PROMETHEUS.MDM.CF\"/>\n" +
				"         <constructor-arg type=\"java.lang.String\" value=\"jms/ESB.PROMETHEUS.MDM.IN\"/>\n" +
				"      </bean>\n" +
				"   </int:service-activator>\n" +
				"   <int:channel id=\"jmsEsbPrometheusMdMOut_routerChannel\"/>\n" +
				"   <!--Extractor service activator for [jms/ESB.PROMETHEUS.MDM.CF]:[jms/ESB.PROMETHEUS.MDM.OUT]--><int:service-activator output-channel=\"jmsEsbPrometheusMdMOut_jmsEsbPrometheusMdMCf_channel\"\n" +
				"                          input-channel=\"jmsEsbPrometheusMdMOut_routerChannel\"\n" +
				"                          expression=\"payload.payload\"/>\n" +
				"   <int:channel id=\"jmsEsbPrometheusMdMIn_routerChannel\"/>\n" +
				"   <!--Extractor service activator for [jms/ESB.PROMETHEUS.MDM.CF]:[jms/ESB.PROMETHEUS.MDM.IN]--><int:service-activator output-channel=\"jmsEsbPrometheusMdMIn_jmsEsbPrometheusMdMCf_channel\"\n" +
				"                          input-channel=\"jmsEsbPrometheusMdMIn_routerChannel\"\n" +
				"                          expression=\"payload.payload\"/>\n" +
				"   <!--Aggregator for [jms/ESB.PROMETHEUS.ASYNC.CF]:[jms/ESB.PROMETHEUS.ASYNC.IN]--><int:service-activator output-channel=\"MockInboundRequestAggregated\"\n" +
				"                          input-channel=\"jmsEsbPrometheusAsyncIn_jmsEsbPrometheusAsyncCf_channel\"\n" +
				"                          method=\"aggregate\">\n" +
				"      <bean class=\"ru.sbt.bpm.mock.spring.bean.MessageAggregator\">\n" +
				"         <constructor-arg type=\"ru.sbt.bpm.mock.config.enums.Protocol\" value=\"JMS\"/>\n" +
				"         <constructor-arg type=\"java.lang.String\" value=\"jms/ESB.PROMETHEUS.ASYNC.CF\"/>\n" +
				"         <constructor-arg type=\"java.lang.String\" value=\"jms/ESB.PROMETHEUS.ASYNC.IN\"/>\n" +
				"      </bean>\n" +
				"   </int:service-activator>\n" +
				"   <int:channel id=\"jmsEsbPrometheusAsyncOut_routerChannel\"/>\n" +
				"   <!--Extractor service activator for [jms/ESB.PROMETHEUS.ASYNC.CF]:[jms/ESB.PROMETHEUS.ASYNC.OUT]--><int:service-activator output-channel=\"jmsEsbPrometheusAsyncOut_jmsEsbPrometheusAsyncCf_channel\"\n" +
				"                          input-channel=\"jmsEsbPrometheusAsyncOut_routerChannel\"\n" +
				"                          expression=\"payload.payload\"/>\n" +
				"   <int:channel id=\"jmsEsbPrometheusAsyncRequest_routerChannel\"/>\n" +
				"   <!--Extractor service activator for [jms/ESB.PROMETHEUS.ASYNC.CF]:[jms/ESB.PROMETHEUS.ASYNC.REQUEST]--><int:service-activator output-channel=\"jmsEsbPrometheusAsyncRequest_jmsEsbPrometheusAsyncCf_channel\"\n" +
				"                          input-channel=\"jmsEsbPrometheusAsyncRequest_routerChannel\"\n" +
				"                          expression=\"payload.payload\"/>\n" +
				"   <!--Aggregator for [jms/Q.PROMETHEUS.JUPITER-REG.CF]:[jms/Q.PROMETHEUS.JUPITER-REG.IN]--><int:service-activator output-channel=\"MockInboundRequestAggregated\"\n" +
				"                          input-channel=\"jmsQPrometheusJupiterRegIn_jmsQPrometheusJupiterRegCf_channel\"\n" +
				"                          method=\"aggregate\">\n" +
				"      <bean class=\"ru.sbt.bpm.mock.spring.bean.MessageAggregator\">\n" +
				"         <constructor-arg type=\"ru.sbt.bpm.mock.config.enums.Protocol\" value=\"JMS\"/>\n" +
				"         <constructor-arg type=\"java.lang.String\" value=\"jms/Q.PROMETHEUS.JUPITER-REG.CF\"/>\n" +
				"         <constructor-arg type=\"java.lang.String\" value=\"jms/Q.PROMETHEUS.JUPITER-REG.IN\"/>\n" +
				"      </bean>\n" +
				"   </int:service-activator>\n" +
				"   <int:channel id=\"jmsQPrometheusJupiterRegOut_routerChannel\"/>\n" +
				"   <!--Extractor service activator for [jms/Q.PROMETHEUS.JUPITER-REG.CF]:[jms/Q.PROMETHEUS.JUPITER-REG.OUT]--><int:service-activator output-channel=\"jmsQPrometheusJupiterRegOut_jmsQPrometheusJupiterRegCf_channel\"\n" +
				"                          input-channel=\"jmsQPrometheusJupiterRegOut_routerChannel\"\n" +
				"                          expression=\"payload.payload\"/>\n" +
				"   <int:channel id=\"jmsQPrometheusJupiterResponse_routerChannel\"/>\n" +
				"   <!--Extractor service activator for [jms/Q.PROMETHEUS.JUPITER-REG.CF]:[jms/Q.PROMETHEUS.JUPITER.RESPONSE]--><int:service-activator output-channel=\"jmsQPrometheusJupiterResponse_jmsQPrometheusJupiterRegCf_channel\"\n" +
				"                          input-channel=\"jmsQPrometheusJupiterResponse_routerChannel\"\n" +
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
