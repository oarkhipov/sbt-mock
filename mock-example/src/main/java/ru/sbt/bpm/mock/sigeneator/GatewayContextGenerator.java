package ru.sbt.bpm.mock.sigeneator;

import ru.sbt.bpm.mock.sigeneator.inentities.IntegrationPoint;
import ru.sbt.bpm.mock.sigeneator.inentities.SystemTag;
import ru.sbt.bpm.mock.sigeneator.inentities.SystemsTag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sbt-hodakovskiy-da on 29.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
public class GatewayContextGenerator {

    // Тип точки интеграции Driver
    private static final String DRIVER_CONST = "Driver";
    // Тип точки интеграции Mock
    private static final String MOCK_CONST = "Mock";

    private List<SystemTag> aSystems;

    private Map<String, Map<String, Pair<String, String>>> mapOfInOutChannels;

    public GatewayContextGenerator(List<SystemTag> systems) {
        this.aSystems = systems;
        this.mapOfInOutChannels = new HashMap<String, Map<String, Pair<String, String>>>();
    }

    // Раскладываем в мап все каналы
    public void putChannelsToMap() {
        if (aSystems == null || aSystems.isEmpty())
            return;

        for (SystemTag system : aSystems) {
            Map<String, Pair<String, String>> mapOfIntPointChannels = new HashMap<String, Pair<String, String>>();

            for (IntegrationPoint intPoint : system.getListOfIntegrationPoints()) {
                intPoint.setaPairOfChannels(new Pair<String, String>(intPoint.getaIncomeQueue(), intPoint.getaOutcomeQueue()));
                mapOfIntPointChannels.put(intPoint.getaIntegrationPointName(), intPoint.getaPairOfChannels());
            }
            mapOfInOutChannels.put(system.getaSystemName(), mapOfIntPointChannels);
        }
    }

    public List<SystemTag> getaSystems() {
        return aSystems;
    }

    public void setaSystems(List<SystemTag> aSystems) {
        this.aSystems = aSystems;
    }
}
