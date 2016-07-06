package ru.sbt.bpm.mock.generator.spring.context.bean;

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
@XmlRootElement(name = "inbound-gateway", namespace = "http://www.springframework.org/schema/integration/jms")
@XmlAccessorType(XmlAccessType.FIELD)
public class InboundGateway implements ContextCommentable {

	@XmlTransient
	String contextComment;

	@XmlAttribute
	private String id;

	@XmlAttribute(name = "request-destination")
	private String requestDestination;

	@XmlAttribute(name = "request-channel")
	private String requestChannel;

	@XmlAttribute(name = "reply-channel")
	private String replyChannel;

	@XmlAttribute(name = "default-reply-destination")
	private String defaultReplyDestination;

	@XmlAttribute(name = "connection-factory")
	private String cf;

	@Override
	public String getComment () {
		return contextComment;
	}
}
