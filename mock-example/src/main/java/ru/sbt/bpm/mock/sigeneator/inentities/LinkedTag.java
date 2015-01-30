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
    private String aNamespace;

    @XStreamAlias("tag")
    private String aTag;

    public LinkedTag(String aNamespace, String aTag) {
        this.aNamespace = aNamespace;
        this.aTag = aTag;
    }

    public String getaNamespace() {
        return aNamespace;
    }

    public void setaNamespace(String aNamespace) {
        this.aNamespace = aNamespace;
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
                "aNamespace='" + aNamespace + '\'' +
                ", aTag='" + aTag + '\'' +
                '}';
    }
}
