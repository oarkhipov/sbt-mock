package ru.sbt.bpm.mock.spring.logging.pojo;

import lombok.NoArgsConstructor;
import ru.sbt.bpm.mock.logging.entities.LogsEntity;

/**
 * @author sbt-bochev-as on 28.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@NoArgsConstructor
public class LogResponseDataEntity {
    String ts;
    String protocol;
    String systemName;
    String integrationPointName;
    String fullEndpointName;
    String shortEndpointName;
    String messageState;
    String messagePreview;


    public LogResponseDataEntity(LogsEntity entity) {
        ts = entity.getTs().toString();
        protocol = entity.getProtocol();
        systemName = entity.getSystemName();
        integrationPointName = entity.getIntegrationPointName();
        fullEndpointName = entity.getFullEndpoint();
        shortEndpointName = entity.getShortEndpoint();
        messageState = entity.getMessageState();
        messagePreview = entity.getMessagePreview();
    }
}
