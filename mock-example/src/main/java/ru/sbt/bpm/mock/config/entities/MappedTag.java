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

    @XStreamAlias("mapFromRequest")
    @Getter
    @Setter
    private MappedFromRqTag mappedFromRqTags;

    @XStreamAlias("xpathQuerry")
    @Getter
    @Setter
    private MappedByXpath xPathQuerries;
}
