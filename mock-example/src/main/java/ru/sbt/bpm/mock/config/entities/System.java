package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */

@XStreamAlias("system")
@Data
public class System {

    @XStreamAlias("name")
    @XStreamAsAttribute
    private String systemName;

    @XStreamAlias("rootXsd")
    private String rootXSD;

    @XStreamAlias("integrationPointSelector")
    private XpathSelector integrationPointSelector;

    @XStreamAlias("protocol")
    private String protocol;

    @XStreamAlias("integrationPoints")
    private IntegrationPoints integrationPoints;

    public List<String> getIntegrationPointNames() {
        List<String> result = new ArrayList<String>();
        for (IntegrationPoint point : getIntegrationPoints().getIntegrationPoints()) {
            result.add(point.getName());
        }
        return result;
    }

    private transient List<IntegrationPoint> mockIntegrationPoints;

    public List<IntegrationPoint> getMockIntegrationPoints() {
        if (mockIntegrationPoints == null) {
            mockIntegrationPoints = new ArrayList<IntegrationPoint>();
            for (IntegrationPoint integrationPoint : integrationPoints.getIntegrationPoints()) {
                if (integrationPoint.isMock()) {
                    mockIntegrationPoints.add(integrationPoint);
                }
            }
        }
        return mockIntegrationPoints;
    }

    private transient List<IntegrationPoint> driverIntegrationPoints;

    public List<IntegrationPoint> getDriverIntegrationPoints() {
        if (driverIntegrationPoints == null) {
            driverIntegrationPoints = new ArrayList<IntegrationPoint>();
            for (IntegrationPoint integrationPoint : integrationPoints.getIntegrationPoints()) {
                if (integrationPoint.isDriver()) {
                    driverIntegrationPoints.add(integrationPoint);
                }
            }
        }
        return driverIntegrationPoints;
    }
}
