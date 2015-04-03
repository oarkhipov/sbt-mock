package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
* Created by sbt-hodakovskiy-da on 30.01.2015.
* <p/>
* Company: SBT - Saint-Petersburg
*/

@XStreamAlias("system")
@ToString
public class SystemTag {

    @XStreamAlias("name")
    @XStreamAsAttribute
    @Getter
    @Setter
    private String systemName;

    @XStreamAlias("pathToXSD")
    @Getter
    @Setter
    private String pathToXSD;

    @XStreamAlias("rootXsd")
    @Getter
    @Setter
    private String rootXSD;

    @XStreamAlias("headerNamespace")
    @Getter
    @Setter
    private String headerNamespace;

    @XStreamAlias("integrationPoints")
    @Getter
    @Setter
    private IntegrationPoints integrationPoints;

    public List<IntegrationPoint> getListIntegrationPoint() {
        return integrationPoints.getListOfIntegrationPoints();
    }
}
