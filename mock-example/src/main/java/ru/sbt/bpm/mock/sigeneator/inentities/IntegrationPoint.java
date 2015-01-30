package ru.sbt.bpm.mock.sigeneator.inentities;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
@XStreamAlias("integrationPoint")
public class IntegrationPoint {

    @XStreamAlias("name")
    private String aName;

    @XStreamAlias("type")
    private String aType;

    @XStreamAlias("linkedTagSequence")
    private List<LinkedTag> aListOfLinkedTags;

    @XStreamAlias("protocol")
    private String aProtocol;

    @XStreamAlias("incomeQueue")
    private String aIncomeQueue;

    @XStreamAlias("outcomeQueue")
    private String aOutcomeQueue;

    @XStreamAlias("xsdFile")
    private String aXsdFile;

    @XStreamAlias("dependencies")
    private List<Dependency> aListOfDependencies;

    public IntegrationPoint(String aName, String aType, List<LinkedTag> aListOfLinkedTags, String aProtocol, String aXsdFile, List<Dependency> aListOfDependencies) {
        this.aName = aName;
        this.aType = aType;
        this.aListOfLinkedTags = aListOfLinkedTags;
        this.aProtocol = aProtocol;
        this.aXsdFile = aXsdFile;
        this.aListOfDependencies = aListOfDependencies;
    }

    public IntegrationPoint(String aName, String aType, List<LinkedTag> aListOfLinkedTags, String aProtocol, String aIncomeQueue, String aOutcomeQueue, String aXsdFile, List<Dependency> aListOfDependencies) {
        this.aName = aName;
        this.aType = aType;
        this.aListOfLinkedTags = aListOfLinkedTags;
        this.aProtocol = aProtocol;
        this.aIncomeQueue = aIncomeQueue;
        this.aOutcomeQueue = aOutcomeQueue;
        this.aXsdFile = aXsdFile;
        this.aListOfDependencies = aListOfDependencies;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType;
    }

    public List<LinkedTag> getaListOfLinkedTags() {
        return aListOfLinkedTags;
    }

    public void setaListOfLinkedTags(List<LinkedTag> aListOfLinkedTags) {
        this.aListOfLinkedTags = aListOfLinkedTags;
    }

    public String getaProtocol() {
        return aProtocol;
    }

    public void setaProtocol(String aProtocol) {
        this.aProtocol = aProtocol;
    }

    public String getaIncomeQueue() {
        return aIncomeQueue;
    }

    public void setaIncomeQueue(String aIncomeQueue) {
        this.aIncomeQueue = aIncomeQueue;
    }

    public String getaOutcomeQueue() {
        return aOutcomeQueue;
    }

    public void setaOutcomeQueue(String aOutcomeQueue) {
        this.aOutcomeQueue = aOutcomeQueue;
    }

    public String getaXsdFile() {
        return aXsdFile;
    }

    public void setaXsdFile(String aXsdFile) {
        this.aXsdFile = aXsdFile;
    }

    public List<Dependency> getaListOfDependencies() {
        return aListOfDependencies;
    }

    public void setaListOfDependencies(List<Dependency> aListOfDependencies) {
        this.aListOfDependencies = aListOfDependencies;
    }

    @Override
    public String toString() {
        return "IntegrationPoint{" +
                "aName='" + aName + '\'' +
                ", aType='" + aType + '\'' +
                ", aListOfLinkedTags=" + aListOfLinkedTags +
                ", aProtocol='" + aProtocol + '\'' +
                ", aIncomeQueue='" + aIncomeQueue + '\'' +
                ", aOutcomeQueue='" + aOutcomeQueue + '\'' +
                ", aXsdFile='" + aXsdFile + '\'' +
                ", aListOfDependencies=" + aListOfDependencies +
                '}';
    }
}
