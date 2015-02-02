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
@XStreamAlias("MockConfig")
public class MockConfig {

    @XStreamAlias("systems")
    private SystemsTag aSystems;

    public SystemsTag getaSystems() {
        return aSystems;
    }

    public void setaSystems(SystemsTag aSystems) {
        this.aSystems = aSystems;
    }

    public List<SystemTag> getListOfSystems() {
        return aSystems.getaListOfSystems();
    }

    @Override
    public String toString() {
        return "MockConfig{" +
                "aSystems=" + aSystems +
                '}';
    }
}
