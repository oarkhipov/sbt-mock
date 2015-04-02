package ru.sbt.bpm.mock.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.sbt.bpm.mock.config.entities.SystemTag;
import ru.sbt.bpm.mock.config.entities.SystemsTag;

import java.util.List;

/**
* Created by sbt-hodakovskiy-da on 30.01.2015.
* <p/>
* Company: SBT - Saint-Petersburg
*/
@XStreamAlias("MockConfig")
@ToString
public class MockConfig {

    @XStreamAlias("systems")
    @Getter
    @Setter
    private SystemsTag systems;

    public List<SystemTag> getListOfSystems() {
        return systems.getListOfSystems();
    }
}
