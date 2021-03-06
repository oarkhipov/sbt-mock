/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ru.sbt.bpm.mock.spring.context.generator.service.constructors;

import generated.springframework.beans.Bean;
import generated.springframework.beans.Beans;
import generated.springframework.integration.*;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import reactor.tuple.Tuple2;
import reactor.tuple.Tuple3;
import ru.sbt.bpm.mock.spring.context.generator.IContextGeneratable;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */

@Service
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
		return createChannelInternal(beans, channelId, new ArrayList<String>(), null);
	}
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
	 * @param comment
	 * @return
	 */
	public Beans createChannel (Beans beans, String channelId, String comment) {
		return createChannelInternal(beans, channelId, new ArrayList<String>(), comment);
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
		return createChannelInternal(beans, channelId, wireTapChannels, null);
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
	 * @param comment
	 * @return
	 */
	public Beans createChannel (Beans beans, String channelId, List<String> wireTapChannels, String comment) {
		return createChannelInternal(beans, channelId, wireTapChannels, comment);
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
		return createServiceActivator(beans, inputChannel, outputChannel, null, null, expressions, null);
	}

	/**
	 * <int:service-activator input-channel="" output-channel="" expression=""/>
	 *
	 * @param beans - <beans/>
	 * @param inputChannel - входной канал
	 * @param outputChannel - выходной канал
	 * @param expressions - выражение
	 * @param comment
	 * @return
	 */
	public Beans createServiceActivator (Beans beans, String inputChannel, String outputChannel, String expressions, String comment) {
		return createServiceActivator(beans, inputChannel, outputChannel, null, null, expressions, comment);
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
		return createServiceActivator(beans, inputChannel, outputChannel, methodName, bean, null, null);
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
	 * @param comment
	 * @return
	 */
	public Beans createServiceActivator (Beans beans, String inputChannel, String outputChannel, String methodName,
	                                     Bean bean, String comment) {
		return createServiceActivator(beans, inputChannel, outputChannel, methodName, bean, null, comment);
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
		return createRouterInternal(beans, id, expression, inputChannel, mappings, null);
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
			String>> mappings, String comment) {
		return createRouterInternal(beans, id, expression, inputChannel, mappings, comment);
	}

	/**
	 * @param beans
	 * @param id
	 * @param expression
	 * @param inputChannel
	 * @param mappings     - парамерты для mapping, где
	 *                     - getT1() - value
	 *                     - getT2() -channel
	 * @param comment
	 * @return
	 */
	private Beans createRouterInternal (Beans beans, String id, String expression, String inputChannel,
	                                    List<Tuple2<String, String>> mappings, String comment) {
		beans.getImportOrAliasOrBean().add(createRouter(createRouterType(id, expression, inputChannel, createMappings
				(mappings), comment)));
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
	public MappingValueChannelType createMapping (String value, String channel) {
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
	 * @param comment
	 * @return
	 */
	private RouterType createRouterType (String id, String expression, String inputChannel,
	                                     List<MappingValueChannelType> mappings, String comment) {
		RouterType routerType = integrationFactory.createRouterType();
		routerType.setId(id);
		routerType.setStrExpression(expression);
		routerType.setInputChannel(inputChannel);
		routerType.getMapping().addAll(mappings);
		if (comment != null && !comment.isEmpty())
			routerType.setComment(comment);
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
	 * @param comment
	 * @return
	 */
	private Beans createServiceActivator (Beans beans, @NonNull String inputChannel, @NonNull String outputChannel,
	                                      String methodName, Bean bean, String expression, String comment) {
		for (Object importOrAliasOrBean : beans.getImportOrAliasOrBean()) {
			if (importOrAliasOrBean instanceof ServiceActivator) {
				ServiceActivator testingServiceActivator = (ServiceActivator) importOrAliasOrBean;
				if (testingServiceActivator.getInputChannel().equals(inputChannel)) return beans;
			}
		}

		beans.getImportOrAliasOrBean().add(createServiceActivator(inputChannel, outputChannel, methodName, bean,
		                                                          expression, comment));
		return beans;
	}

	/**
	 * @param inputChannel
	 * @param outputChannel
	 * @param methodName
	 * @param bean
	 * @param expression
	 * @param comment
	 * @return
	 */
	private ServiceActivator createServiceActivator (@NonNull String inputChannel, @NonNull String outputChannel,
	                                                 String methodName, Bean bean, String expression, String comment) {
		ServiceActivator serviceActivator = integrationFactory.createServiceActivator();
		serviceActivator.setInputChannel(inputChannel);
		serviceActivator.setOutputChannel(outputChannel);
		if (methodName != null && !methodName.isEmpty())
			serviceActivator.setMethod(methodName);
		if (bean != null)
			serviceActivator.getPollerOrExpressionOrRequestHandlerAdviceChain().add(bean);
		if (expression != null && !expression.isEmpty())
			serviceActivator.setStrExpression(expression);
		if (comment != null && !comment.isEmpty())
			serviceActivator.setComment(comment);
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
	 * @param comment
	 * @return
	 */
	private Beans createChannelInternal (Beans beans, String channelId, List<String> wireTapChannels, String comment) {
		beans.getImportOrAliasOrBean().add(createChannel(channelId, wireTapChannels, comment));
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
	 * @param comment
	 * @return
	 */
	private Channel createChannel (@NonNull String channelId, List<String> wireTapChannels, String comment) {
		Channel channel = integrationFactory.createChannel();
		channel.setId(channelId);
		if (wireTapChannels != null && !wireTapChannels.isEmpty())
			channel.setInterceptors(createInterceptorsType(wireTapChannels));
		if (comment != null && !comment.isEmpty())
			channel.setComment(comment);
		return channel;
	}

	public ObjectFactory getIntegrationFactory () {
		return integrationFactory;
	}
}
