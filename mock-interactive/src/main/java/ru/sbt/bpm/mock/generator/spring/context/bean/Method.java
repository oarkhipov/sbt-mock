package ru.sbt.bpm.mock.generator.spring.context.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sbt-hodakovskiy-da on 24.06.2016.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "method", namespace = "http://www.springframework.org/schema/integration")
@XmlAccessorType(XmlAccessType.FIELD)
public class Method {

	@XmlAttribute
	String name;

	@XmlAttribute(name = "request-channel")
	String requestChannel;

	@XmlAttribute(name = "reply-channel")
	String replyChannel;
}
