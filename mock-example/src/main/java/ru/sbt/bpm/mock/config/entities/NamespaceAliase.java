package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by sbt-vostrikov-mi on 03.04.2015.
 */
@XStreamAlias("tag")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = "url")
@ToString
public class NamespaceAliase {

    @XStreamAlias("name")
    @XStreamAsAttribute
    @Getter
    @Setter
    private String alias;

    @Getter
    @Setter
    private String url;
}
