package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
@XStreamAlias("linkedTagSequence")
@ToString
public class LinkedTagSequence {

    @XStreamImplicit(itemFieldName = "linkedTag")
    @Getter
    @Setter
    private List<LinkedTag> listOfLinkedTags = new ArrayList<LinkedTag>();
}
