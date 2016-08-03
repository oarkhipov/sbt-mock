package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */

@XStreamAlias("integrationPoints")
@Data
public class IntegrationPoints {

    @XStreamImplicit(itemFieldName = "integrationPoint")
    private Set<IntegrationPoint> integrationPoints;

    public IntegrationPoints() {
        integrationPoints = initSet();
    }

    public static Set<IntegrationPoint> initSet() {
        return new TreeSet<IntegrationPoint>(new IntegrationPointComparator());
    }

    public IntegrationPoint getIntegrationPointByName(String name) {
        for (IntegrationPoint integrationPoint : getIntegrationPoints()) {
            if (integrationPoint.getName().equals(name)) {
                return integrationPoint;
            }
        }

        throw new NoSuchElementException("No Integration point with name [" + name + "]");
    }

    public Set<IntegrationPoint> getIntegrationPoints() {
        if (integrationPoints == null) {
            integrationPoints = initSet();
        }
        return integrationPoints;
    }

    private static class IntegrationPointComparator implements Comparator<IntegrationPoint> {
        @Override
        public int compare(IntegrationPoint o1, IntegrationPoint o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public void fixTypes() {
        if (integrationPoints != null) {
            Set<IntegrationPoint> integrationPointSet = initSet();
            integrationPointSet.addAll(integrationPoints);
            this.integrationPoints = integrationPointSet;
        }
    }
}
