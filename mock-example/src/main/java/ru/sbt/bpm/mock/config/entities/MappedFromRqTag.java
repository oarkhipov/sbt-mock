package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by sbt-vostrikov-mi on 14.03.2015.
 */
@XStreamAlias("mapFromRequest")
public class MappedFromRqTag {

    @XStreamAlias("requestTagSequence")
    private RequestTagSequence aRequestTagSequence;

    @XStreamAlias("responseTagSequence")
    private ResponseTagSequence aResponseTagSequence;

    public RequestTagSequence getaRequestTagSequence() {
        if (aRequestTagSequence!=null) {
            return aRequestTagSequence;
        }
        return null;
    }

    public void setaRequestTagSequence(RequestTagSequence aRequestTagSequence) {
        this.aRequestTagSequence = aRequestTagSequence;
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
                "aRequestTagSequence='" + aRequestTagSequence + '\'' +
                ", aResponseTagSequence='" + aResponseTagSequence + '\'' +
                '}';
    }
}
