package ru.sbt.bpm.mock.spring.service;

import generated.springframework.beans.Bean;
import generated.springframework.beans.Beans;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.list.TreeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.context.generator.service.constructors.BeansConstructor;
import ru.sbt.bpm.mock.context.generator.service.constructors.IntegrationConstructor;
import ru.sbt.bpm.mock.context.generator.service.constructors.JmsIntegrationConstructor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 11.07.2016.
 */

@Slf4j
@Service
public class MockSpringContextGeneratorService {

	/**
	 * *************   import constants    ***************
	 */
	// base config import path
	private static final String BASE_CONFIG_IMPORT    = "../contextConfigs/base-config.xml";
	// logginig config import path
	private static final String LOGGING_CONFIG_IMPORT = "../contextConfigs/logging-config.xml";

	/**
	 * ************* classes constants    ***************
	 */
	// java.lang.String class
	private static final String STRING_CLASS             = "java.lang.String";
	// org.springframework.jndi.JndiObjectFactoryBean class
	private static final String JNDI_OBJECT_CLASS        = "org.springframework.jndi.JndiObjectFactoryBean";
	// ru.sbt.bpm.mock.spring.integration.gateway.ClientService class
	private static final String CLIENT_SERVICE_CLASS     = "ru.sbt.bpm.mock.spring.integration.gateway.ClientService";
	// ru.sbt.bpm.mock.spring.bean.MessageAggregator class
	private static final String MESSAGE_AGGREGATOR_CLASS = "ru.sbt.bpm.mock.spring.bean.MessageAggregator";
	// ru.sbt.bpm.mock.config.enums.Protocol class
	private static final String PROTOCOL_CLASS           = "ru.sbt.bpm.mock.config.enums.Protocol";

	/**
	 * ************* properties constants  ***************
	 */
	// jndiName property name
	private static final String JNDI_PROPERTY_NAME            = "jndiName";
	// JMS constructor-arg value
	private static final String JMS_CONSTRUCTOR_ARG_VALUE     = "JMS";
	// sendMockMessage method gateway name
	private static final String SEND_MOCK_MESSAGE_METHOD_NAME = "sendMockMessage";
	// aggregate method name service activator
	private static final String AGGREGATE_SERVICE_ACTIVATOR_METHOD_NAME = "aggregate";
	// service activator expressions for generation response
	private static final String SERVICE_ACTIVATOR_RESPONSE_EXPRESSION = "@responseGenerator.proceedJmsRequest(payload).payload";

	/**
	 * *************   logger channels constants  ***************
	 */
	// MockInboundRequestLogger channel
	private static final String MOCK_INBOUNT_REQUEST_LOGGER_CHANNEL    = "MockInboundRequestLogger";
	// MockOutboundResponseLogger channel
	private static final String MOCK_OUTBOUND_RESPONSELOGGER_CHANNEL   = "MockOutboundResponseLogger";
	// DriverOutboundRequestLogger channel
	private static final String DRIVER_OUTBOUND_RESPONSELOGGER_CHANNEL = "DriverOutboundRequestLogger";
	// DriverInboundResponseLogger channel
	private static final String DRIVER_INBOUND_RESPONSELOGGER_CHANNEL  = "DriverInboundResponseLogger";

	/**
	 * *************   mock servlet elements prefixes & postfixes  ***************
	 */
	// connectionFactoryString bean
//	private static final String QUEUE_CONNECTION_FACTORY_STRING_BEAN         = "_connectionFactory";
	// mockConnectionInputString bean
//	private static final String MOCK_CONNECTION_INPUT_STRING_BEAN            = "_mockConnectionInput";
	// mockConnectionOutputString bean
//	private static final String MOCK_CONNECTION_OUTPUT_STRING_BEAN           = "_mockConnectionOutput";
	// driverConnectionOutputString bean
//	private static final String DRIVER_CONNECTION_OUTPUT_STRING_BEAN         = "_driverConnectionOutput";
	// driverConnectionInputString bean
//	private static final String DRIVER_CONNECTION_INPUT_STRING_BEAN          = "_driverConnectionInput";
	// jndiConnectionFactory
	private static final String JNDI_CONNECTION_FACTORY_POSTFIX              = "_jndiConnectionFactory";
	// Queue postfix
	private static final String QUEUE_POSTFIX                                = "_queue";
	// channel postfix
	private static final String CHANNEL_POSTFIX                              = "_channel";
	// jms inbound gateway postfix
	private static final String JMS_INBOUND_GATEWAY_POSTFIX                  = "_jmsin";
	// jms outbound gateway postfix
	private static final String JMS_OUTBOUND_GATEWAY_POSTFIX                 = "_jmsout";
	// service activator aggregator channel
	private static final String SERVICE_ACTIVATOR_AGGREGATOR_CHANNEL_POSTFIX = "_aggregatedChannel";
	// gateway postfix
	private static final String GATEWAY_POSTFIX = "_gateway";
	// router channel postfix
	private static final String ROUTER_CHANNEL_POSTFIX = "_routerChannel";
	// router postfix
	private static final String ROUTER_POSTFIX = "_router";
	// driver aggregated channel
	private static final String DRIVER_AGGREGATED_CHANNEL_POSTFIX = "_aggregatedDriver";

