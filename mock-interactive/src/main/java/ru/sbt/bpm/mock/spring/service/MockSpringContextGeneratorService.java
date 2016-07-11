package ru.sbt.bpm.mock.spring.service;

import generated.springframework.beans.Beans;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.list.TreeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.context.generator.service.constructors.BeansConstructor;
import ru.sbt.bpm.mock.context.generator.service.constructors.IntegrationConstructor;
import ru.sbt.bpm.mock.context.generator.service.constructors.JmsIntegrationConstructor;

import javax.annotation.PostConstruct;
import java.util.Arrays;
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
	private static final String JNDI_PROPERTY_NAME_           = "jndiName";
	// JMS constructor-arg value
	private static final String JMS_CONSTRUCTOR_ARG_VALUE     = "JMS";
	// sendMockMessage method gateway name
	private static final String SEND_MOCK_MESSAGE_METHOD_NAME = "sendMockMessage";

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
	private static final String QUEUE_CONNECTION_FACTORY_STRING_BEAN = "connectionFactory";
	// mockConnectionInputString bean
	private static final String MOCK_CONNECTION_INPUT_STRING_BEAN    = "mockConnectionInput";
	// mockConnectionOutputString bean
	private static final String MOCK_CONNECTION__OUTPUT_STRING_BEAN  = "mockConnectionOutput";
	// driverConnectionOutputString bean
	private static final String DRIVER_CONNECTION_OUTPUT_STRING_BEAN = "driverConnectionOutput";
	// driverConnectionInputString bean
	private static final String DRIVER_CONNECTION_INPUT_STRING_BEAN  = "driverConnectionInput";
	// jndiConnectionFactory
	private static final String JNDI_CONNECTION_FACTORY_POSTFIX      = "_jndiConnectionFactory";
	// Queue postfix
	private static final String QUEUE_POSTFIX                        = "_Queue";
	// channel postfix
	private static final String CHANNEL_POSTFIX                      = "_Channel";

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
				String connectionFactoryName = deleteSpesialCharsFromString(system.getQueueConnectionFactory()) +
				                               QUEUE_CONNECTION_FACTORY_STRING_BEAN;
				System.out.println(connectionFactoryName);
			}

			if (system.getProtocol() == Protocol.SOAP) {

			}
		}

		return beans;
	}

	private String deleteSpesialCharsFromString (String string) {
		return string.replaceAll("\\W", "");
	}

}
