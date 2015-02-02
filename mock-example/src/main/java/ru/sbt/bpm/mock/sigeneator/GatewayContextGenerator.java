package ru.sbt.bpm.mock.sigeneator;

import ru.sbt.bpm.mock.sigeneator.inentities.IntegrationPoint;
import ru.sbt.bpm.mock.sigeneator.inentities.SystemTag;

import java.util.*;

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

    // <Система, <Точка интеграции, <Входной канал, Выходной канал>>>
    private Map<String, Map<String, Pair<String, String>>> mapOfInOutChannels;
    // <Система, <Тип точки интеграции, <Входной канал, Выходной канал>>>
    private Map<String, Map<String, Pair<String, String>>> mapOfInOutChannelsByType;
    // Данный map служит для хранения уникальных значений каналов по их типу
    // <Система, <Тип точки интеграции, <Входной канал, Выходной канал>>>
    private Map<String, Map<String, Pair<String, String>>> mapOfInOutChannelsWithoutDuplicates;

    public GatewayContextGenerator(List<SystemTag> systems) {
        this.aSystems = systems;
        this.mapOfInOutChannels = new HashMap<String, Map<String, Pair<String, String>>>();
        this.mapOfInOutChannelsByType = new HashMap<String, Map<String, Pair<String, String>>>();
        this.mapOfInOutChannelsWithoutDuplicates = new HashMap<String, Map<String, Pair<String, String>>>();
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

    // Исключение дубликатов
    public void removeDuplicates() {
        Object[] keysSystem = mapOfInOutChannelsByType.keySet().toArray();
        int sizeKeysSystem = keysSystem.length;

        for (int i = 0; i < sizeKeysSystem - 1; i++)
            for (int j = i + 1; j < sizeKeysSystem; j++) {
                HashMap<String, Pair<String, String>> hashMap = new HashMap<String, Pair<String, String>>();
                boolean mockFlag = false;
                boolean driverFlag = false;
                if (mapOfInOutChannelsByType.get(keysSystem[i]).get(MOCK_CONST).equals(mapOfInOutChannelsByType.get(keysSystem[j]).get(MOCK_CONST))) {
                    mockFlag = true;
                } else {

                }

                if (mapOfInOutChannelsByType.get(keysSystem[i]).get(DRIVER_CONST).equals(mapOfInOutChannelsByType.get(keysSystem[j]).get(DRIVER_CONST))) {
                    driverFlag = true;
                } else {

                }
            }
    }

    // Получение inbound & outbound gateway
    public String getInboundAndOutboundGateway() {
        String sbIN = null;
        String sbOUT = null;
        StringBuilder sbCommon = new StringBuilder();

        for (String  key : mapOfInOutChannelsByType.keySet())
            for (String keyType : mapOfInOutChannelsByType.get(key).keySet())
                if (keyType.equals(DRIVER_CONST)) {
                    sbOUT = getGatewayDescription("outbound", "jmsout_" + key, mapOfInOutChannelsByType.get(key).get(keyType).getaFirst(), mapOfInOutChannelsByType.get(key).get(keyType).getaSecond());
                } else if(keyType.equals(MOCK_CONST)) {
                    sbIN = getGatewayDescription("inbound", "jmsin_" + key, mapOfInOutChannelsByType.get(key).get(keyType).getaFirst(), mapOfInOutChannelsByType.get(key).get(keyType).getaSecond());
                }

        sbCommon.append(sbIN);
        sbCommon.append(sbOUT);
        return sbCommon.toString();
    }

    // Собирание строки для gateway
    private String getGatewayDescription(String typeGateway, String id, String request, String reply) {
        return "<" + typeGateway + "-gateway id=\"" + id + "\" request-channel=\"" + request + "\" " +
                "reply-channel=\"" + reply + "\"/>/n/n";
    }


    public List<SystemTag> getaSystems() {
        return aSystems;
    }

    public void setaSystems(List<SystemTag> aSystems) {
        this.aSystems = aSystems;
    }
}
