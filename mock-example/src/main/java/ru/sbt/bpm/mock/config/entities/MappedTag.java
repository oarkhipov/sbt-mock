package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by sbt-vostrikov-mi on 14.03.2015.
 */
@XStreamAlias("mappedTag")
public class MappedTag {

    @XStreamAlias("mapFromRequest")
    private MappedFromRqTag aMappedFromRqTags;

    @XStreamAlias("xpathQuerry")
    private MappedByXpath aXpathQuerries;

    public MappedFromRqTag getaMappedFromRqTags() {
        return aMappedFromRqTags;
    }

    public void setaXpathQuerrys(MappedByXpath aXpathQuerries) {
        this.aXpathQuerries = aXpathQuerries;
    }

    public MappedByXpath getaXpathQuerrys() {
        return aXpathQuerries;
    }

    public void setaMappedFromRqTags(MappedFromRqTag aMappedFromRqTags) {
        this.aMappedFromRqTags = aMappedFromRqTags;
    }

    @Override
    public String toString() {
        return "MappedTag{" +
                "aMappedFromRqTags='" + aMappedFromRqTags + '\'' +
                ", aXpathQuerrys='" + aXpathQuerries + '\'' +
                '}';
    }
}
