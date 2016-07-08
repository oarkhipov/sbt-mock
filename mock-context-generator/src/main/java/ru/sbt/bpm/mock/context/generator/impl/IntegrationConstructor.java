package ru.sbt.bpm.mock.context.generator.impl;

import generated.springframework.beans.Bean;
import generated.springframework.beans.Beans;
import generated.springframework.integration.*;
import lombok.NonNull;
import reactor.tuple.Tuple2;
import reactor.tuple.Tuple3;
import ru.sbt.bpm.mock.context.generator.IContextGeneratable;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */

public class IntegrationConstructor implements IContextGeneratable {


	/**
	 * <channel id="">
	 * <interceptors>
	 * <wire-tap channel=""/>
	 * <wire-tap channel=""/>
	 * </interceptors>
	 * </channel>
	 *
	 * @param beans - <beans/>
	 * @param channelId - id канала
	 * @return
	 */
	public Beans createChannel (Beans beans, String channelId) {
		return createChannelInternal(beans, channelId, new ArrayList<String>());
	}

	/**
	 * <channel id="">
	 * <interceptors>
	 * <wire-tap channel=""/>
	 * <wire-tap channel=""/>
	 * </interceptors>
	 * </channel>
	 *
	 * @param beans <beans/>
	 * @param channelId - id канала
	 * @param wireTapChannels - список логеров
	 * @return
	 */
	public Beans createChannel (Beans beans, String channelId, List<String> wireTapChannels) {
		return createChannelInternal(beans, channelId, wireTapChannels);
	}

	/**
	 * <int:service-activator input-channel="" output-channel="" expression=""/>
	 *
	 * @param beans - <beans/>
	 * @param inputChannel - входной канал
	 * @param outputChannel - выходной канал
	 * @param expressions - выражение
	 * @return
	 */
	public Beans createServiceActivator (Beans beans, String inputChannel, String outputChannel, String expressions) {
		return createServiceActivator(beans, inputChannel, outputChannel, null, null, expressions);
	}

	/**
	 * <int:service-activator input-channel="" output-channel="" method="">
	 * <bean class="">
	 * <constructor-arg value="" type=""/>
	 * <constructor-arg value="" type=""/>
	 * <constructor-arg value="" type=""/>
	 * </bean>
	 * </int:service-activator>
	 *
	 * @param beans - <beans/>
	 * @param inputChannel  - входной канал
	 * @param outputChannel -выходной канал
	 * @param methodName - имя методя
	 * @param bean - <bean><constructor-arg value="" type=""/></bean>
	 * @return
	 */
	public Beans createServiceActivator (Beans beans, String inputChannel, String outputChannel, String methodName,
	                                     Bean bean) {
		return createServiceActivator(beans, inputChannel, outputChannel, methodName, bean, null);
	}

	/**
	 * <beans>
	 * <int:gateway id="" error-channel="" service-interface="" default-reply-timeout="" default-request-timeout="">
	 * <int:method name="" request-channel="" reply-channel=""/>
	 * </int:gateway>
	 * </beans>
	 *
	 * @param beans <int:method name="" request-channel="" reply-channel=""/>
	 * @param id - id gateway
	 * @param errorChannel - канал ошибок
	 * @param serviceInterface - интерфейс отправки сообщений
	 * @param defaultReplyTimeout - задержка отправки
	 * @param defaultRequestTimeout - задержка ответа
	 * @param methodArgs <int:method name="" request-channel="" reply-channel=""/>
	 * @return
	 */
	public Beans createGateway (Beans beans, String id, String errorChannel, String serviceInterface, String
			defaultReplyTimeout, String defaultRequestTimeout, List<Tuple3<String,
			String, String>> methodArgs) {
		return createGatewayInternal(beans, id, errorChannel, serviceInterface, defaultReplyTimeout,
		                             defaultRequestTimeout, methodArgs);
	}

	/**
	 * @param beans <int:method name="" request-channel="" reply-channel=""/>
	 * @param id - id router
	 * @param expression - выражение
	 * @param inputChannel - входной канал
	 * @param mappings - парамерты для mapping, где
	 *                     - getT1() - value
	 *                     - getT2() -channel
	 * @return
	 */
	public Beans createRouter (Beans beans, String id, String expression, String inputChannel, List<Tuple2<String,
			String>> mappings) {
		return createRouterInternal(beans, id, expression, inputChannel, mappings);
	}

