package ru.sbt.bpm.mock.sigeneator.inentities;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
@XStreamAlias("MockConfig")
public class MockConfig {

    @XStreamAlias("systems")
    private List<System> aListSystems;

    public MockConfig(List<System> aListSystems) {
        this.aListSystems = aListSystems;
    }

    public List<System> getaListSystems() {
        return aListSystems;
    }

    public void setaListSystems(List<System> aListSystems) {
        this.aListSystems = aListSystems;
    }

    @Override
    public String toString() {
        return "MockConfig{" +
                "aListSystems=" + aListSystems +
                '}';
    }
}
