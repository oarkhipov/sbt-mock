package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */

@XStreamAlias("system")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class System {

    @XStreamAlias("name")
    @XStreamAsAttribute
    private String systemName;

    @XStreamAlias("rootXsd")
    private String rootXSD;

    @XStreamAlias("integrationPointSelector")
    private XpathSelector integrationPointSelector;

    @XStreamAlias("selectorType")
    private XpathType selectorType;

    @XStreamAlias("protocol")
    private String protocol;

    @XStreamAlias("queueConnectionFactory")
    String queueConnectionFactory;

    @XStreamAlias("mockIncomeQueue")
    String mockIncomeQueue;

    @XStreamAlias("mockOutcomeQueue")
    String mockOutcomeQueue;

    @XStreamAlias("driverOutcomeQueue")
    String driverOutcomeQueue;

    @XStreamAlias("driverIncomeQueue")
    String driverIncomeQueue;

    @XStreamAlias("integrationPoints")
    private IntegrationPoints integrationPoints;

    public List<String> getIntegrationPointNames() {
        List<String> result = new ArrayList<String>();
        for (IntegrationPoint point : getIntegrationPoints().getIntegrationPoints()) {
            result.add(point.getName());
        }
        return result;
    }


    public List<IntegrationPoint> getMockIntegrationPoints() {
        List<IntegrationPoint> mockIntegrationPoints = new ArrayList<IntegrationPoint>();
        for (IntegrationPoint integrationPoint : integrationPoints.getIntegrationPoints()) {
            if (integrationPoint.isMock()) {
                mockIntegrationPoints.add(integrationPoint);
            }
        }
        return mockIntegrationPoints;
    }

    public List<IntegrationPoint> getDriverIntegrationPoints() {
        List<IntegrationPoint> driverIntegrationPoints = new ArrayList<IntegrationPoint>();
        for (IntegrationPoint integrationPoint : integrationPoints.getIntegrationPoints()) {
            if (integrationPoint.isDriver()) {
                driverIntegrationPoints.add(integrationPoint);
            }
        }
        return driverIntegrationPoints;
    }
}
