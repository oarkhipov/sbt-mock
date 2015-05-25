package ru.sbt.bpm.mock.spring.test.bean;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

/**
 * Created by sbt-bochev-as on 12.05.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@MessageEndpoint
public class JmsTestBean {

    @ServiceActivator
    public String upperCase(String input) {
        return "JMS response: " + input.toUpperCase();
    }
}
