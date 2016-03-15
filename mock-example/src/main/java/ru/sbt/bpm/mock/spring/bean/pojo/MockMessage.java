package ru.sbt.bpm.mock.spring.bean.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.sbt.bpm.mock.config.enums.Protocol;

/**
 * @author sbt-bochev-as on 10.02.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Data
@AllArgsConstructor
public class MockMessage {
    private Protocol protocol;
    private String MQConnectionFactoryName;
    //Queue or EndpointName
    private String endpointName;
    private String payload;
}
