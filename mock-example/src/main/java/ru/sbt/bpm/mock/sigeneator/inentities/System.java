package ru.sbt.bpm.mock.sigeneator.inentities;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */

@XStreamAlias("system")
public class System {

    @XStreamAlias("name")
    private String aName;

    @XStreamAlias("pathToXSD")
    private String aPathToXSD;

    @XStreamAlias("rootXsd")
    private String aRootXSD;

    @XStreamAlias("integrationPoints")
    private List<IntegrationPoint> aListOfIntegrationPoints;

    public System(String aName, String aPathToXSD, String aRootXSD, List<IntegrationPoint> aListOfIntegrationPoints) {
        this.aName = aName;
        this.aPathToXSD = aPathToXSD;
        this.aRootXSD = aRootXSD;
        this.aListOfIntegrationPoints = aListOfIntegrationPoints;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
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

    public List<IntegrationPoint> getaListOfIntegrationPoints() {
        return aListOfIntegrationPoints;
    }

    public void setaListOfIntegrationPoints(List<IntegrationPoint> aListOfIntegrationPoints) {
        this.aListOfIntegrationPoints = aListOfIntegrationPoints;
    }

    @Override
    public String toString() {
        return "System{" +
                "aName='" + aName + '\'' +
                ", aPathToXSD='" + aPathToXSD + '\'' +
                ", aRootXSD='" + aRootXSD + '\'' +
                ", aListOfIntegrationPoints=" + aListOfIntegrationPoints +
                '}';
    }
}
