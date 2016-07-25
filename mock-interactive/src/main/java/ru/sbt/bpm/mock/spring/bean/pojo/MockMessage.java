package ru.sbt.bpm.mock.spring.bean.pojo;

import lombok.Data;
import ru.sbt.bpm.mock.config.entities.*;
import ru.sbt.bpm.mock.config.enums.Protocol;

import java.util.UUID;

/**
 * @author sbt-bochev-as on 10.02.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Data
public class MockMessage {
    private UUID transactionId;

    private Protocol protocol;
    private ru.sbt.bpm.mock.config.entities.System system;
    private IntegrationPoint integrationPoint;

    //JMS
    private String jmsConnectionFactoryName;
    private String queue;

    private String payload;

    private boolean faultMessage = false;
    private boolean sendMessage = true;

    //for JMS
    public MockMessage(Protocol protocol, String jmsConnectionFactoryName, String queue, String payload) {
        this.protocol = protocol;
        this.jmsConnectionFactoryName = jmsConnectionFactoryName;
        this.queue = queue;
        this.payload = payload;
    }

    //for SOAP
    public MockMessage(String payload) {
        this.payload = payload;
    }
}
