/**
 * @author sbt-bochev-as on 13.01.2016.
 * <p/>
 * Company: SBT - Moscow
 */
@XmlSchema(namespace = "http://www.springframework.org/schema/beans",
        xmlns = {
                @XmlNs(namespaceURI = "http://www.springframework.org/schema/beans", prefix = ""),
                @XmlNs(namespaceURI = "http://www.w3.org/2001/XMLSchema-instance", prefix = "xsi"),
                @XmlNs(namespaceURI = "http://www.springframework.org/schema/integration/jms", prefix = "int-jms"),
                @XmlNs(namespaceURI = "http://www.springframework.org/schema/integration", prefix = "int")
        },
        elementFormDefault = XmlNsForm.QUALIFIED)
package ru.sbt.bpm.mock.generator.spring.context.bean;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;


