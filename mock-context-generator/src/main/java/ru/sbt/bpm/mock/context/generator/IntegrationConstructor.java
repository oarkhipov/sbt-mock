package ru.sbt.bpm.mock.context.generator;

import generated.springframework.integration.GateMethod;
import generated.springframework.integration.WireTap;
import lombok.NonNull;
import reactor.tuple.Tuple3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */

public class IntegrationConstructor implements IContextGeneratable {


	/**
	 * <channel id="">
	 *     <interceptors>
	 *         <wire-tap channel=""/>
	 *         <wire-tap channel=""/>
	 *     </interceptors>
	 * </channel>
	 * @param beans
	 * @param channelId
	 * @return
	 */
	public generated.springframework.beans.Beans createChannel(generated.springframework.beans.Beans beans, String channelId) {
		return createChannelInternal(beans, channelId, new ArrayList<String>());
	}

	/**
	 * <channel id="">
	 *     <interceptors>
	 *         <wire-tap channel=""/>
	 *         <wire-tap channel=""/>
	 *     </interceptors>
	 * </channel>
	 * @param beans
	 * @param channelId
	 * @param wireTapChannels
	 * @return
	 */
	public generated.springframework.beans.Beans createChannel(generated.springframework.beans.Beans beans, String channelId, List<String> wireTapChannels) {
		return createChannelInternal(beans, channelId, wireTapChannels);
	}

	/**
	 * <int:service-activator input-channel="" output-channel="" expression=""/>
	 * @param beans
	 * @param inputChannel
	 * @param outputChannel
	 * @param expressions
	 * @return
	 */
	public generated.springframework.beans.Beans createServiceActivator(generated.springframework.beans.Beans beans, String inputChannel, String outputChannel, String expressions) {
		return createServiceActivator(beans, inputChannel, outputChannel, null, null, expressions);
	}

	/**
	 * <int:service-activator input-channel="" output-channel="" method="">
	 *     <bean class="">
	 *         <constructor-arg value="" type=""/>
	 *         <constructor-arg value="" type=""/>
	 *         <constructor-arg value="" type=""/>
	 *     </bean>
	 * </int:service-activator>
	 * @param inputChannel
	 * @param outputChannel
	 * @param methodName
	 * @param bean
	 * @return
	 */
	public generated.springframework.beans.Beans createServiceActivator (generated.springframework.beans.Beans beans, String inputChannel, String outputChannel, String methodName, generated.springframework.beans.Bean bean) {
		return createServiceActivator(beans, inputChannel, outputChannel, methodName, bean, null);
	}

	/**
	 * <beans>
	 *    <int:gateway id="" error-channel="" service-interface="" default-reply-timeout="" default-request-timeout="">
	 *      <int:method name="" request-channel="" reply-channel=""/>
	 *    </int:gateway>
	 * </beans>
	 * @param beans
	 * @param id
	 * @param errorChannel
	 * @param serviceInterface
	 * @param defaultReplyTimeout
	 * @param defaultRequestTimeout
	 * @param methodArgs
	 * @return
	 */
	public generated.springframework.beans.Beans createGateway(generated.springframework.beans.Beans beans, String id,
	                                                           String errorChannel, String serviceInterface, String defaultReplyTimeout, String defaultRequestTimeout, List<Tuple3<String, String, String>> methodArgs) {
		return createGatewayInternal(beans, id, errorChannel, serviceInterface, defaultReplyTimeout, defaultRequestTimeout, methodArgs);
	}

	/**
	 * <beans>
	 *    <int:gateway id="" error-channel="" service-interface="" default-reply-timeout="" default-request-timeout="">
	 *      <int:method name="" request-channel="" reply-channel=""/>
	 *    </int:gateway>
	 * </beans>
	 * @param beans
	 * @param id
	 * @param errorChannel
	 * @param serviceInterface
	 * @param defaultReplyTimeout
	 * @param defaultRequestTimeout
	 * @param methodArgs
	 * @return
	 */
	private generated.springframework.beans.Beans createGatewayInternal(generated.springframework.beans.Beans beans, String id, String errorChannel, String serviceInterface, String defaultReplyTimeout, String defaultRequestTimeout, List<Tuple3<String, String, String>> methodArgs) {
		beans.getImportOrAliasOrBean().add(createGateway(id, errorChannel, serviceInterface, defaultReplyTimeout, defaultRequestTimeout, createListGateMethods(methodArgs)));
		return beans;
	}

	/**
	 *
	 * @param methodArgs - список аргуметов method принимается как Tuple3, где
	 *  - getT1() - methodName;
	 *  - getT2() - requestChannel;
	 *  - getT3() - replyChannel.
	 * @return
	 */
	private List<generated.springframework.integration.GateMethod> createListGateMethods(List<Tuple3<String, String, String>> methodArgs) {
		List<generated.springframework.integration.GateMethod> list = new ArrayList<GateMethod>();
		for (Tuple3<String, String, String> methodArg : methodArgs)
			list.add(createGateMethod(methodArg.getT1(), methodArg.getT2(), methodArg.getT3()));
		return list;
	}

