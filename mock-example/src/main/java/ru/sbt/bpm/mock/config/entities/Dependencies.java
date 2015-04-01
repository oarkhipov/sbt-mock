package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */

@XStreamAlias("dependencies")
public class Dependencies {

    @XStreamImplicit(itemFieldName = "dependency")
    private List<Dependency> aDependency = new ArrayList<Dependency>();

    public List<Dependency> getaDependency() {
        return aDependency;
    }

    public void setaDependency(List<Dependency> aDependency) {
        this.aDependency = aDependency;
    }

    @Override
    public String toString() {
        return "Dependencies{" +
                "aDependency=" + aDependency +
                '}';
    }
}
