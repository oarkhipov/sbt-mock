package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-vostrikov-mi on 14.03.2015.
 */
@XStreamAlias("requestTagSequence")
public class RequestTagSequence {

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
        return "RequestTagSequence{" +
                "aListOfLinkedTags=" + aListOfLinkedTags +
                '}';
    }
}
