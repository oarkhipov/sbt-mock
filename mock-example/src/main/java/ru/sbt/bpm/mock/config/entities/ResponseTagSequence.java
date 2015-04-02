package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-vostrikov-mi on 14.03.2015.
 */
@XStreamAlias("responseTagSequence")
@ToString
public class ResponseTagSequence {

    @XStreamImplicit(itemFieldName = "linkedTag")
    @Getter
    @Setter
    private List<LinkedTag> listOfLinkedTags = new ArrayList<LinkedTag>();
}
