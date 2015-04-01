package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
* Created by sbt-hodakovskiy-da on 30.01.2015.
* <p/>
* Company: SBT - Saint-Petersburg
*/

@XStreamAlias("system")
public class SystemTag {

    @XStreamAlias("name")
    private String aSystemName;

    @XStreamAlias("pathToXSD")
    private String aPathToXSD;

    @XStreamAlias("rootXsd")
    private String aRootXSD;

    @XStreamAlias("headerNamespace")
    private String aHeaderNamespace;

    @XStreamAlias("integrationPoints")
    private IntegrationPoints aIntegrationPoints;

    public String getaSystemName() {
        return aSystemName;
    }

    public void setaSystemName(String aSystemName) {
        this.aSystemName = aSystemName;
    }

    public String getaPathToXSD() {
        return aPathToXSD;
    }

    public void setaPathToXSD(String aPathToXSD) {
        this.aPathToXSD = aPathToXSD;
    }

    public String getaRootXSD() {
        return aRootXSD;
    }

    public void setaRootXSD(String aRootXSD) {
        this.aRootXSD = aRootXSD;
    }

    public String getaHeaderNamespace() {
        return aHeaderNamespace;
    }

    public void setaHeaderNamespace(String aHeaderNamespace) {
        this.aHeaderNamespace = aHeaderNamespace;
    }

    public IntegrationPoints getaIntegrationPoints() {
        return aIntegrationPoints;
    }

    public void setaIntegrationPoints(IntegrationPoints aIntegrationPoints) {
        this.aIntegrationPoints = aIntegrationPoints;
    }

    public List<IntegrationPoint> getListOfIntegrationPoints() {
        return aIntegrationPoints.getaListOfIntegrationPoints();
    }

    @Override
    public String toString() {
        return "SystemTag{" +
                "aSystemName='" + aSystemName + '\'' +
                ", aPathToXSD='" + aPathToXSD + '\'' +
                ", aRootXSD='" + aRootXSD + '\'' +
                ", aIntegrationPoints=" + aIntegrationPoints +
                '}';
    }
}
