package ru.sbt.bpm.mock.sigeneator.inentities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */

@XStreamAlias("integrationPoints")
public class IntegrationPoints {

    @XStreamImplicit(itemFieldName = "integrationPoint")
    private List aListOfIntegrationPoints = new ArrayList();

    public List getaListOfIntegrationPoints() {
        return aListOfIntegrationPoints;
    }

    public void setaListOfIntegrationPoints(List aListOfIntegrationPoints) {
        this.aListOfIntegrationPoints = aListOfIntegrationPoints;
    }

    @Override
    public String toString() {
        return "IntegrationPoints{" +
                "aListOfIntegrationPoints=" + aListOfIntegrationPoints +
                '}';
    }
}