	/**
	 * *************   mock container elements     ***************
	 */


	@Autowired
	private MockConfigContainer container;

	@Autowired
	private BeansConstructor beansConstructor;

	@Autowired
	private IntegrationConstructor integrationConstructor;

	@Autowired
	private JmsIntegrationConstructor jmsConstructor;

	@Getter
	private Beans beans;

	private List<ru.sbt.bpm.mock.config.entities.System> systems;

	// список созданных id beans
	private List<String> listBeansId;

	@PostConstruct
	private void init () {
		systems = container.getConfig().getSystems().getSystems();
		listBeansId = new TreeList<String>();
		initBeans();
	}

	/**
	 * Инициализация <beans></beans>
	 */
	private void initBeans () {
		beans = beansConstructor.createBeans();
		beans = beansConstructor.createImports(beans, Arrays.asList(BASE_CONFIG_IMPORT, LOGGING_CONFIG_IMPORT));
	}

	/**
	 * Очистка всех составляющих
	 */
	private void clean () {
		systems.clear();
		listBeansId.clear();
	}

	public void reInit () {
		clean();
		init();
	}

	public Beans generateContext () {

		for (ru.sbt.bpm.mock.config.entities.System system : systems) {
			if (system.getProtocol() == Protocol.JMS) {
				/**
				 * Настройка используемой конфигурации заглушки
				 */
				String queueConnectionFactory = system.getQueueConnectionFactory();
				String mockInputString = system.getMockIncomeQueue();
				String mockOutputString = system.getMockOutcomeQueue();
				String driverOutputString = system.getDriverOutcomeQueue();
				String driverInputString = system.getDriverIncomeQueue();

//				final String queueConnectionFactoryName = createBeanConnectionString(queueConnectionFactory,
//				                                                                     QUEUE_CONNECTION_FACTORY_STRING_BEAN);

//				final String mockInputStringBeanName = createBeanConnectionString(mockInputString,
//				                                                                  MOCK_CONNECTION_INPUT_STRING_BEAN);

//				final String mockOutputStringBeanName = createBeanConnectionString(mockOutputString,
//				                                                                   MOCK_CONNECTION_OUTPUT_STRING_BEAN);

//				final String driverOutputStringBeanName = createBeanConnectionString(driverOutputString, DRIVER_CONNECTION_OUTPUT_STRING_BEAN);

//				final String driverInputStringBeanName = createBeanConnectionString(driverInputString, DRIVER_CONNECTION_INPUT_STRING_BEAN);

				/**
				 * Создаем настройки подлючения к jms
				 */
				String jndiFactoryName = createJndiConnectionObjects(queueConnectionFactory, JNDI_CONNECTION_FACTORY_POSTFIX);

				String jndiMockInboundQueueName = createJndiConnectionObjects(mockInputString, QUEUE_POSTFIX);

				String jndiMockOutboundQueueName = createJndiConnectionObjects(mockOutputString, QUEUE_POSTFIX);

				String jndiDriverRequestQueueName = createJndiConnectionObjects(driverOutputString, QUEUE_POSTFIX);

				String jndiDriverResponseQueueName = createJndiConnectionObjects(driverInputString, QUEUE_POSTFIX);

				/**
				 * Создание jms inbound & outbound
				 */
				// Создание каналов inbound gateway
				String mockInputChannel = createChannel(mockInputString, CHANNEL_POSTFIX);

				String mockOutputChannel = createChannel(mockOutputString, CHANNEL_POSTFIX);

				// Создаем inbound-gateway
				String jmsInboundGatewayBean = createJmsInboundGateway(queueConnectionFactory, jndiFactoryName,
				                                                       jndiMockInboundQueueName,
				                                                       jndiMockOutboundQueueName, mockInputChannel,
				                                                       mockOutputChannel);

				// Создание каналов outbound gateway
				String driverRequestChannel = createChannel(driverOutputString, CHANNEL_POSTFIX);

				String driverResponseChannel = createChannel(driverInputString, CHANNEL_POSTFIX);

				// Создаем outbound-gateway
				String jmsOutboundGatewayBean = createJmsOutboundGateway(queueConnectionFactory, jndiDriverRequestQueueName, jndiDriverResponseQueueName, driverRequestChannel, driverResponseChannel, jndiFactoryName);

				/**
				 * Маршрутизация
				 */
//				String serviceActivatorAggregatedChannel = createServiceActivatorAggregated(queueConnectionFactory, mockInputString, mockInputChannel, mockOutputChannel);

				/**
				 * driver mapping
				 */
				// router channel
//				String routerInputChannel = createChannel(queueConnectionFactory, ROUTER_CHANNEL_POSTFIX);

				// gateway
//				String gatewayBean = createGateway(queueConnectionFactory, routerInputChannel, driverResponseChannel);

				// router aggregated channel
//				String routerAggregatedChannel = createChannel(queueConnectionFactory, DRIVER_AGGREGATED_CHANNEL_POSTFIX);
			}

			if (system.getProtocol() == Protocol.SOAP) {

			}
		}

		return beans;
	}

