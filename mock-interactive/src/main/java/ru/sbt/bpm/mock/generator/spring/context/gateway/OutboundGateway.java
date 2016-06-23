package ru.sbt.bpm.mock.generator.spring.context.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sbt.bpm.mock.generator.spring.context.ContextCommentable;

import javax.xml.bind.annotation.*;

/**
 * Created by sbt-hodakovskiy-da on 23.06.2016.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "outbound-gateway", namespace = "http://www.springframework.org/schema/integration/jms")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutboundGateway implements ContextCommentable {

	@XmlTransient
	String comment;

	@XmlAttribute
	String id;

	@XmlAttribute(name = "request-destination")
	String requestDestination;

	@XmlAttribute(name = "reply-destination")
	String replyDestination;

	@XmlAttribute(name = "request-channel")
	String requestChannel;

	@XmlAttribute(name = "reply-channel")
	String replyChannel;

	@XmlAttribute(name = "header-mapper")
	String headerMapper;

	@XmlAttribute(name = "connection-factory")
	String connectionFactory;

	@XmlAttribute(name = "reply-timeout")
	String replyTimeout;

	@XmlAttribute(name = "receive-timeout")
	String receiveTimeout;

	@Override
	public String getComment () {
		return comment;
	}
}
