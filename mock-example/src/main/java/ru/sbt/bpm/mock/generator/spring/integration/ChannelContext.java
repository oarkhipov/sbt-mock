package ru.sbt.bpm.mock.generator.spring.integration;

import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.SystemTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 05.02.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */

// Генерция контекста каналов
// TODO Подумать над переачей alias namespace как перфикса для тега
public class ChannelContext {

    private static final String CHANNEL_TAG = "channel";

    private List<SystemTag> aSystems;

    private List<String> mockIntPointsChannels;

    public ChannelContext(List<SystemTag> aSystems) {
        this.aSystems = aSystems;
        this.mockIntPointsChannels = new ArrayList<String>();
    }

    /**
     * Формирование списка каналов по точкам интеграции только для Mock
     * Driver не содержится в servlet config context файле
     */
    public void putChannelsIntoList() {
        for (SystemTag system : aSystems)
            for (IntegrationPoint intPoint : system.getListOfIntegrationPoints())
                if (intPoint.getaIntegrationPointType().equals("Mock"))
                    this.mockIntPointsChannels.add(intPoint.getaIntegrationPointName());
    }

    /**
     * Формирование тега channel для точек типа Mock
     */
    public String generateChannelsWithOutLogger() {
        StringBuilder sb = new StringBuilder();
        for (String  channelName : mockIntPointsChannels)
            sb.append("<" + CHANNEL_TAG + "id=\"" + channelName + "\"/>");
        return sb.toString();
    }

    /**
     * Формирование списка очередей для того, чтобы сформировать каналы по очередям
     */
    public void putQueueIntoList() {

    }
}