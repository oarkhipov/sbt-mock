package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */

@XStreamAlias("integrationPoints")
@ToString
public class IntegrationPoints {

    @XStreamImplicit(itemFieldName = "integrationPoint")
    @Getter
    @Setter
    private List<IntegrationPoint> listOfIntegrationPoints = new ArrayList<IntegrationPoint>();

    public IntegrationPoint getIntegrationPointByName(String name) {
        for (IntegrationPoint integrationPoint : getListOfIntegrationPoints()) {
            if(integrationPoint.getIntegrationPointName().equals(name)) {
                return integrationPoint;
            }
        }

        throw new NoSuchElementException("No Integration point with name [" + name + "]");
    }
}