	private String createRouter (String queueConnectionFactory, String routerInputChannel, String routerAggregatedChannel) {
		String routerBeanName = deleteSpecialCharsFromString(queueConnectionFactory) + ROUTER_POSTFIX;
		if (!isBeanIdInList(routerBeanName)) {
			listBeansId.add(routerBeanName);
			beans = integrationConstructor.createRouter(beans, routerBeanName, "payload.jmsConnectionFactoryName+'_'+payload.queue", routerInputChannel, null);
		}
		return routerBeanName;
	}

	/**
	 *
	 * @param queueConnectionFactory
	 * @param routerInputChannel
	 * @param driverResponseChannel
	 * @return
	 */
	private String createGateway (String queueConnectionFactory, String routerInputChannel, String driverResponseChannel) {
		String gatewayBeanName = deleteSpecialCharsFromString(queueConnectionFactory) + GATEWAY_POSTFIX;
		if (!isBeanIdInList(gatewayBeanName)) {
			listBeansId.add(gatewayBeanName);
			beans = integrationConstructor.createGateway(beans, gatewayBeanName, driverResponseChannel, CLIENT_SERVICE_CLASS, "30000", "30000", Arrays.asList(Tuple.of(SEND_MOCK_MESSAGE_METHOD_NAME, routerInputChannel, driverResponseChannel)));
		}
		return gatewayBeanName;
	}

	/**
	 *
	 * @param inputChannel
	 * @param outputChannel
	 * @param expression
	 */
	private void createServiceActivatorWithExpressions (String inputChannel, String outputChannel, String expression) {
		beans = integrationConstructor.createServiceActivator(beans, inputChannel, outputChannel, expression);
	}

	/**
	 *
	 * @param queueConnectionFactory
	 * @param mockInputStringBeanName
	 * @param mockInputChannel
	 * @param mockOutputChannel
	 * @return
	 */
	private String createServiceActivatorAggregated (String queueConnectionFactory,
	                                                 String mockInputStringBeanName, String mockInputChannel,
	                                                 String mockOutputChannel) {
		String serviceActivatorBeanChannelName = deleteSpecialCharsFromString(queueConnectionFactory) + SERVICE_ACTIVATOR_AGGREGATOR_CHANNEL_POSTFIX;
		if (!isBeanIdInList(serviceActivatorBeanChannelName)) {
			listBeansId.add(serviceActivatorBeanChannelName);
			beans = integrationConstructor.createChannel(beans, serviceActivatorBeanChannelName);
			List<Tuple2<String, String>> listConstructorArgs = new ArrayList<Tuple2<String, String>>();
			listConstructorArgs.add(Tuple.of(JMS_CONSTRUCTOR_ARG_VALUE, PROTOCOL_CLASS));
			listConstructorArgs.add(Tuple.of(queueConnectionFactory, STRING_CLASS));
			listConstructorArgs.add(Tuple.of(mockInputStringBeanName, STRING_CLASS));
			Bean bean = beansConstructor.createBean(MESSAGE_AGGREGATOR_CLASS, listConstructorArgs);
			beans = integrationConstructor.createServiceActivator(beans, mockInputChannel, serviceActivatorBeanChannelName, AGGREGATE_SERVICE_ACTIVATOR_METHOD_NAME, bean);

			// Генерация ответа
			createServiceActivatorWithExpressions(serviceActivatorBeanChannelName, mockOutputChannel, SERVICE_ACTIVATOR_RESPONSE_EXPRESSION);
		}
		return serviceActivatorBeanChannelName;
	}

