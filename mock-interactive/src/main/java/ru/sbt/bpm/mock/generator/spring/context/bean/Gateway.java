package ru.sbt.bpm.mock.generator.spring.context.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sbt.bpm.mock.generator.spring.context.ContextCommentable;

import javax.xml.bind.annotation.*;

/**
 * Created by sbt-hodakovskiy-da on 24.06.2016.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "gateway", namespace = "http://www.springframework.org/schema/integration")
@XmlAccessorType(XmlAccessType.FIELD)
public class Gateway implements ContextCommentable {

	@XmlTransient
	String comment;

	@XmlAttribute
	String id;

	@XmlAttribute(name = "error-channel")
	String errorChannel;

	@XmlAttribute(name = "service-interface")
	String serviceInterface;

	@XmlAttribute(name = "default-reply-timeout")
	String defaultReplyTimeout;

	@XmlAttribute(name = "default-request-timeout")
	String defaultRequestTimeout;

	@XmlElement(name = "method")
	Method method;

	@Override
	public String getComment () {
		return null;
	}
}
