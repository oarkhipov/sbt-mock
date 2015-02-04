package ru.sbt.bpm.mock.sigeneator.inentities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.sbt.bpm.mock.sigeneator.Pair;

import java.util.List;

/**
* Created by sbt-hodakovskiy-da on 30.01.2015.
* <p/>
* Company: SBT - Saint-Petersburg
*/
@XStreamAlias("integrationPoint")
public class IntegrationPoint {

    @XStreamAlias("name")
    private String aIntegrationPointName;

    @XStreamAlias("rsRootElementName")
    private String aRsRootElementName;

    @XStreamAlias("rqRootElementName")
    private String aRqRootElementName;

    @XStreamAlias("type")
    private String aIntegrationPointType;

    @XStreamAlias("linkedTagSequence")
    private LinkedTagSequence aLinkedTagSequence;

    @XStreamAlias("protocol")
    private String aProtocol;

    @XStreamAlias("incomeQueue")
    private String aIncomeQueue;

    @XStreamAlias("outcomeQueue")
    private String aOutcomeQueue;

    // Так как маппинг идет по полям xml, для удобства доступа и сравнения создаем Pair<INCOME, OUTCOME>
    private Pair<String, String> aPairOfChannels;

    @XStreamAlias("xsdFile")
    private String aXsdFile;

    @XStreamAlias("dependencies")
    private Dependencies aDependencies;

    public String getaIntegrationPointName() {
        return aIntegrationPointName;
    }

    public void setaIntegrationPointName(String aIntegrationPointName) {
        this.aIntegrationPointName = aIntegrationPointName;
    }

    public String getaRsRootElementName() {
        return aRsRootElementName;
    }

    public void setaRsRootElementName(String aRsRootElementName) {
        this.aRsRootElementName = aRsRootElementName;
    }

    public String getaRqRootElementName() {
        return aRqRootElementName;
    }

    public void setaRqRootElementName(String aRqRootElementName) {
        this.aRqRootElementName = aRqRootElementName;
    }

    public String getaIntegrationPointType() {
        return aIntegrationPointType;
    }

    public void setaIntegrationPointType(String aIntegrationPointType) {
        this.aIntegrationPointType = aIntegrationPointType;
    }

    public LinkedTagSequence getaLinkedTagSequence() {
        return aLinkedTagSequence;
    }

    public void setaLinkedTagSequence(LinkedTagSequence aLinkedTagSequence) {
        this.aLinkedTagSequence = aLinkedTagSequence;
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

    public Dependencies getaDependencies() {
        return aDependencies;
    }

    public void setaDependencies(Dependencies aDependencies) {
        this.aDependencies = aDependencies;
    }

    public Pair<String, String> getaPairOfChannels() {
        return aPairOfChannels;
    }

    public void setaPairOfChannels(Pair<String, String> aPairOfChannels) {
        this.aPairOfChannels = aPairOfChannels;
    }

    public List<Dependency> getListOfDependencies() {
        return aDependencies.getaDependency();
    }

    public List<LinkedTag> getListOfLinkedTags() {
        return aLinkedTagSequence.getaListOfLinkedTags();
    }

    @Override
    public String toString() {
        return "IntegrationPoint{" +
                "aIntegrationPointName='" + aIntegrationPointName + '\'' +
                ", aRsRootElementName='" + aRsRootElementName + '\'' +
                ", aRqRootElementName='" + aRqRootElementName + '\'' +
                ", aIntegrationPointType='" + aIntegrationPointType + '\'' +
                ", aLinkedTagSequence=" + aLinkedTagSequence +
                ", aProtocol='" + aProtocol + '\'' +
                ", aIncomeQueue='" + aIncomeQueue + '\'' +
                ", aOutcomeQueue='" + aOutcomeQueue + '\'' +
                ", aPairOfChannels=" + aPairOfChannels +
                ", aXsdFile='" + aXsdFile + '\'' +
                ", aDependencies=" + aDependencies +
                '}';
    }
}
