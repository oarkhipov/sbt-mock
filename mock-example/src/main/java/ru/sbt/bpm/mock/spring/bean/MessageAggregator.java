package ru.sbt.bpm.mock.spring.bean;

import lombok.AllArgsConstructor;
import ru.sbt.bpm.mock.config.enums.Protocols;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;

/**
 * @author sbt-bochev-as on 10.02.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@AllArgsConstructor
public class MessageAggregator {
    private Protocols protocol;
    private String MQConnectionFactoryName;
    //Queue or EndpointName
    private String endpointName;

    public MockMessage aggregate(String payload) {
        return new MockMessage(protocol, MQConnectionFactoryName, endpointName, payload);
    }
}
