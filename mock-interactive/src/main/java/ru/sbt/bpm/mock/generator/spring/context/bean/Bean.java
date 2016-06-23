package ru.sbt.bpm.mock.generator.spring.context.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sbt.bpm.mock.generator.spring.context.ContextCommentable;

import javax.xml.bind.annotation.*;

/**
 * @author sbt-bochev-as on 12.01.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "bean", namespace = "http://www.springframework.org/schema/beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class Bean implements ContextCommentable {

    @XmlTransient
    String contextComment;

    @XmlAttribute
    String id;
    @XmlAttribute(name = "class")
    String className;

    @XmlElement(name = "constructor-arg")
    ConstructorArg constructor;

    Property property;

    @Override
    public String getComment () {
        return contextComment;
    }
}
