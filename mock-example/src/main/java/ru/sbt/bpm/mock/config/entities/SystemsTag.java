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
@XStreamAlias("systems")
public class SystemsTag {

    @XStreamImplicit(itemFieldName = "system")
    private List<SystemTag> aListOfSystems = new ArrayList<SystemTag>();

    public List getaListOfSystems() {
        return aListOfSystems;
    }

    public void setaListOfSystems(List<SystemTag> aListOfSystems) {
        this.aListOfSystems = aListOfSystems;
    }

    @Override
    public String toString() {
        return "SystemsTag{" +
                "aListOfSystems=" + aListOfSystems +
                '}';
    }
}
