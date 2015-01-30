package ru.sbt.bpm.mock.sigeneator.inentities;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* Created by sbt-hodakovskiy-da on 30.01.2015.
* <p/>
* Company: SBT - Saint-Petersburg
*/
@XStreamAlias("linkedTag")
public class LinkedTag {

    @XStreamAlias("namespace")
    private String aNameSpace;

    @XStreamAlias("tag")
    private String aTag;

    public String getaNameSpace() {
        return aNameSpace;
    }

    public void setaNameSpace(String aNameSpace) {
        this.aNameSpace = aNameSpace;
    }

    public String getaTag() {
        return aTag;
    }

    public void setaTag(String aTag) {
        this.aTag = aTag;
    }

    @Override
    public String toString() {
        return "LinkedTag{" +
                "aNameSpace='" + aNameSpace + '\'' +
                ", aTag='" + aTag + '\'' +
                '}';
    }
}