	/**
	 *
	 * @param connectionFactoryName
	 * @param jndiDriverRequest
	 * @param jndiDriverResponse
	 * @param driverRequestChannel
	 * @param driverResponseChannel
	 * @param jndiConnectionFactory
	 * @return
	 */
	private String createJmsOutboundGateway(String connectionFactoryName, String jndiDriverRequest, String jndiDriverResponse, String driverRequestChannel, String driverResponseChannel, String jndiConnectionFactory) {
		String jmsBeanName = deleteSpecialCharsFromString(connectionFactoryName) + JMS_OUTBOUND_GATEWAY_POSTFIX;
		if (!isBeanIdInList(jmsBeanName)) {
			listBeansId.add(jmsBeanName);
			beans = jmsConstructor.createOutboundGateway(beans, jmsBeanName, jndiDriverRequest, jndiDriverResponse, driverRequestChannel, driverResponseChannel, "defHeaderMapper", jndiConnectionFactory, "30000", "30000");
		}
		return jmsBeanName;
	}

	/**
	 *
	 * @param connectionFactoryName
	 * @param jndiConnectionFactory
	 * @param jndiMockInputString
	 * @param jndiMockOutputString
	 * @param mockInputChannel
	 * @param mockOutputChannel
	 * @return
	 */
	private String createJmsInboundGateway (String connectionFactoryName, String jndiConnectionFactory, String jndiMockInputString, String jndiMockOutputString, String mockInputChannel, String mockOutputChannel) {
		String jmsBeanName = deleteSpecialCharsFromString(connectionFactoryName) + JMS_INBOUND_GATEWAY_POSTFIX;
		if (!isBeanIdInList(jmsBeanName)) {
			listBeansId.add(jmsBeanName);
			beans = jmsConstructor.createInboundGateway(beans, jmsBeanName, jndiMockInputString, mockInputChannel,
			                                            mockOutputChannel, jndiMockOutputString, jndiConnectionFactory);
		}
		return jmsBeanName;
	}

	/**
	 *  Создание канала
	 *  <int:channel id=""/>
	 * @param beanString
	 * @return
	 */
	private String createChannel(String beanString, String channelPostfix) {
		String channelName = deleteSpecialCharsFromString(beanString) + channelPostfix;
		if (!isBeanIdInList(channelName)) {
			listBeansId.add(channelName);
			beans = integrationConstructor.createChannel(beans, channelName);
		}
		return channelName;
	}

	/**
	 * Созджание настроек для для подключения jms
	 * <bean id="" class="">
	 *     <property name="" value="#{}"/>
	 * </bean>
	 * @param beanString
	 * @param beanPostfix
	 * @return
	 */
	private String createJndiConnectionObjects (final String beanString, String beanPostfix) {
		String jndiStringName = deleteSpecialCharsFromString(beanString) + beanPostfix;
		if (!isBeanIdInList(jndiStringName)) {
			listBeansId.add(jndiStringName);
			beans = beansConstructor.createBean(beans, JNDI_OBJECT_CLASS, jndiStringName, new HashMap<String,
					String>() {
				{
					put(JNDI_PROPERTY_NAME, beanString);
				}
			});
		}
		return jndiStringName;
	}


//	/**
//	 * Создание настроек для конфигурации заглушки
//	 * <bean class="" id="">
//	 *      <constructor-arg value=""/>
//	 * </bean>
//	 * @param beanString
//	 * @param beanPostfix
//	 * @return
//	 */
//	private String createBeanConnectionString (String beanString, String beanPostfix) {
//		final String beanStringName = deleteSpecialCharsFromString(beanString) + beanPostfix;
//		if (!isBeanIdInList(beanStringName)) {
//			listBeansId.add(beanStringName);
//			beans = beansConstructor.createBean(beans, STRING_CLASS, beanStringName, Arrays.asList(Tuple.of
//					(beanString, "")));
//		}
//		return beanStringName;
//	}

	private boolean isBeanIdInList (String mockIncomeQueueName) {
		return listBeansId.contains(mockIncomeQueueName);
	}

	private String deleteSpecialCharsFromString (String string) {
		return string.replaceAll("\\W", "");
	}

}