	/**
	 * @param beans
	 * @param id
	 * @param expression
	 * @param inputChannel
	 * @param mappings     - парамерты для mapping, где
	 *                     - getT1() - value
	 *                     - getT2() -channel
	 * @return
	 */
	private Beans createRouterInternal (Beans beans, String id, String expression, String inputChannel,
	                                    List<Tuple2<String, String>> mappings) {
		beans.getImportOrAliasOrBean().add(createRouter(createRouterType(id, expression, inputChannel, createMappings
				(mappings))));
		return beans;
	}

	/**
	 * <router></router>
	 *
	 * @param routerType
	 * @return
	 */
	private JAXBElement<RouterType> createRouter (RouterType routerType) {
		return integrationFactory.createRouter(routerType);
	}

	/**
	 * <int:mapping value="" channel=""/>
	 * <int:mapping value="" channel=""/>
	 * <int:mapping value="" channel=""/>
	 *
	 * @param mappingsParams - парамерты для mapping, где
	 *                       - getT1() - value
	 *                       - getT2() -channel
	 * @return
	 */
	private List<MappingValueChannelType> createMappings (List<Tuple2<String, String>> mappingsParams) {
		List<MappingValueChannelType> list = new ArrayList<MappingValueChannelType>();
		for (Tuple2<String, String> mappingsParam : mappingsParams)
			list.add(createMapping(mappingsParam.getT1(), mappingsParam.getT2()));
		return list;
	}

	/**
	 * <int:mapping value="" channel=""/>
	 *
	 * @param value
	 * @param channel
	 * @return
	 */
	private MappingValueChannelType createMapping (String value, String channel) {
		MappingValueChannelType mapping = integrationFactory.createMappingValueChannelType();
		mapping.setValue(value);
		mapping.setChannel(channel);
		return mapping;
	}

	/**
	 * @param id
	 * @param expression
	 * @param inputChannel
	 * @param mappings
	 * @return
	 */
	private RouterType createRouterType (String id, String expression, String inputChannel,
	                                     List<MappingValueChannelType> mappings) {
		RouterType routerType = integrationFactory.createRouterType();
		routerType.setId(id);
		routerType.setStrExpression(expression);
		routerType.setInputChannel(inputChannel);
		routerType.getMapping().addAll(mappings);
		return routerType;
	}

	/**
	 * <beans>
	 * <int:gateway id="" error-channel="" service-interface="" default-reply-timeout="" default-request-timeout="">
	 * <int:method name="" request-channel="" reply-channel=""/>
	 * </int:gateway>
	 * </beans>
	 *
	 * @param beans
	 * @param id
	 * @param errorChannel
	 * @param serviceInterface
	 * @param defaultReplyTimeout
	 * @param defaultRequestTimeout
	 * @param methodArgs
	 * @return
	 */
	private Beans createGatewayInternal (Beans beans, String id, String errorChannel, String serviceInterface, String
			defaultReplyTimeout, String defaultRequestTimeout, List<Tuple3<String, String, String>> methodArgs) {
		beans.getImportOrAliasOrBean().add(createGateway(id, errorChannel, serviceInterface, defaultReplyTimeout,
		                                                 defaultRequestTimeout, createListGateMethods(methodArgs)));
		return beans;
	}

	/**
	 * @param methodArgs - список аргуметов method принимается как Tuple3, где
	 *                   - getT1() - methodName;
	 *                   - getT2() - requestChannel;
	 *                   - getT3() - replyChannel.
	 * @return
	 */
	private List<GateMethod> createListGateMethods (List<Tuple3<String, String, String>> methodArgs) {
		List<GateMethod> list = new ArrayList<GateMethod>();
		for (Tuple3<String, String, String> methodArg : methodArgs)
			list.add(createGateMethod(methodArg.getT1(), methodArg.getT2(), methodArg.getT3()));
		return list;
	}

	/**
	 * <int:method name="" request-channel="" reply-channel=""/>
	 *
	 * @param methodName
	 * @param requestChannel
	 * @param replyChannel
	 * @return
	 */
	private GateMethod createGateMethod (String methodName, String requestChannel, String replyChannel) {
		GateMethod gateMethod = integrationFactory.createGateMethod();
		gateMethod.setName(methodName);
		gateMethod.setRequestChannel(requestChannel);
		gateMethod.setReplyChannel(replyChannel);
		return gateMethod;
	}

