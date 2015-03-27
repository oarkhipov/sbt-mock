package ru.sbt.bpm.mock.sigeneator.inentities;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by sbt-vostrikov-mi on 14.03.2015.
 */
@XStreamAlias("mapFromRequest")
public class MappedByXpath {

    @XStreamAlias("querry")
    private String aQuerry;

    @XStreamAlias("responseTagSequence")
    private ResponseTagSequence aResponseTagSequence;

    public void setaQuerry(String aQuerry) {
        this.aQuerry = aQuerry;
    }


    public String getaQuerry() {
        return aQuerry;
    }

    public ResponseTagSequence getaResponseTagSequence() {
        if (aResponseTagSequence!=null) {
            return aResponseTagSequence;
        }
        return null;
    }

    public void setaResponseTagSequence(ResponseTagSequence aResponseTagSequence) {
        this.aResponseTagSequence = aResponseTagSequence;
    }

    @Override
    public String toString() {
        return "LinkedTag{" +
                "aQuerry='" + aQuerry + '\'' +
                ", aResponseTagSequence='" + aResponseTagSequence + '\'' +
                '}';
    }
}
