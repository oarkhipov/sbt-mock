package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sbt.bpm.mock.config.enums.Protocol;

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

    @XStreamAlias("remoteRootSchema")
    private String remoteRootSchema;

    @XStreamAlias("localRootSchema")
    private String localRootSchema;

    @XStreamAlias("integrationPointSelector")
    private XpathSelector integrationPointSelector;

    @XStreamAlias("protocol")
    private Protocol protocol;

    @XStreamAlias("queueConnectionFactory")
    private String queueConnectionFactory;

    @XStreamAlias("mockIncomeQueue")
    private String mockIncomeQueue;

    @XStreamAlias("mockOutcomeQueue")
    private String mockOutcomeQueue;

    @XStreamAlias("driverOutcomeQueue")
    private String driverOutcomeQueue;

    @XStreamAlias("driverIncomeQueue")
    private String driverIncomeQueue;

    @XStreamAlias("driverWebServiceEndpoint")
    private String driverWebServiceEndpoint;

    @XStreamAlias("integrationPoints")
    private IntegrationPoints integrationPoints;

    //for soap
//    @XStreamOmitField
//    private WsdlProject wsdlProject;

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
