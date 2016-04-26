package ru.sbt.bpm.mock.generator.spring.context.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-bochev-as on 12.01.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
@Data
@XmlRootElement(name = "beans", namespace = "http://www.springframework.org/schema/beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class BeanContainer {

    List<Import> imports = new ArrayList<Import>(){{
        add(new Import("contextConfigs/base-config.xml"));
        add(new Import("contextConfigs/logging-config.xml"));
    }};

    @XmlElement(name = "bean")
    List<Bean> beans;

    @XmlAttribute(namespace = "http://www.w3.org/2001/XMLSchema-instance")
    String schemaLocation =
            "http://www.springframework.org/schema/beans \t http://www.springframework.org/schema/beans/spring-beans.xsd\n" +
            "http://www.springframework.org/schema/integration \t http://www.springframework.org/schema/integration/spring-integration.xsd\n" +
            "http://www.springframework.org/schema/integration \t http://www.springframework.org/schema/integration/spring-integration.xsd\n" +
            "http://www.springframework.org/schema/integration/jms \t http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd";
}
