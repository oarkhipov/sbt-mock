package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by sbt-vostrikov-mi on 14.03.2015.
 */
@XStreamAlias("mappedTag")
@ToString
public class MappedTag {

    @XStreamAlias("requestMapping")
    @Getter
    @Setter
    private LinkedTag mappedFromRqTags;

    @XStreamAlias("requestQuerryValue")
    @Getter
    @Setter
    private String requestQuerryValue;

    @XStreamAlias("responceMapping")
    @Getter
    @Setter
    private LinkedTag responceMapping;
}
