package ru.sbt.bpm.mock.generator.spring.context.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sbt-bochev-as on 12.01.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class Property {
    @XmlAttribute(name = "name")
    String name;
    @XmlAttribute(name = "value")
    String value;
}
