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
@XStreamAlias("mappedTagSequence")
@ToString
public class MappedTagSequence {

    @XStreamImplicit(itemFieldName = "mappedTag")
    @Getter
    @Setter
    private List<MappedTag> listOfMappedTagTags = new ArrayList<MappedTag>();
}
