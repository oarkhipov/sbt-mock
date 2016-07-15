package ru.sbt.bpm.mock.spring.context.generator;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by sbt-hodakovskiy-da on 15.07.2016.
 */

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractCommentable {

	@XmlTransient
	private String comment;
}
