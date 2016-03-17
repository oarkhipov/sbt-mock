package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sbt-bochev-as
 * on 25.11.2015.
 */
@XStreamAlias("tag")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = "element")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElementSelector {

    @XStreamAlias("ns")
    @XStreamAsAttribute
    private String namespace;

    private String element;

    public ElementSelector(String namespace) {
        this.namespace = namespace;
    }
}
