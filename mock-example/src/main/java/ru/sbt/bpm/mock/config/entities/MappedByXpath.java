package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by sbt-vostrikov-mi on 14.03.2015.
 */
@XStreamAlias("mapFromRequest")
@ToString
public class MappedByXpath {

    @XStreamAlias("querry")
    @Getter
    @Setter
    private String querry;

    @XStreamAlias("responseTagSequence")
    @Getter
    @Setter
    private ResponseTagSequence responseTagSequence;
}
