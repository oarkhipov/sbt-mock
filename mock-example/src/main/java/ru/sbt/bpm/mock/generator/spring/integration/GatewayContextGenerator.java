package ru.sbt.bpm.mock.generator.spring.integration;

import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.SystemTag;

import java.util.*;

/**
 * Created by sbt-hodakovskiy-da on 29.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */

// Генератор Inbound and Outbound gateway
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
    // <Тип точки интеграции, <Входной канал, Выходной канал>>
    private Map<String, Set<Pair<String, String>>> mapOfInOutChannelsWithoutDuplicates;

    public GatewayContextGenerator(List<SystemTag> systems) {
        this.aSystems = systems;
        this.mapOfInOutChannels = new HashMap<String, Map<String, Pair<String, String>>>();
        this.mapOfInOutChannelsByType = new HashMap<String, Map<String, Pair<String, String>>>();
        this.mapOfInOutChannelsWithoutDuplicates = new HashMap<String, Set<Pair<String, String>>>();
    }

    // Раскладываем в мап все каналы
    public void putChannelsToMap() {
        if (aSystems == null || aSystems.isEmpty())
            return;
        // Set<E> для удаления дубоикатов очередей
        Set<Pair<String, String>> setOfInChannels = new HashSet<Pair<String, String>>();
        Set<Pair<String, String>> setOfOutChannels = new HashSet<Pair<String, String>>();

        for (SystemTag system : aSystems) {
            Map<String, Pair<String, String>> mapOfIntPointChannels = new HashMap<String, Pair<String, String>>();
            Map<String, Pair<String, String>> mapOfIntPointChannelsByType = new HashMap<String, Pair<String, String>>();

            for (IntegrationPoint intPoint : system.getListIntegrationPoint()) {
                intPoint.setPairOfChannels(new Pair<String, String>(intPoint.getIncomeQueue(), intPoint.getOutcomeQueue()));
                mapOfIntPointChannels.put(intPoint.getIntegrationPointName(), intPoint.getPairOfChannels());
                mapOfIntPointChannelsByType.put(intPoint.getIntegrationPointType(), intPoint.getPairOfChannels());

                if (intPoint.getIntegrationPointType().equals(MOCK_CONST))
                    setOfInChannels.add(intPoint.getPairOfChannels());
                else
                    setOfOutChannels.add(intPoint.getPairOfChannels());
            }

            mapOfInOutChannels.put(system.getSystemName(), mapOfIntPointChannels);
            mapOfInOutChannelsByType.put(system.getSystemName(), mapOfIntPointChannelsByType);
            mapOfInOutChannelsWithoutDuplicates.put(MOCK_CONST, setOfInChannels);
            mapOfInOutChannelsWithoutDuplicates.put(DRIVER_CONST, setOfOutChannels);
        }
    }

    // Получение inbound & outbound gateway
    public String getInboundAndOutboundGateway() {
        String sbIN = null;
        String sbOUT = null;
        StringBuilder sbCommon = new StringBuilder();

        for (Pair<String, String> mock : mapOfInOutChannelsWithoutDuplicates.get(MOCK_CONST))
            sbCommon.append(generateGatewayDescription("inbound", "jmsin_" + Double.valueOf(Math.random() * 10000).longValue(), mock.getaFirst(), mock.getaSecond()));

        for (Pair<String, String> driver : mapOfInOutChannelsWithoutDuplicates.get(MOCK_CONST))
            sbCommon.append(generateGatewayDescription("outbound", "jmsout_" + Double.valueOf(Math.random() * 10000).longValue(), driver.getaFirst(), driver.getaSecond()));

        sbCommon.append(sbIN);
        sbCommon.append(sbOUT);
        return sbCommon.toString();
    }

    // Собирание строки для gateway
    private String generateGatewayDescription(String typeGateway, String id, String request, String reply) {
        return "<" + typeGateway + "-gateway id=\"" + id + "\" request-channel=\"" + request + "\" " +
                "reply-channel=\"" + reply + "\"/>\n\n";
    }


    public List<SystemTag> getaSystems() {
        return aSystems;
    }

    public void setaSystems(List<SystemTag> aSystems) {
        this.aSystems = aSystems;
    }
}
