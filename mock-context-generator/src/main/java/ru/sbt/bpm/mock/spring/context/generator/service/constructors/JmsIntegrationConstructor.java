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

import generated.springframework.beans.Beans;
import generated.springframework.integration.jms.InboundGateway;
import generated.springframework.integration.jms.ObjectFactory;
import generated.springframework.integration.jms.OutboundGateway;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.spring.context.generator.IContextGeneratable;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */

@Service
public class JmsIntegrationConstructor implements IContextGeneratable {

	/**
	 * <jms:inbound-gateway id="" request-destination="" request-channel="" reply-channel=""
	 * default-reply-destination="" connection-factory=""/>
	 *
	 * @param beans - <beans/>
	 * @param id - id inbound-gateway
	 * @param requestDestination -
	 * @param requestChannel -
	 * @param replyChannel
	 * @param defaultReplyDestination
	 * @param connectionFactory
	 * @return
	 */
	public Beans createInboundGateway (Beans beans, String id, String requestDestination, String requestChannel,
	                                   String replyChannel, String defaultReplyDestination, String connectionFactory) {
		return createInboundGatewayInternal(beans, id, requestDestination, requestChannel, replyChannel,
		                                    defaultReplyDestination, connectionFactory, null);
	}
	/**
	 * <jms:inbound-gateway id="" request-destination="" request-channel="" reply-channel=""
	 * default-reply-destination="" connection-factory=""/>
	 *
	 * @param beans - <beans/>
	 * @param id - id inbound-gateway
	 * @param requestDestination -
	 * @param requestChannel -
	 * @param replyChannel
	 * @param defaultReplyDestination
	 * @param connectionFactory
	 * @param comment
	 * @return
	 */
	public Beans createInboundGateway (Beans beans, String id, String requestDestination, String requestChannel,
	                                   String replyChannel, String defaultReplyDestination, String connectionFactory, String comment) {
		return createInboundGatewayInternal(beans, id, requestDestination, requestChannel, replyChannel,
		                                    defaultReplyDestination, connectionFactory, comment);
	}

	/**
	 * <jms:outbound-gateway id="" request-destination="" reply-destination="" request-channel="" reply-channel=""
	 * header-mapper="" connection-factory="" reply-timeout="" receive-timeout=""/>
	 *
	 * @param beans
	 * @param id
	 * @param requestDestination
	 * @param replyDestination
	 * @param requestChannel
	 * @param replyChannel
	 * @param headerMapper
	 * @param connectionFactory
	 * @param replyTimeout
	 * @param receiveTimeout
	 * @return
	 */
	public Beans createOutboundGateway (Beans beans, String id, String requestDestination, String replyDestination,
	                                    String requestChannel, String replyChannel, String headerMapper, String
			                                    connectionFactory, String replyTimeout, String receiveTimeout) {
		return createOutboundGatewayInternal(beans, id, requestDestination, replyDestination, requestChannel,
		                                     replyChannel, headerMapper, connectionFactory, replyTimeout,
		                                     receiveTimeout);
	}

	/**
	 * <jms:outbound-gateway id="" request-destination="" reply-destination="" request-channel="" reply-channel=""
	 * header-mapper="" connection-factory="" reply-timeout="" receive-timeout=""/>
	 *
	 * @param beans
	 * @param id
	 * @param requestDestination
	 * @param replyDestination
	 * @param requestChannel
	 * @param replyChannel
	 * @param headerMapper
	 * @param connectionFactory
	 * @param replyTimeout
	 * @param receiveTimeout
	 * @return
	 */
	private Beans createOutboundGatewayInternal (Beans beans, String id, String requestDestination, String
			replyDestination, String requestChannel, String replyChannel, String headerMapper, String
			                                             connectionFactory, String replyTimeout, String
			receiveTimeout) {
		beans.getImportOrAliasOrBean().add(createOutboundGateway(id, requestDestination, replyDestination,
		                                                         requestChannel, replyChannel, headerMapper,
		                                                         connectionFactory, replyTimeout, receiveTimeout));
		return beans;
	}

	/**
	 * <jms:outbound-gateway id="" request-destination="" reply-destination="" request-channel="" reply-channel=""
	 * header-mapper="" connection-factory="" reply-timeout="" receive-timeout=""/>
	 *
	 * @param id
	 * @param requestDestination
	 * @param replyDestination
	 * @param requestChannel
	 * @param replyChannel
	 * @param headerMapper
	 * @param connectionFactory
	 * @param replyTimeout
	 * @param receiveTimeout
	 * @return
	 */
	private OutboundGateway createOutboundGateway (String id, String requestDestination,
	                            String replyDestination, String requestChannel, String replyChannel,
	                            String headerMapper, String connectionFactory, String replyTimeout,
	                            String receiveTimeout) {
		generated.springframework.integration.jms.OutboundGateway outboundGateway = jmsFactory.createOutboundGateway();
		outboundGateway.setId(id);
		outboundGateway.setRequestDestination(requestDestination);
		outboundGateway.setReplyDestination(replyDestination);
		outboundGateway.setRequestChannel(requestChannel);
		outboundGateway.setReplyChannel(replyChannel);
		outboundGateway.setHeaderMapper(headerMapper);
		outboundGateway.setConnectionFactory(connectionFactory);
		outboundGateway.setReplyTimeout(replyTimeout);
		outboundGateway.setReceiveTimeout(receiveTimeout);
		return outboundGateway;
	}

	/**
	 * <jms:inbound-gateway id="" request-destination="" request-channel="" reply-channel=""
	 * default-reply-destination="" connection-factory=""/>
	 *
	 * @param beans
	 * @param id
	 * @param requestDestination
	 * @param requestChannel
	 * @param replyChannel
	 * @param defaultReplyDestination
	 * @param connectionFactory
	 * @param comment
	 * @return
	 */
	private Beans createInboundGatewayInternal (Beans beans, String id, String requestDestination,
	                                            String requestChannel, String replyChannel,
	                                            String defaultReplyDestination, String connectionFactory, String comment) {
		beans.getImportOrAliasOrBean().add(createInboundGateway(id, requestDestination, requestChannel, replyChannel,
		                                                        defaultReplyDestination, connectionFactory, comment));
		return beans;
	}

	/**
	 * <jms:inbound-gateway id="" request-destination="" request-channel="" reply-channel="" default-reply-destination
	 * ="" connection-factory=""/>
	 *
	 * @param id
	 * @param requestDestination
	 * @param requestChannel
	 * @param replyChannel
	 * @param defaultReplyDestination
	 * @param connectionFactory
	 * @param comment
	 * @return
	 */
	private InboundGateway createInboundGateway (String id, String requestDestination, String requestChannel,
	                                             String replyChannel, String defaultReplyDestination,
	                                             String connectionFactory, String comment) {
		generated.springframework.integration.jms.InboundGateway inboundGateway = jmsFactory.createInboundGateway();
		inboundGateway.setId(id);
		inboundGateway.setRequestDestination(requestDestination);
		inboundGateway.setRequestChannel(requestChannel);
		inboundGateway.setReplyChannel(replyChannel);
		inboundGateway.setDefaultReplyDestination(defaultReplyDestination);
		inboundGateway.setConnectionFactory(connectionFactory);
		if (comment != null && !comment.isEmpty())
			inboundGateway.setComment(comment);
		return inboundGateway;
	}

	public ObjectFactory getJmsIntegrationFactory () {
		return jmsFactory;
	}

}
