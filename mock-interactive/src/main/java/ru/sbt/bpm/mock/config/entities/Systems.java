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
@XStreamAlias("systems")
@Data
public class Systems {

    @XStreamImplicit(itemFieldName = "system")
    private Set<System> systems;

    public Systems() {
        systems = initSet();
    }

    public static Set<System> initSet() {
        return new TreeSet<System>(new SystemComparator());
    }

    public System getSystemByName(String name) {
        for (System system : systems) {
            if (system.getSystemName().equals(name)) {
                return system;
            }
        }

        throw new NoSuchElementException("No system with name [" + name + "]");

    }

    public Set<System> getSystems() {
        if (systems == null) {
            systems = initSet();
        }
        return systems;
    }

    private static class SystemComparator implements Comparator<System> {
        @Override
        public int compare(System o1, System o2) {
            return (o1.getProtocol().name() + o1.getSystemName())
                    .compareTo(o2.getProtocol().name() + o2.getSystemName());
        }
    }

    public void fixTypes() {
        if (systems != null) {
            Set<System> systemSet = initSet();
            systemSet.addAll(systems);
            this.systems = systemSet;
        }
    }
}
