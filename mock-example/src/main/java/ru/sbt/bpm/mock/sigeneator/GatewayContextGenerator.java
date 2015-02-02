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
    private Map<String, Map<String, Pair<String, String>>> mapOfInOutChannelsByType;

    public GatewayContextGenerator(List<SystemTag> systems) {
        this.aSystems = systems;
        this.mapOfInOutChannels = new HashMap<String, Map<String, Pair<String, String>>>();
        this.mapOfInOutChannelsByType = new HashMap<String, Map<String, Pair<String, String>>>();
    }

    // Раскладываем в мап все каналы
    public void putChannelsToMap() {
        if (aSystems == null || aSystems.isEmpty())
            return;

        for (SystemTag system : aSystems) {
            Map<String, Pair<String, String>> mapOfIntPointChannels = new HashMap<String, Pair<String, String>>();
            Map<String, Pair<String, String>> mapOfIntPointChannelsByType = new HashMap<String, Pair<String, String>>();

            for (IntegrationPoint intPoint : system.getListOfIntegrationPoints()) {
                intPoint.setaPairOfChannels(new Pair<String, String>(intPoint.getaIncomeQueue(), intPoint.getaOutcomeQueue()));
                mapOfIntPointChannels.put(intPoint.getaIntegrationPointName(), intPoint.getaPairOfChannels());
                mapOfIntPointChannelsByType.put(intPoint.getaIntegrationPointType(), intPoint.getaPairOfChannels());
            }

            mapOfInOutChannels.put(system.getaSystemName(), mapOfIntPointChannels);
            mapOfInOutChannelsByType.put(system.getaSystemName(), mapOfIntPointChannelsByType);
        }
    }

    // Исклбючение дубликатов
    public Map<String, Pair<String, String>> removeDuplicates() {


        return new HashMap<>();
    }

    // Получение inbound & outbound gateway
    public Pair<String, String> getInboundAndOutboundGateway() {
        StringBuilder sbIN = new StringBuilder();
        StringBuilder sbOUT = new StringBuilder();

        for (String  key : mapOfInOutChannelsByType.keySet())
            for (String keyType : mapOfInOutChannelsByType.get(key).keySet())
                if (keyType.equals(DRIVER_CONST)) {
                    sbOUT.append("<outbound-gateway id=\"jmsout"
                            + key + "\" request-channel=\"" + mapOfInOutChannelsByType.get(key).get(keyType).getaFirst()
                            + "\" reply-channel=\"" + mapOfInOutChannelsByType.get(key).get(keyType).getaSecond()
                            + "\"/>/n/n");
                } else if(keyType.equals(MOCK_CONST)) {
                    sbIN.append("<inbound-gateway id=\"jmsin"
                            + key + "\" request-channel=\"" + mapOfInOutChannelsByType.get(key).get(keyType).getaFirst()
                            + "\" reply-channel=\"" + mapOfInOutChannelsByType.get(key).get(keyType).getaSecond()
                            + "\"/>/n/n");
                }

        return new Pair<String, String>(sbIN.toString(), sbOUT.toString());
    }

    public List<SystemTag> getaSystems() {
        return aSystems;
    }

    public void setaSystems(List<SystemTag> aSystems) {
        this.aSystems = aSystems;
    }
}
