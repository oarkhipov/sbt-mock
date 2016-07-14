package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;


/**
 * @author sbt-hodakovskiy-da
 * @author sbt-bochev-as
 *         on 30.01.2015
 *         <p/>
 *         Company: SBT - Saint-Petersburg
 */
@XStreamAlias("integrationPoint")
@Data
@AllArgsConstructor
public class IntegrationPoint {

    // Тип точки интеграции Driver
    public static final String DRIVER = "Driver";
    // Тип точки интеграции Mock
    public static final String MOCK = "Mock";

    @XStreamAlias("name")
    @XStreamAsAttribute
    private String name;

    @XStreamAlias("type")
    private String integrationPointType;

    @XStreamAlias("delayMs")
    private Integer delayMs;

    @XStreamAlias("xpathValidation")
    private XpathSelector xpathValidatorSelector;

    //  For override
    @XStreamAlias("incomeQueue")
    private String incomeQueue;

    //  For override
    @XStreamAlias("outcomeQueue")
    private String outcomeQueue;

    @XStreamAlias("answerRequired")
    private Boolean answerRequired;

    // Так как маппинг идет по полям xml, для удобства доступа и сравнения создаем Tuple2<INCOME, OUTCOME>
    private transient Tuple2<String, String> pairOfChannels;
    @XStreamAlias("xsdFile")
    private String xsdFile;
    @XStreamAlias("rootElement")
    private ElementSelector rootElement;

    public Tuple2<String, String> getPairOfChannels() {
        return Tuple.of(this.getIncomeQueue(), this.getOutcomeQueue());
    }

    public void setPairOfChannels(Tuple2<String, String> pairOfChannels) {
        this.setIncomeQueue(pairOfChannels.getT1());
        this.setOutcomeQueue(pairOfChannels.getT2());
    }

    public boolean isMock() {
        return integrationPointType.equals(MOCK);
    }

    public boolean isDriver() {
        return integrationPointType.equals(DRIVER);
    }

    public String getXpathString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (xpathValidatorSelector != null) {
            for (ElementSelector elementSelector : xpathValidatorSelector.getElementSelectors()) {
                stringBuilder.append(elementSelector.getElement()).append("/");
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }
        return stringBuilder.toString();
    }

    public String getXpathWithFullNamespaceString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ElementSelector elementSelector : xpathValidatorSelector.getElementSelectors()) {
            stringBuilder
                    .append("/*:")
                    .append(elementSelector.getElement())
                    .append("[namespace-uri()='")
                    .append(elementSelector.getNamespace())
                    .append("']");
        }
//        stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
        return stringBuilder.toString();
    }
}
