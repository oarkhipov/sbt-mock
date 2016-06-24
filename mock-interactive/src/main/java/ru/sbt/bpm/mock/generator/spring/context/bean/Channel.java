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
@XmlRootElement(name = "channel", namespace = "http://www.springframework.org/schema/integration")
@XmlAccessorType(XmlAccessType.FIELD)
public class Channel implements ContextCommentable {

	@XmlTransient
	String comment;

	@XmlAttribute
	String id;

	@XmlElement
	Interceptors interceptors;

	@Override
	public String getComment () {
		return comment;
	}
}
