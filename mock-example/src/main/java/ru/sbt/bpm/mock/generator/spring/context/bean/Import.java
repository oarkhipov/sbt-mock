package ru.sbt.bpm.mock.generator.spring.context.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sbt-bochev-as on 13.01.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "import", namespace = "http://www.springframework.org/schema/beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class Import {
    @XmlAttribute
    String resource;
}
