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
    String transactionId;
    String protocol;
    String systemName;
    String integrationPointName;
    String fullEndpoint;
    String shortEndpoint;
    String messageState;


    public LogResponseDataEntity(LogsEntity entity) {
        ts = entity.getTs().toString();
        transactionId = entity.getTransactionId()!=null?entity.getTransactionId():"-- Unknown --";
        protocol = entity.getProtocol();
        systemName = entity.getSystemName();
        integrationPointName = entity.getIntegrationPointName();
        fullEndpoint = entity.getFullEndpoint();
        shortEndpoint = entity.getShortEndpoint();
        messageState = entity.getMessageState();
    }
}
