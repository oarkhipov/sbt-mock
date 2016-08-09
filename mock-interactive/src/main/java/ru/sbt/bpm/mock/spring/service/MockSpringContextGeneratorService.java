package ru.sbt.bpm.mock.spring.service;

import generated.springframework.beans.Bean;
import generated.springframework.beans.Beans;
import generated.springframework.integration.MappingValueChannelType;
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
    private static final String BASE_CONFIG_IMPORT = "../contextConfigs/base-config.xml";
    // logginig config import path
    private static final String LOGGING_CONFIG_IMPORT = "../contextConfigs/logging-config.xml";

    /**
     * ************* classes constants    ***************
     */
    // java.lang.String class
    private static final String STRING_CLASS = "java.lang.String";
    // org.springframework.jndi.JndiObjectFactoryBean class
    private static final String JNDI_OBJECT_CLASS = "org.springframework.jndi.JndiObjectFactoryBean";
    // ru.sbt.bpm.mock.spring.bean.MessageAggregator class
    private static final String MESSAGE_AGGREGATOR_CLASS = "ru.sbt.bpm.mock.spring.bean.MessageAggregator";
    // ru.sbt.bpm.mock.config.enums.Protocol class
    private static final String PROTOCOL_CLASS = "ru.sbt.bpm.mock.config.enums.Protocol";

    /**
     * ************* properties constants  ***************
     */
    // jndiName property name
    private static final String JNDI_PROPERTY_NAME = "jndiName";
    // JMS constructor-arg value
    private static final String JMS_CONSTRUCTOR_ARG_VALUE = "JMS";
    // aggregate method name service activator
    private static final String AGGREGATE_SERVICE_ACTIVATOR_METHOD_NAME = "aggregate";
    // service activator expressions for generation response
    private static final String SERVICE_ACTIVATOR_RESPONSE_EXPRESSION = "payload.payload";
    // router payload
    private static final String ROUTER_PAYLOAD = "payload.jmsConnectionFactoryName+'_'+payload.queue";
    // default reply timeout
    private static final String DEFAULT_REPLY_TIMEOUT = "30000";
    // default receive timeout
    private static final String DEFAULT_RECEIVE_TIMEOUT = "30000";


    /**
     * *************   channels constants  ***************
     */
    // MockInboundRequestLogger channel
    private static final String MOCK_INBOUNT_REQUEST_LOGGER_CHANNEL = "MockInboundRequestLogger";
    // MockOutboundResponseLogger channel
    private static final String MOCK_OUTBOUND_RESPONSELOGGER_CHANNEL = "MockOutboundResponseLogger";
    // DriverOutboundRequestLogger channel
    private static final String DRIVER_OUTBOUND_RESPONSELOGGER_CHANNEL = "DriverOutboundRequestLogger";
    // DriverInboundResponseLogger channel
    private static final String DRIVER_INBOUND_RESPONSELOGGER_CHANNEL = "DriverInboundResponseLogger";
    // MockInboundRequestAggregated channel
    private static final String MOCK_INBOUND_REQUEST_AGGREGATED = "MockInboundRequestAggregated";
    // MockOutboundRouterResponse channel
    private static final String MOCK_OUTBOUND_ROUTER_RESPONSE = "MockOutboundRouterResponse";
    // DriverRouterChannelInput channel
    private static final String DRIVER_ROUTER_CHANNEL_INPUT = "DriverRouterChannelInput";
    // DriverOutboundRequest channel
    private static final String DRIVER_INBOUND_RESPONSE = "DriverInboundResponse";

    /**
     * *************   mock servlet elements prefixes & postfixes  ***************
     */
    // jndiConnectionFactory
    private static final String JNDI_CONNECTION_FACTORY_POSTFIX = "_jndiConnectionFactory";
    // Queue postfix
    private static final String QUEUE_POSTFIX = "_queue";
    // channel postfix
    private static final String CHANNEL_POSTFIX = "_channel";
    // jms inbound gateway postfix
    private static final String JMS_INBOUND_GATEWAY_POSTFIX = "_jmsin";
    // jms outbound gateway postfix
    private static final String JMS_OUTBOUND_GATEWAY_POSTFIX = "_jmsout";
    // gateway postfix
    private static final String GATEWAY_POSTFIX = "_gateway";
    // router channel postfix
    private static final String ROUTER_CHANNEL_POSTFIX = "_routerChannel";
    // router mock name
    private static final String MOCK_ROUTER_NAME = "MockRouter";
    // router driver name
    private static final String DRIVER_ROUTER_NAME = "DriverRouter";

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

    @Autowired
    private JndiNameService jndiNameService;

    @Getter
    private Beans beans;

    private Set<ru.sbt.bpm.mock.config.entities.System> systems;

    // список созданных id beans
    private List<String> listBeansId;

    // Map созданных каналов для mock по системе
    private Map<String, Tuple2<String, String>> mapMockChannels;

    // Map созданных каналов для driver по системе
    private Map<String, Tuple2<String, String>> mapDriverChannels;

    @PostConstruct
    private void init() {
        systems = container.getConfig().getSystems().getSystems();
        listBeansId = new TreeList<String>();
        mapMockChannels = new HashMap<String, Tuple2<String, String>>();
        mapDriverChannels = new HashMap<String, Tuple2<String, String>>();
        initBeans();
    }

    /**
     * Инициализация <beans></beans>
     */
    private void initBeans() {
        beans = beansConstructor.createBeans();
        beans = beansConstructor.createImports(beans, Arrays.asList(BASE_CONFIG_IMPORT, LOGGING_CONFIG_IMPORT));
    }

    /**
     * Очистка всех составляющих
     */
    private void clean() {
        systems = container.getConfig().getSystems().getSystems();
        listBeansId.clear();
        mapDriverChannels.clear();
        mapMockChannels.clear();
    }

    public void reInit() {
        clean();
        initBeans();
    }

    /**
     * Генерация mockapp-servlet
     *
     * @return
     */
    public Beans generateContext() {
        if (systems != null)
            for (ru.sbt.bpm.mock.config.entities.System system : systems) {
                if ( (system.getProtocol() == Protocol.JMS) && system.getEnabled()) {
                    /**
                     * Настройка используемой конфигурации заглушки
                     */
                    String queueConnectionFactory = system.getQueueConnectionFactory();
                    String mockInputString = system.getMockIncomeQueue();
                    String mockOutputString = system.getMockOutcomeQueue();
                    String driverOutputString = system.getDriverOutcomeQueue();
                    String driverInputString = system.getDriverIncomeQueue();

                    if (jndiExist(queueConnectionFactory)) {
                        /**
                         * Создаем настройки подлючения к jms
                         */
                        final String CF_SUFFIX = makeSuffix(queueConnectionFactory);
                        String jndiFactoryName = createJndiConnectionObjects(queueConnectionFactory,
                                JNDI_CONNECTION_FACTORY_POSTFIX, String.format("Connection Factory for jndi [%s]", queueConnectionFactory));

                        //Generating Mock JMS beans + channels + gateway
                        if (jndiExist(mockInputString, mockOutputString)) {
                            String jndiMockInboundQueueName = createJndiConnectionObjects(mockInputString, CF_SUFFIX + QUEUE_POSTFIX, String.format("Mock Inbound Queue for [%s]:[%s]", queueConnectionFactory, mockInputString));
                            String jndiMockOutboundQueueName = createJndiConnectionObjects(mockOutputString, CF_SUFFIX + QUEUE_POSTFIX, String.format("Mock Outbound Queue for [%s]:[%s]", queueConnectionFactory, mockOutputString));
                            /**
                             * Создание jms inbound & outbound
                             */
                            // Создание каналов inbound gateway
                            String mockInputChannel = createChannel(mockInputString,  CF_SUFFIX + CHANNEL_POSTFIX);

                            String mockOutputChannel = createChannel(mockOutputString, CF_SUFFIX + CHANNEL_POSTFIX);

                            mapMockChannels.put(system.getSystemName(), Tuple.of(mockInputChannel, mockOutputChannel));
                            // Создаем inbound-gateway
                            createJmsInboundGateway(queueConnectionFactory, jndiFactoryName, jndiMockInboundQueueName,
                                    jndiMockOutboundQueueName, mockInputChannel,
                                    mockOutputChannel, String.format("Inbound gateway for queues pair [%s]:[%s] -> [%s]:[%s]", queueConnectionFactory, mockInputString, queueConnectionFactory, mockOutputString));
                        }

                        // Generating Driver JMS beans + channels + gateway
                        if (jndiExist(driverOutputString, driverInputString)) {
                            String jndiDriverRequestQueueName = createJndiConnectionObjects(driverOutputString, CF_SUFFIX + QUEUE_POSTFIX, String.format("Driver Request Queue for [%s]:[%s]", queueConnectionFactory, driverOutputString));
                            String jndiDriverResponseQueueName = createJndiConnectionObjects(driverInputString, CF_SUFFIX + QUEUE_POSTFIX, String.format("Driver MessageTemplate Queue for [%s]:[%s]", queueConnectionFactory, driverInputString));

                            // Создание каналов outbound gateway
                            String driverRequestChannel = createChannel(driverOutputString, CF_SUFFIX + CHANNEL_POSTFIX);

                            // Создаем outbound-gateway
                            createJmsOutboundGateway(queueConnectionFactory, jndiDriverRequestQueueName,
                                    jndiDriverResponseQueueName, driverRequestChannel, DRIVER_INBOUND_RESPONSE,
                                    jndiFactoryName);
                        }

                    }
                }
            }

        /**
         * Маршрутизация
         */
        if (systems != null)
            for (ru.sbt.bpm.mock.config.entities.System system : systems) {
                if ((system.getProtocol() == Protocol.JMS) && system.getEnabled()) {
                    /**
                     * Mock
                     */
                    String mockIncomeQueue = system.getMockIncomeQueue();
                    String systemQueueConnectionFactory = system.getQueueConnectionFactory();
                    final String CF_SUFFIX = makeSuffix(systemQueueConnectionFactory);

                    if (jndiExist(mockIncomeQueue)) {
                        createServiceActivatorWithBean(mapMockChannels.get(system.getSystemName()).getT1(), new String[]{systemQueueConnectionFactory, mockIncomeQueue}, String.format("Aggregator for [%s]:[%s]", systemQueueConnectionFactory, mockIncomeQueue));

                        String systemMockOutcomeQueue = system.getMockOutcomeQueue();
                        // mockRouterChannel
                        String mockRouterChannel = createChannel(systemMockOutcomeQueue, ROUTER_CHANNEL_POSTFIX, null);
                        //MockOutboundChannel
                        String mockOutboundChannel = generateBeanNameLowCamelStyle(systemMockOutcomeQueue, CF_SUFFIX + CHANNEL_POSTFIX);

                        // router
                        createRouter(MOCK_ROUTER_NAME, MOCK_OUTBOUND_ROUTER_RESPONSE, systemQueueConnectionFactory, systemMockOutcomeQueue, mockRouterChannel, "MOCK ROUTER");
                        // service activator

                        createServiceActivatorWithExpressions(mockRouterChannel, mockOutboundChannel, String.format("Extractor service activator for [%s]:[%s]", systemQueueConnectionFactory, systemMockOutcomeQueue));
                    }
                    /**
                     * Driver
                     */
                    String driverOutcomeQueue = system.getDriverOutcomeQueue();
                    if (jndiExist(driverOutcomeQueue)) {
                        // driver channel
                        String driverRouterChannel = createChannel(driverOutcomeQueue, ROUTER_CHANNEL_POSTFIX, null);
                        // router
                        createRouter(DRIVER_ROUTER_NAME, DRIVER_ROUTER_CHANNEL_INPUT, systemQueueConnectionFactory, driverOutcomeQueue, driverRouterChannel, "DRIVER ROUTER");
                        // service activator
                        createServiceActivatorWithExpressions(driverRouterChannel, generateBeanNameLowCamelStyle(driverOutcomeQueue, CF_SUFFIX + CHANNEL_POSTFIX), String.format("Extractor service activator for [%s]:[%s]", systemQueueConnectionFactory, driverOutcomeQueue));
                    }
                }
            }
        return beans;
    }

    private boolean jndiExist(String... jndiNames) {
        for (String jndiName : jndiNames) {
            if (jndiName == null) return false;
            if (jndiName.isEmpty()) return false;
            if (!jndiNameService.isExist(jndiName)) return false;
        }
        return true;
    }

    /**
     * @param routerOutboundResponseChannel
     * @param mockOutputChannel
     * @param comment
     */

    private void createServiceActivatorWithExpressions(String routerOutboundResponseChannel, String mockOutputChannel, String comment) {
        beans = integrationConstructor.createServiceActivator(beans, routerOutboundResponseChannel, mockOutputChannel, SERVICE_ACTIVATOR_RESPONSE_EXPRESSION, comment);
    }

    /**
     * @param queueConnectionFactory
     * @param outputQueue
     * @param routerOutboundResponseChannel
     * @param comment
     */
    private void createRouter(String routerName, String outboundRouterResponse, String queueConnectionFactory, String outputQueue, String routerOutboundResponseChannel, String comment) {
        if (!isBeanIdInList(routerName)) {
            listBeansId.add(routerName);
            beans = integrationConstructor.createRouter(beans, routerName, ROUTER_PAYLOAD, outboundRouterResponse, createRouterMappings(queueConnectionFactory, outputQueue, routerOutboundResponseChannel), comment);
        } else {
            for (Object obj : beans.getImportOrAliasOrBean())
                if (obj instanceof JAXBElement) {
                    @SuppressWarnings("unchecked")
                    JAXBElement<RouterType> element = (JAXBElement<RouterType>) obj;
                    RouterType router = element.getValue();
                    if (router.getId().equals(routerName)) {
                        MappingValueChannelType newMapping = integrationConstructor.createMapping(queueConnectionFactory + "_" +
                                        outputQueue, routerOutboundResponseChannel);
                        //check for mapping duplicates
                        for (MappingValueChannelType mappingValueChannelType : router.getMapping()) {
                            if (mappingValueChannelType.getValue().equals(newMapping.getValue())
                                    &&
                                    mappingValueChannelType.getChannel().equals(newMapping.getChannel())) {
                                return;
                            }
                        }
                        router.getMapping().add(newMapping);
                    }
                }
        }
    }

    /**
     * @param queueConnectionFactory
     * @param outputQueue
     * @param outputChannel
     * @return
     */
    private List<Tuple2<String, String>> createRouterMappings(String queueConnectionFactory, String outputQueue, String outputChannel) {
        List<Tuple2<String, String>> mappings = new ArrayList<Tuple2<String, String>>();
        mappings.add(Tuple.of(queueConnectionFactory + "_" + outputQueue, outputChannel));
        return mappings;
    }

    /**
     * <service-activator input-channel="" output-channel="" method="">
     * <bean class="">
     * <constructor-agr value="" type=""/>
     * ...
     * <constructor-agr value="" type=""/>
     * </bean>
     * </service-activator>
     *
     * @param mockInputChannel
     * @param constructorArgsValue
     */
    private void createServiceActivatorWithBean(String mockInputChannel, String[] constructorArgsValue, String comment) {
        beans = integrationConstructor.createServiceActivator(beans, mockInputChannel, MOCK_INBOUND_REQUEST_AGGREGATED, AGGREGATE_SERVICE_ACTIVATOR_METHOD_NAME, createBeanForServiceActivator(constructorArgsValue), comment);
    }

    /**
     * <constructor-agr value="" type=""/>
     * ...
     * <constructor-agr value="" type=""/>
     *
     * @param constructorArgsValue
     * @return
     */
    private List<Tuple2<String, String>> createConstructorArgsForBean(String... constructorArgsValue) {
        List<Tuple2<String, String>> list = new ArrayList<Tuple2<String, String>>();
        list.add(Tuple.of(JMS_CONSTRUCTOR_ARG_VALUE, PROTOCOL_CLASS));
        for (String arg : constructorArgsValue)
            list.add(Tuple.of(arg, STRING_CLASS));
        return list;
    }

    /**
     * <bean class="">
     * <constructor-agr value="" type=""/>
     * ...
     * <constructor-agr value="" type=""/>
     * </bean>
     *
     * @param constructorArgsValue
     * @return
     */
    private Bean createBeanForServiceActivator(String... constructorArgsValue) {
        return beansConstructor.createBean(MESSAGE_AGGREGATOR_CLASS, createConstructorArgsForBean(constructorArgsValue));
    }

    /**
     * <jms:outbound-gateway id="" request-destination="" reply-destination="" request-channel=""
     * reply-channel="" header-mapper="" connection-factory="" reply-timeout="" receive-timeout="" />
     *
     * @param connectionFactoryName
     * @param jndiDriverRequest
     * @param jndiDriverResponse
     * @param driverRequestChannel
     * @param driverResponseChannel
     * @param jndiConnectionFactory
     * @return
     */
    private String createJmsOutboundGateway(String connectionFactoryName, String jndiDriverRequest, String
            jndiDriverResponse, String driverRequestChannel, String driverResponseChannel, String
                                                    jndiConnectionFactory) {
        String jmsBeanName = generateBeanNameLowCamelStyle(connectionFactoryName, JMS_OUTBOUND_GATEWAY_POSTFIX);

        String timeout = DEFAULT_REPLY_TIMEOUT;
        String configDriverTimeout = container.getConfig().getMainConfig().getDriverTimeout();
        if (configDriverTimeout != null && !configDriverTimeout.isEmpty()) {
            timeout = configDriverTimeout;
        }

        if (!isBeanIdInList(jmsBeanName)) {
            listBeansId.add(jmsBeanName);
            beans = jmsConstructor.createOutboundGateway(beans, jmsBeanName, jndiDriverRequest, jndiDriverResponse,
                    driverRequestChannel, driverResponseChannel,
                    "defHeaderMapper", jndiConnectionFactory, timeout, timeout);
        }
        return jmsBeanName;
    }

    /**
     * <jms:inbound-gateway id="" request-destination="" request-channel="" reply-channel=""
     * default-reply-destination="" connection-factory="" />
     *
     * @param connectionFactoryName
     * @param jndiConnectionFactory
     * @param jndiMockInputString
     * @param jndiMockOutputString
     * @param mockInputChannel
     * @param mockOutputChannel
     * @param comment
     * @return
     */
    private String createJmsInboundGateway(String connectionFactoryName, String jndiConnectionFactory, String
            jndiMockInputString, String jndiMockOutputString, String mockInputChannel, String mockOutputChannel, String comment) {
        String jmsBeanName = generateBeanNameLowCamelStyle(connectionFactoryName, makeSuffix(mockInputChannel) + makeSuffix(mockOutputChannel) +JMS_INBOUND_GATEWAY_POSTFIX);
        if (!isBeanIdInList(jmsBeanName)) {
            listBeansId.add(jmsBeanName);
            beans = jmsConstructor.createInboundGateway(beans, jmsBeanName, jndiMockInputString, mockInputChannel,
                    mockOutputChannel, jndiMockOutputString,
                    jndiConnectionFactory, comment);
        }
        return jmsBeanName;
    }

    private String createChannel(String beanString, String channelPostfix) {
        return createChannel(beanString, channelPostfix, null);
    }

    /**
     * Создание канала
     * <int:channel id=""/>
     *
     * @param beanString
     * @param comment
     * @return
     */
    private String createChannel(String beanString, String channelPostfix, String comment) {
        String channelName = generateBeanNameLowCamelStyle(beanString, channelPostfix);
        if (!isBeanIdInList(channelName)) {
            listBeansId.add(channelName);
            beans = integrationConstructor.createChannel(beans, channelName, comment);
        }
        return channelName;
    }

    /**
     * Созджание настроек для для подключения jms
     * <bean id="" class="">
     * <property name="" value="#{}"/>
     * </bean>
     *
     * @param beanString
     * @param beanPostfix
     * @param comment
     * @return
     */
    private String createJndiConnectionObjects(final String beanString, String beanPostfix, String comment) {
        String jndiStringName = generateBeanNameLowCamelStyle(beanString, beanPostfix);
        if (!isBeanIdInList(jndiStringName)) {
            listBeansId.add(jndiStringName);
            beans = beansConstructor.createBean(beans, JNDI_OBJECT_CLASS, jndiStringName, new HashMap<String,
                    String>() {
                {
                    put(JNDI_PROPERTY_NAME, beanString);
                }
            }, comment);
        }
        return jndiStringName;
    }

    /**
     * @param mockIncomeQueueName
     * @return
     */
    private boolean isBeanIdInList(String mockIncomeQueueName) {
        return listBeansId.contains(mockIncomeQueueName);
    }

    /**
     * @param string
     * @param postfix
     * @return
     */
    private String generateBeanNameLowCamelStyle(String string, String postfix) {
        String beanName = string.replaceAll("\\W", ":").toLowerCase();
        String[] namesParts = beanName.split(":");
        for (int i = 1; i < namesParts.length; i++)
            namesParts[i] = namesParts[i].replace(String.valueOf(namesParts[i].charAt(0)), String.valueOf
                    (namesParts[i].charAt(0)).toUpperCase());
        beanName = "";
        for (String namesPart : namesParts)
            beanName += namesPart;
        return beanName + postfix;
    }

    private String makeSuffix(String string) {
        return "_" + generateBeanNameLowCamelStyle(string,"");
    }

}
