package ru.sbt.bpm.mock.sigeneator.inentities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-vostrikov-mi on 14.03.2015.
 */
@XStreamAlias("mappedTags")
public class MappedTagSequence {

    @XStreamImplicit(itemFieldName = "mappedTag")
    private List<MappedTag> aListOfMappedTagTags = new ArrayList<MappedTag>();

    public List<MappedTag> getaListOfMappedTagTags() {
        return aListOfMappedTagTags;
    }

    public void setaListOfMappedTagTags(List<MappedTag> aListOfMappedTagTags) {
        this.aListOfMappedTagTags = aListOfMappedTagTags;
    }

    @Override
    public String toString() {
        return "MappedTagTagSequence{" +
                "aListOfMappedTagTags=" + aListOfMappedTagTags +
                '}';
    }
}
