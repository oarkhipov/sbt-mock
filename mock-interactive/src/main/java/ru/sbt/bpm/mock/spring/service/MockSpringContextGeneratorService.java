package ru.sbt.bpm.mock.spring.service;

import generated.springframework.beans.Bean;
import generated.springframework.beans.Beans;
import generated.springframework.integration.RouterType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.list.TreeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.context.generator.service.constructors.BeansConstructor;
import ru.sbt.bpm.mock.spring.context.generator.service.constructors.IntegrationConstructor;
import ru.sbt.bpm.mock.spring.context.generator.service.constructors.JmsIntegrationConstructor;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import java.util.*;

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
	// ru.sbt.bpm.mock.spring.bean.MessageAggregator class
	private static final String MESSAGE_AGGREGATOR_CLASS = "ru.sbt.bpm.mock.spring.bean.MessageAggregator";
	// ru.sbt.bpm.mock.config.enums.Protocol class
	private static final String PROTOCOL_CLASS           = "ru.sbt.bpm.mock.config.enums.Protocol";

	/**
	 * ************* properties constants  ***************
	 */
	// jndiName property name
	private static final String JNDI_PROPERTY_NAME                      = "jndiName";
	// JMS constructor-arg value
	private static final String JMS_CONSTRUCTOR_ARG_VALUE               = "JMS";
	// aggregate method name service activator
	private static final String AGGREGATE_SERVICE_ACTIVATOR_METHOD_NAME = "aggregate";
	// service activator expressions for generation response
	private static final String SERVICE_ACTIVATOR_RESPONSE_EXPRESSION   = "payload.payload";
	// router payload
	private static final String ROUTER_PAYLOAD = "payload.jmsConnectionFactoryName+'_'+payload.queue";
	// default reply timeout
	private static final String DEFAULT_REPLY_TIMEOUT                   = "30000";
	// default receive timeout
	private static final String DEFAULT_RECEIVE_TIMEOUT                  = "30000";


	/**
	 * *************   channels constants  ***************
	 */
	// MockInboundRequestLogger channel
	private static final String MOCK_INBOUNT_REQUEST_LOGGER_CHANNEL    = "MockInboundRequestLogger";
	// MockOutboundResponseLogger channel
	private static final String MOCK_OUTBOUND_RESPONSELOGGER_CHANNEL   = "MockOutboundResponseLogger";
	// DriverOutboundRequestLogger channel
	private static final String DRIVER_OUTBOUND_RESPONSELOGGER_CHANNEL = "DriverOutboundRequestLogger";
	// DriverInboundResponseLogger channel
	private static final String DRIVER_INBOUND_RESPONSELOGGER_CHANNEL  = "DriverInboundResponseLogger";
	// MockInboundRequestAggregated channel
	private static final String MOCK_INBOUND_REQUEST_AGGREGATED = "MockInboundRequestAggregated";
	// MockOutboundRouterResponse channel
	private static final String MOCK_OUTBOUND_ROUTER_RESPONSE = "MockOutboundRouterResponse";
	// DriverRouterChannelInput channel
	private static final String DRIVER_ROUTER_CHANNEL_INPUT = "DriverRouterChannelInput";
	// DriverOutboundRequest channel
	private static final String DRIVER_OUTBOUND_REQUEST = "DriverOutboundRequest";

	/**
	 * *************   mock servlet elements prefixes & postfixes  ***************
	 */
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
	// gateway postfix
	private static final String GATEWAY_POSTFIX                              = "_gateway";
	// router channel postfix
	private static final String ROUTER_CHANNEL_POSTFIX                       = "_routerChannel";
	// router mock name
	private static final String MOCK_ROUTER_NAME                               = "MockRouter";
	// router driver name
	private static final String DRIVER_ROUTER_NAME                               = "DriverRouter";

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

	// Map созданных каналов для mock по системе
	private Map<String, Tuple2<String, String>> mapMockChannels;

	// Map созданных каналов для driver по системе
	private Map<String, Tuple2<String, String>> mapDriverChannels;

	@PostConstruct
	private void init () {
		systems = container.getConfig().getSystems().getSystems();
		listBeansId = new TreeList<String>();
		mapMockChannels = new HashMap<String, Tuple2<String, String>>();
		mapDriverChannels = new HashMap<String, Tuple2<String, String>>();
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
		mapDriverChannels.clear();
		mapMockChannels.clear();
	}

	public void reInit () {
		clean();
		init();
	}

	/**
	 * Генерация mockapp-servlet
	 * @return
	 */
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

				/**
				 * Создаем настройки подлючения к jms
				 */
				String jndiFactoryName = createJndiConnectionObjects(queueConnectionFactory,
				                                                     JNDI_CONNECTION_FACTORY_POSTFIX);

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

				mapMockChannels.put(system.getSystemName(), Tuple.of(mockInputChannel, mockOutputChannel));

				// Создаем inbound-gateway
				createJmsInboundGateway(queueConnectionFactory, jndiFactoryName, jndiMockInboundQueueName,
				                        jndiMockOutboundQueueName, mockInputChannel,
				                        mockOutputChannel);

				// Создание каналов outbound gateway
				String driverRequestChannel = createChannel(driverOutputString, CHANNEL_POSTFIX);

				String driverResponseChannel = createChannel(driverInputString, CHANNEL_POSTFIX);

				mapDriverChannels.put(system.getSystemName(), Tuple.of(driverRequestChannel, driverResponseChannel));

				// Создаем outbound-gateway
				createJmsOutboundGateway(queueConnectionFactory, jndiDriverRequestQueueName,
				                         jndiDriverResponseQueueName, driverRequestChannel, driverResponseChannel,
				                         jndiFactoryName);
			}
		}

		/**
		 * Маршрутизация
		 */
		for (ru.sbt.bpm.mock.config.entities.System system : systems) {
			if (system.getProtocol() == Protocol.JMS) {
				/**
				 * Mock
 				 */
				createServiceActivatorWithBean(mapMockChannels.get(system.getSystemName()).getT1(), system.getQueueConnectionFactory(), system.getMockIncomeQueue());
				// mockRouterChannel
				String mockRouterChannel = createChannel(system.getMockOutcomeQueue(), ROUTER_CHANNEL_POSTFIX);
				// router
				createRouter(MOCK_ROUTER_NAME, MOCK_OUTBOUND_ROUTER_RESPONSE, system.getQueueConnectionFactory(), system.getMockOutcomeQueue(), mockRouterChannel, true);
				// service activator
				createServiceActivatorWithExpressions(mockRouterChannel, system.getMockOutcomeQueue());

				/**
				 * Driver
				 */
				// driver channel
				String driverRouterChannel = createChannel(system.getDriverOutcomeQueue(), ROUTER_CHANNEL_POSTFIX);
				// router
				createRouter(DRIVER_ROUTER_NAME, DRIVER_ROUTER_CHANNEL_INPUT, system.getQueueConnectionFactory(), system.getDriverOutcomeQueue(), driverRouterChannel, false);
				// service activator
				createServiceActivatorWithExpressions(driverRouterChannel, DRIVER_OUTBOUND_REQUEST);
			}
		}
		return beans;
	}

	/**
	 *
	 * @param routerOutboundResponseChannel
	 * @param mockOutputChannel
	 */
	private void createServiceActivatorWithExpressions (String routerOutboundResponseChannel, String mockOutputChannel) {
		beans = integrationConstructor.createServiceActivator(beans, routerOutboundResponseChannel, mockOutputChannel, SERVICE_ACTIVATOR_RESPONSE_EXPRESSION);
	}

	/**
	 *
	 * @param queueConnectionFactory
	 * @param outputQueue
	 * @param routerOutboundResponseChannel
	 */
	private void createRouter (String routerName, String outboundRouterResponse, String queueConnectionFactory, String outputQueue, String routerOutboundResponseChannel, boolean isMock) {
		if (!isBeanIdInList(routerName)) {
			listBeansId.add(routerName);
			beans = integrationConstructor.createRouter(beans, routerName, ROUTER_PAYLOAD, outboundRouterResponse, createRouterMappings(queueConnectionFactory, outputQueue, routerOutboundResponseChannel));
		} else {
			for (Object obj : beans.getImportOrAliasOrBean())
				if (obj instanceof JAXBElement) {
					JAXBElement<RouterType> element = (JAXBElement<RouterType>) obj;
					RouterType router = element.getValue();
					if (router.getId().equals(routerName) && isMock)
						router.getMapping().add(integrationConstructor.createMapping(queueConnectionFactory + "_" +
					                                                             outputQueue,
					                                                             routerOutboundResponseChannel));

					if (router.getId().equals(routerName) && !isMock)
						router.getMapping().add(integrationConstructor.createMapping(queueConnectionFactory + "_" +
						                                                             outputQueue,
						                                                             routerOutboundResponseChannel));
				}
		}
	}

	/**
	 *
	 * @param queueConnectionFactory
	 * @param outputQueue
	 * @param outputChannel
	 * @return
	 */
	private List<Tuple2<String, String>> createRouterMappings (String queueConnectionFactory, String outputQueue, String outputChannel) {
		List<Tuple2<String, String>> mappings = new ArrayList<Tuple2<String, String>>();
		mappings.add(Tuple.of(queueConnectionFactory+ "_" + outputQueue, outputChannel));
		return mappings;
	}

	/**
	 * <service-activator input-channel="" output-channel="" method="">
	 *      <bean class="">
	 *          <constructor-agr value="" type=""/>
	 *          ...
	 *          <constructor-agr value="" type=""/>
	 *      </bean>
	 * </service-activator>
	 *
	 * @param mockInputChannel
	 * @param constructorArgsValue
	 */
	private void createServiceActivatorWithBean(String mockInputChannel, String... constructorArgsValue) {
		beans = integrationConstructor.createServiceActivator(beans, mockInputChannel, MOCK_INBOUND_REQUEST_AGGREGATED, AGGREGATE_SERVICE_ACTIVATOR_METHOD_NAME, createBeanForServiceActivator(constructorArgsValue));
	}

	/**
	 *     <constructor-agr value="" type=""/>
	 *     ...
	 *     <constructor-agr value="" type=""/>
	 *
	 * @param constructorArgsValue
	 * @return
	 */
	private List<Tuple2<String, String>> createConstructorArgsForBean (String... constructorArgsValue) {
		List<Tuple2<String, String>> list = new ArrayList<Tuple2<String, String>>();
		list.add(Tuple.of(JMS_CONSTRUCTOR_ARG_VALUE, PROTOCOL_CLASS));
		for (String arg : constructorArgsValue)
			list.add(Tuple.of(arg, STRING_CLASS));
		return list;
	}

	/**
	 * <bean class="">
	 *     <constructor-agr value="" type=""/>
	 *     ...
	 *     <constructor-agr value="" type=""/>
	 * </bean>
	 *
	 * @param constructorArgsValue
	 * @return
	 */
	private Bean createBeanForServiceActivator (String... constructorArgsValue) {
		return beansConstructor.createBean(MESSAGE_AGGREGATOR_CLASS, createConstructorArgsForBean(constructorArgsValue));
	}

	/**
	 * <jms:outbound-gateway id="" request-destination="" reply-destination="" request-channel=""
	 *          reply-channel="" header-mapper="" connection-factory="" reply-timeout="" receive-timeout="" />
	 *
	 * @param connectionFactoryName
	 * @param jndiDriverRequest
	 * @param jndiDriverResponse
	 * @param driverRequestChannel
	 * @param driverResponseChannel
	 * @param jndiConnectionFactory
	 * @return
	 */
	private String createJmsOutboundGateway (String connectionFactoryName, String jndiDriverRequest, String
			jndiDriverResponse, String driverRequestChannel, String driverResponseChannel, String
			                                         jndiConnectionFactory) {
		String jmsBeanName = generateBeanNameLowCamelStyle(connectionFactoryName, JMS_OUTBOUND_GATEWAY_POSTFIX);
		if (!isBeanIdInList(jmsBeanName)) {
			listBeansId.add(jmsBeanName);
			beans = jmsConstructor.createOutboundGateway(beans, jmsBeanName, jndiDriverRequest, jndiDriverResponse,
			                                             driverRequestChannel, driverResponseChannel,
			                                             "defHeaderMapper", jndiConnectionFactory, DEFAULT_REPLY_TIMEOUT, DEFAULT_RECEIVE_TIMEOUT);
		}
		return jmsBeanName;
	}

	/**
	 * <jms:inbound-gateway id="" request-destination="" request-channel="" reply-channel=""
	 *              default-reply-destination="" connection-factory="" />
	 *
	 * @param connectionFactoryName
	 * @param jndiConnectionFactory
	 * @param jndiMockInputString
	 * @param jndiMockOutputString
	 * @param mockInputChannel
	 * @param mockOutputChannel
	 * @return
	 */
	private String createJmsInboundGateway (String connectionFactoryName, String jndiConnectionFactory, String
			jndiMockInputString, String jndiMockOutputString, String mockInputChannel, String mockOutputChannel) {
		String jmsBeanName = generateBeanNameLowCamelStyle(connectionFactoryName, JMS_INBOUND_GATEWAY_POSTFIX);
		if (!isBeanIdInList(jmsBeanName)) {
			listBeansId.add(jmsBeanName);
			beans = jmsConstructor.createInboundGateway(beans, jmsBeanName, jndiMockInputString, mockInputChannel,
			                                            mockOutputChannel, jndiMockOutputString,
			                                            jndiConnectionFactory);
		}
		return jmsBeanName;
	}

	/**
	 * Создание канала
	 * <int:channel id=""/>
	 *
	 * @param beanString
	 * @return
	 */
	private String createChannel (String beanString, String channelPostfix) {
		String channelName = generateBeanNameLowCamelStyle(beanString, channelPostfix);
		if (!isBeanIdInList(channelName)) {
			listBeansId.add(channelName);
			beans = integrationConstructor.createChannel(beans, channelName);
		}
		return channelName;
	}

	/**
	 * Созджание настроек для для подключения jms
	 * <bean id="" class="">
	 *  <property name="" value="#{}"/>
	 * </bean>
	 *
	 * @param beanString
	 * @param beanPostfix
	 * @return
	 */
	private String createJndiConnectionObjects (final String beanString, String beanPostfix) {
		String jndiStringName = generateBeanNameLowCamelStyle(beanString, beanPostfix);
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

	/**
	 *
	 * @param mockIncomeQueueName
	 * @return
	 */
	private boolean isBeanIdInList (String mockIncomeQueueName) {
		return listBeansId.contains(mockIncomeQueueName);
	}

	/**
	 *
	 * @param string
	 * @param postfix
	 * @return
	 */
	private String generateBeanNameLowCamelStyle (String string, String postfix) {
		String   beanName   = string.replaceAll("\\W", ":").toLowerCase();
		String[] namesParts = beanName.split(":");
		for (int i = 1; i < namesParts.length; i++)
			namesParts[i] = namesParts[i].replace(String.valueOf(namesParts[i].charAt(0)), String.valueOf
					(namesParts[i].charAt(0)).toUpperCase());
		beanName = "";
		for (String namesPart : namesParts)
			beanName += namesPart;
		return beanName + postfix;
	}

}
