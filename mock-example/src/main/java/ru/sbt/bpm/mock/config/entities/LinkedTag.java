package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* Created by sbt-hodakovskiy-da on 30.01.2015.
* <p/>
* Company: SBT - Saint-Petersburg
*/
@XStreamAlias("tag")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = "tag")
@ToString
public class LinkedTag {

    @XStreamAlias("ns")
    @XStreamAsAttribute
    @Getter
    @Setter
    private String nameSpace;

    @Getter
    @Setter
    private String tag;
}