	/**
	 * <int:method name="" request-channel="" reply-channel=""/>
	 * @param methodName
	 * @param requestChannel
	 * @param replyChannel
	 * @return
	 */
	private generated.springframework.integration.GateMethod createGateMethod(String methodName, String requestChannel, String replyChannel) {
		generated.springframework.integration.GateMethod gateMethod = integrationFactory.createGateMethod();
		gateMethod.setName(methodName);
		gateMethod.setRequestChannel(requestChannel);
		gateMethod.setReplyChannel(replyChannel);
		return gateMethod;
	}

	/**
	 * <int:gateway id="" error-channel="" service-interface="" default-reply-timeout="" default-request-timeout="">
	 *      <int:method name="" request-channel="" reply-channel=""/>
	 * </int:gateway>
	 * @param id
	 * @param errorChannel
	 * @param serviceInterface
	 * @param defaultReplyTimeout
	 * @param defaultRequestTimeout
	 * @param gateMethods
	 * @return
	 */
	private generated.springframework.integration.Gateway createGateway(String id, String errorChannel, String serviceInterface, String defaultReplyTimeout, String defaultRequestTimeout, List<generated.springframework.integration.GateMethod> gateMethods) {
		generated.springframework.integration.Gateway gateway = integrationFactory.createGateway();
		gateway.setId(id);
		gateway.setErrorChannel(errorChannel);
		gateway.setServiceInterface(serviceInterface);
		gateway.setDefaultReplyTimeout(defaultReplyTimeout);
		gateway.setDefaultRequestTimeout(defaultRequestTimeout);
		gateway.getMethod().addAll(gateMethods);
		return gateway;
	}

	/**
	 *
	 * @param beans
	 * @param inputChannel
	 * @param outputChannel
	 * @param methodName
	 * @param bean
	 * @param expression
	 * @return
	 */
	 private generated.springframework.beans.Beans createServiceActivator (generated.springframework.beans.Beans beans, @NonNull String inputChannel, @NonNull String outputChannel, String methodName, generated.springframework.beans.Bean bean, String expression) {
		 beans.getImportOrAliasOrBean().add(createServiceActivator(inputChannel, outputChannel, methodName, bean, expression));
		 return beans;
	 }

	/**
	 *
	 * @param inputChannel
	 * @param outputChannel
	 * @param methodName
	 * @param bean
	 * @param expression
	 * @return
	 */
	private generated.springframework.integration.ServiceActivator createServiceActivator(@NonNull String inputChannel, @NonNull String outputChannel, String methodName, generated.springframework.beans.Bean bean, String expression) {
		generated.springframework.integration.ServiceActivator serviceActivator = integrationFactory.createServiceActivator();
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
	 *     <interceptors>
	 *         <wire-tap channel=""/>
	 *         <wire-tap channel=""/>
	 *     </interceptors>
	 * </channel>
	 * @param beans
	 * @param channelId
	 * @param wireTapChannels
	 * @return
	 */
	private generated.springframework.beans.Beans createChannelInternal (generated.springframework.beans.Beans beans,
	                                                                     String channelId, List<String> wireTapChannels) {
		beans.getImportOrAliasOrBean().add(createChannel(channelId, wireTapChannels));
		return beans;
	}

	/**
	 * <wire-tap channel=""/>
	 * @param wireTapChannel
	 * @return
	 */
	private generated.springframework.integration.WireTap createWireTap (String wireTapChannel) {
		generated.springframework.integration.WireTap wireTap = integrationFactory.createWireTap();
		wireTap.setChannel(wireTapChannel);
		return wireTap;
	}

	/**
	 * Список wire-tap
	 * @param wireTapChannels
	 * @return
	 */
	private List<generated.springframework.integration.WireTap> createListWireTap(List<String> wireTapChannels) {
		List<generated.springframework.integration.WireTap> listWireTap = new ArrayList<WireTap>();
		for (String wireTapChannel : wireTapChannels)
			listWireTap.add(createWireTap(wireTapChannel));
		return listWireTap;
	}

	/**
	 * <interceptors>
	 *     <wire-tap channel=""/>
	 *     <wire-tap channel=""/>
	 * </interceptors>
	 * @param wireTapChannels
	 * @return
	 */
	private generated.springframework.integration.ChannelInterceptorsType createInterceptorsType(List<String> wireTapChannels) {
		generated.springframework.integration.ChannelInterceptorsType interceptorsType = integrationFactory.createChannelInterceptorsType();
		interceptorsType.getRefOrWireTapOrAny().addAll(createListWireTap(wireTapChannels));
		return interceptorsType;
	}

	/**
	 * <channel id="">
	 *     <interceptors>
	 *         <wire-tap channel=""/>
	 *         <wire-tap channel=""/>
	 *     </interceptors>
	 * </channel>
	 * @param channelId
	 * @param wireTapChannels
	 * @return
	 */
	private generated.springframework.integration.Channel createChannel(@NonNull String channelId, List<String> wireTapChannels) {
		generated.springframework.integration.Channel channel = integrationFactory.createChannel();
		channel.setId(channelId);
		if (wireTapChannels != null && !wireTapChannels.isEmpty())
			channel.setInterceptors(createInterceptorsType(wireTapChannels));
		return channel;
	}

	public generated.springframework.integration.ObjectFactory getIntegrationFactory() {
		return integrationFactory;
	}
}
