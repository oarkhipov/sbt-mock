package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
* Created by sbt-hodakovskiy-da on 30.01.2015.
* <p/>
* Company: SBT - Saint-Petersburg
*/

@XStreamAlias("system")
@ToString
public class SystemTag {

    @XStreamAlias("name")
    @XStreamAsAttribute
    @Getter
    @Setter
    private String systemName;

    @XStreamAlias("pathToXSD")
    @Getter
    @Setter
    private String pathToXSD;

    @XStreamAlias("rootXsd")
    @Getter
    @Setter
    private String rootXSD;

    @XStreamAlias("headerNamespace")
    @Getter
    @Setter
    private String headerNamespace;

    @XStreamAlias("namespace-aliases")
    @Getter
    @Setter
    private NamespaceAliases namespaceAliases;

    @XStreamAlias("integrationPoints")
    @Getter
    @Setter
    private IntegrationPoints integrationPoints;

    @XStreamAlias("dependencies")
    @Setter
    private Dependencies dependencies;

    public List<Dependency> getDependencies() {
        if (dependencies!=null) {
            return dependencies.getDependencies();
        }
        return new ArrayList<Dependency>();
    }
    public List<IntegrationPoint> getListIntegrationPoint() {
        return integrationPoints.getListOfIntegrationPoints();
    }

    public List<String> getIntegrationPointNames() {
        List<String> result = new ArrayList<String>();
        for (IntegrationPoint point : getListIntegrationPoint()) {
            result.add(point.getIntegrationPointName());
        }
        return result;
    }
    /**
     * наследование алиасов.  Получаем алиасы сверху и сохраняем себе. Потом передаем алиасы внутрь иерархии, чтобы они взяли их себе
     */
    public void inheritNamespaceAliases(NamespaceAliases ns) {
        if (ns != null) {
            for (NamespaceAliase nsToAdd : ns.getListOfNamespaces()) {
                if (namespaceAliases == null) {
                    setNamespaceAliases(new NamespaceAliases());
                }
                namespaceAliases.addNamespaces(nsToAdd);
            }
        }
        for (IntegrationPoint ip : getListIntegrationPoint() ) {
            ip.inheritNamespaceAliases(namespaceAliases);
        }
    }
}
