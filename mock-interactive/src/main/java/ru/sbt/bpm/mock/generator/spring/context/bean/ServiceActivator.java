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
@XmlRootElement(name = "service-activator", namespace = "http://www.springframework.org/schema/integration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceActivator implements ContextCommentable {

	@XmlTransient
	String comment;

	@XmlAttribute(name = "input-channel")
	String inputChannel;

	@XmlAttribute(name = "output-channel")
	String outputChannel;

	@XmlAttribute(name = "method")
	String methodValue;

	@XmlAttribute(name = "expression")
	String expressionValue;

	@XmlElement(name = "bean")
	Bean bean;

	@Override
	public String getComment () {
		return comment;
	}
}
