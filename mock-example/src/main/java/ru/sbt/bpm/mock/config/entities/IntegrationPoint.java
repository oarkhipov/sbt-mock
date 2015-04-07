package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.sbt.bpm.mock.generator.spring.integration.Pair;

import java.util.ArrayList;
import java.util.List;

/**
* Created by sbt-hodakovskiy-da on 30.01.2015.
* <p/>
* Company: SBT - Saint-Petersburg
*/
@XStreamAlias("integrationPoint")
@ToString
public class IntegrationPoint {

    @XStreamAlias("name")
    @XStreamAsAttribute
    @Getter
    @Setter
    private String integrationPointName;

    @XStreamAlias("operationName")
    @Getter
    @Setter
    private String operationName;

    @XStreamAlias("rsRootElementName")
    @Getter
    @Setter
    private String rsRootElementName;

    @XStreamAlias("rqRootElementName")
    @Getter
    @Setter
    private String rqRootElementName;

    @XStreamAlias("type")
    @Getter
    @Setter
    private String integrationPointType;

    @XStreamAlias("namespace-aliases")
    @Getter
    @Setter
    private NamespaceAliases namespaceAliases;

    @XStreamAlias("linkedTagSequence")
    @Getter
    @Setter
    private LinkedTagSequence linkedTagSequence;

    @XStreamAlias("protocol")
    @Getter
    @Setter
    private String protocol;

    @XStreamAlias("incomeQueue")
    @Getter
    @Setter
    private String incomeQueue;

    @XStreamAlias("outcomeQueue")
    @Getter
    @Setter
    private String outcomeQueue;

    // Так как маппинг идет по полям xml, для удобства доступа и сравнения создаем Pair<INCOME, OUTCOME>
    @Getter
    @Setter
    private Pair<String, String> pairOfChannels;

    @XStreamAlias("xsdFile")
    @Getter
    @Setter
    private String xsdFile;

    @XStreamAlias("RqXsdFile")
    @Getter
    @Setter
    private String rqXsdFile;

    @XStreamAlias("dataxml")
    @Getter
    @Setter
    private String dataXml;

    @XStreamAlias("dependencies")
    @Setter
    private Dependencies dependencies;

    public List<Dependency> getDependencies() {
        if (dependencies!=null) {
            return dependencies.getDependencies();
        }
        return new ArrayList<Dependency>();
    }

    public IntegrationPoint() {
        linkedTagSequence = null;
    }

    /**
     * наследование алиасов. Получаем алиасы сверху и сохраняем себе
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
    }
}
