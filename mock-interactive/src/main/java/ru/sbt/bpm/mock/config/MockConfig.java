package ru.sbt.bpm.mock.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import ru.sbt.bpm.mock.config.entities.MainConfig;
import ru.sbt.bpm.mock.config.entities.Systems;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
@XStreamAlias("MockConfig")
@Data
public class MockConfig {

    @XStreamAlias("mainConfig")
    private MainConfig mainConfig;

    @XStreamAlias("systems")
    private Systems systems;

    public Systems getSystems() {
        if (systems == null) {
            systems = new Systems();
        }
        return systems;
    }
}
