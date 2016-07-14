package ru.sbt.bpm.mock.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import ru.sbt.bpm.mock.config.entities.Systems;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
@XStreamAlias("MockConfig")
@Data
public class MockConfig {

    @XStreamAlias("systems")
    private Systems systems;
}
