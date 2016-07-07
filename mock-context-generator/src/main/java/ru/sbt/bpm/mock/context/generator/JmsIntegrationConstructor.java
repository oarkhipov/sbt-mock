package ru.sbt.bpm.mock.context.generator;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */

public class JmsIntegrationConstructor implements IContextGeneratable {

	/**
	 * <jms:inbound-gateway id="" request-destination="" request-channel="" reply-channel="" default-reply-destination="" connection-factory=""/>
	 * @param beans
	 * @param id
	 * @param requestDestination
	 * @param requestChannel
	 * @param replyChannel
	 * @param defaultReplyDestination
	 * @param connectionFactory
	 * @return
	 */
	public generated.springframework.beans.Beans createInboundGateway(generated.springframework.beans.Beans beans, String id, String requestDestination, String requestChannel, String replyChannel, String defaultReplyDestination, String connectionFactory) {
		return createInboundGatewayInternal(beans, id, requestDestination, requestChannel, replyChannel, defaultReplyDestination, connectionFactory);
	}

	/**
	 * <jms:inbound-gateway id="" request-destination="" request-channel="" reply-channel="" default-reply-destination="" connection-factory=""/>
	 * @param beans
	 * @param id
	 * @param requestDestination
	 * @param requestChannel
	 * @param replyChannel
	 * @param defaultReplyDestination
	 * @param connectionFactory
	 * @return
	 */
	private generated.springframework.beans.Beans createInboundGatewayInternal(generated.springframework.beans.Beans beans, String id, String requestDestination, String requestChannel, String replyChannel, String defaultReplyDestination, String connectionFactory) {
		beans.getImportOrAliasOrBean().add(createInboundGateway(id, requestDestination, requestChannel, replyChannel, defaultReplyDestination, connectionFactory));
		return beans;
	}

	/**
	 * <jms:inbound-gateway id="" request-destination="" request-channel="" reply-channel="" default-reply-destination="" connection-factory=""/>
	 * @param id
	 * @param requestDestination
	 * @param requestChannel
	 * @param replyChannel
	 * @param defaultReplyDestination
	 * @param connectionFactory
	 * @return
	 */
	private generated.springframework.integration.jms.InboundGateway createInboundGateway(String id, String requestDestination, String requestChannel, String replyChannel, String defaultReplyDestination, String connectionFactory) {
		generated.springframework.integration.jms.InboundGateway inboundGateway = jmsFactory.createInboundGateway();
		inboundGateway.setId(id);
		inboundGateway.setRequestDestination(requestDestination);
		inboundGateway.setRequestChannel(requestChannel);
		inboundGateway.setReplyChannel(replyChannel);
		inboundGateway.setDefaultReplyDestination(defaultReplyDestination);
		inboundGateway.setConnectionFactory(connectionFactory);
		return inboundGateway;
	}

	public generated.springframework.integration.jms.ObjectFactory getIntegrationFactory() {
		return jmsFactory;
	}

}
