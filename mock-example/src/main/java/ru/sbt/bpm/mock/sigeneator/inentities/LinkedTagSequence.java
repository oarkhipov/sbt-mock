package ru.sbt.bpm.mock.sigeneator.inentities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
@XStreamAlias("linkedTagSequence")
public class LinkedTagSequence {

    @XStreamImplicit(itemFieldName = "linkedTag")
    private List<LinkedTag> aListOfLinkedTags = new ArrayList<LinkedTag>();

    public List<LinkedTag> getaListOfLinkedTags() {
        return aListOfLinkedTags;
    }

    public void setaListOfLinkedTags(List<LinkedTag> aListOfLinkedTags) {
        this.aListOfLinkedTags = aListOfLinkedTags;
    }

    @Override
    public String toString() {
        return "LinkedTagSequence{" +
                "aListOfLinkedTags=" + aListOfLinkedTags +
                '}';
    }
}
