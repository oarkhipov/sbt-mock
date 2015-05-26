package ru.sbt.bpm.mock.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.sbt.bpm.mock.config.entities.NamespaceAliases;
import ru.sbt.bpm.mock.config.entities.SystemTag;
import ru.sbt.bpm.mock.config.entities.SystemsTag;

import java.util.ArrayList;
import java.util.List;

/**
* Created by sbt-hodakovskiy-da on 30.01.2015.
*
* Company: SBT - Saint-Petersburg
*/
@XStreamAlias("MockConfig")
@ToString
public class MockConfig {

    @XStreamAlias("namespace-aliases")
    @Getter
    @Setter
    private NamespaceAliases namespaceAliases;

    @XStreamAlias("systems")
    @Getter
    @Setter
    private SystemsTag systems;

    public List<SystemTag> getListOfSystems() {
        return systems.getListOfSystems();
    }

    public List<String> getIntegrationPointNames() {
        List<String> result = new ArrayList<String>();
        for (SystemTag systemTag : getListOfSystems()) {
            result.addAll(systemTag.getIntegrationPointNames());
        }
        return result;
    }
    /**
     * наследование алиасов. Передаем алиасы внутрь иерархии, чтобы они взяли их себе
     */
    public void inheritNamespaceAliases() {
        for (SystemTag system : getListOfSystems() ) {
            system.inheritNamespaceAliases(namespaceAliases);
        }
    }

    @Getter
    @Setter
    private String filename;
}
