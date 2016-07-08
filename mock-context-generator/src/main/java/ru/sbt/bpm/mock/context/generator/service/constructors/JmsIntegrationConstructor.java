package ru.sbt.bpm.mock.context.generator.service.constructors;

import generated.springframework.beans.Beans;
import generated.springframework.integration.jms.InboundGateway;
import generated.springframework.integration.jms.ObjectFactory;
import generated.springframework.integration.jms.OutboundGateway;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.context.generator.IContextGeneratable;

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
	 * @param connectionFactory* @return
	 */
	public Beans createInboundGateway (Beans beans, String id, String requestDestination, String requestChannel,
	                                   String replyChannel, String defaultReplyDestination, String connectionFactory) {
		return createInboundGatewayInternal(beans, id, requestDestination, requestChannel, replyChannel,
		                                    defaultReplyDestination, connectionFactory);
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
	 * @return
	 */
	private Beans createInboundGatewayInternal (Beans beans, String id, String requestDestination,
	                                            String requestChannel, String replyChannel,
	                                            String defaultReplyDestination, String connectionFactory) {
		beans.getImportOrAliasOrBean().add(createInboundGateway(id, requestDestination, requestChannel, replyChannel,
		                                                        defaultReplyDestination, connectionFactory));
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
	 * @return
	 */
	private InboundGateway createInboundGateway (String id, String requestDestination, String requestChannel,
	                                             String replyChannel, String defaultReplyDestination,
	                                             String connectionFactory) {
		generated.springframework.integration.jms.InboundGateway inboundGateway = jmsFactory.createInboundGateway();
		inboundGateway.setId(id);
		inboundGateway.setRequestDestination(requestDestination);
		inboundGateway.setRequestChannel(requestChannel);
		inboundGateway.setReplyChannel(replyChannel);
		inboundGateway.setDefaultReplyDestination(defaultReplyDestination);
		inboundGateway.setConnectionFactory(connectionFactory);
		return inboundGateway;
	}

	public ObjectFactory getJmsIntegrationFactory () {
		return jmsFactory;
	}

}