	/**
	 * <int:gateway id="" error-channel="" service-interface="" default-reply-timeout="" default-request-timeout="">
	 * <int:method name="" request-channel="" reply-channel=""/>
	 * </int:gateway>
	 *
	 * @param id
	 * @param errorChannel
	 * @param serviceInterface
	 * @param defaultReplyTimeout
	 * @param defaultRequestTimeout
	 * @param gateMethods
	 * @return
	 */
	private Gateway createGateway (String id, String errorChannel, String serviceInterface, String
			defaultReplyTimeout, String defaultRequestTimeout, List<GateMethod> gateMethods) {
		Gateway gateway = integrationFactory.createGateway();
		gateway.setId(id);
		gateway.setErrorChannel(errorChannel);
		gateway.setServiceInterface(serviceInterface);
		gateway.setDefaultReplyTimeout(defaultReplyTimeout);
		gateway.setDefaultRequestTimeout(defaultRequestTimeout);
		gateway.getMethod().addAll(gateMethods);
		return gateway;
	}

	/**
	 * @param beans
	 * @param inputChannel
	 * @param outputChannel
	 * @param methodName
	 * @param bean
	 * @param expression
	 * @return
	 */
	private Beans createServiceActivator (Beans beans, @NonNull String inputChannel, @NonNull String outputChannel,
	                                      String methodName, Bean bean, String expression) {
		beans.getImportOrAliasOrBean().add(createServiceActivator(inputChannel, outputChannel, methodName, bean,
		                                                          expression));
		return beans;
	}

	/**
	 * @param inputChannel
	 * @param outputChannel
	 * @param methodName
	 * @param bean
	 * @param expression
	 * @return
	 */
	private ServiceActivator createServiceActivator (@NonNull String inputChannel, @NonNull String outputChannel,
	                                                 String methodName, Bean bean, String expression) {
		ServiceActivator serviceActivator = integrationFactory.createServiceActivator();
		serviceActivator.setInputChannel(inputChannel);
		serviceActivator.setOutputChannel(outputChannel);
		if (methodName != null && !methodName.isEmpty())
			serviceActivator.setMethod(methodName);
		if (bean != null)
			serviceActivator.getPollerOrExpressionOrRequestHandlerAdviceChain().add(bean);
		if (expression != null && !expression.isEmpty())
			serviceActivator.setStrExpression(expression);
		return serviceActivator;
	}

	/**
	 * <channel id="">
	 * <interceptors>
	 * <wire-tap channel=""/>
	 * <wire-tap channel=""/>
	 * </interceptors>
	 * </channel>
	 *
	 * @param beans
	 * @param channelId
	 * @param wireTapChannels
	 * @return
	 */
	private Beans createChannelInternal (Beans beans, String channelId, List<String> wireTapChannels) {
		beans.getImportOrAliasOrBean().add(createChannel(channelId, wireTapChannels));
		return beans;
	}

	/**
	 * <wire-tap channel=""/>
	 *
	 * @param wireTapChannel
	 * @return
	 */
	private WireTap createWireTap (String wireTapChannel) {
		WireTap wireTap = integrationFactory.createWireTap();
		wireTap.setChannel(wireTapChannel);
		return wireTap;
	}

	/**
	 * Список wire-tap
	 *
	 * @param wireTapChannels
	 * @return
	 */
	private List<WireTap> createListWireTap (List<String> wireTapChannels) {
		List<WireTap> listWireTap = new ArrayList<WireTap>();
		for (String wireTapChannel : wireTapChannels)
			listWireTap.add(createWireTap(wireTapChannel));
		return listWireTap;
	}

	/**
	 * <interceptors>
	 * <wire-tap channel=""/>
	 * <wire-tap channel=""/>
	 * </interceptors>
	 *
	 * @param wireTapChannels
	 * @return
	 */
	private ChannelInterceptorsType createInterceptorsType (List<String> wireTapChannels) {
		ChannelInterceptorsType interceptorsType = integrationFactory.createChannelInterceptorsType();
		interceptorsType.getRefOrWireTapOrAny().addAll(createListWireTap(wireTapChannels));
		return interceptorsType;
	}

	/**
	 * <channel id="">
	 * <interceptors>
	 * <wire-tap channel=""/>
	 * <wire-tap channel=""/>
	 * </interceptors>
	 * </channel>
	 *
	 * @param channelId
	 * @param wireTapChannels
	 * @return
	 */
	private Channel createChannel (@NonNull String channelId, List<String> wireTapChannels) {
		Channel channel = integrationFactory.createChannel();
		channel.setId(channelId);
		if (wireTapChannels != null && !wireTapChannels.isEmpty())
			channel.setInterceptors(createInterceptorsType(wireTapChannels));
		return channel;
	}

	public ObjectFactory getIntegrationFactory () {
		return integrationFactory;
	}
}
