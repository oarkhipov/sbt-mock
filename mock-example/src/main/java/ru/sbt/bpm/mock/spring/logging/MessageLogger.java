package ru.sbt.bpm.mock.spring.logging;


/**
 * Created by sbt-bochev-as on 21.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */
public interface MessageLogger {

    void logMessage(Object payload, String queueName);
}
