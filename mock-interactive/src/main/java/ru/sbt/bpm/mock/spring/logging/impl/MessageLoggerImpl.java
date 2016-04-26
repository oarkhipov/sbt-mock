package ru.sbt.bpm.mock.spring.logging.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.spring.logging.MessageLogger;

/**
 * Created by sbt-bochev-as on 26.05.2015.
 *
 * Company: SBT - Moscow
 */
@Service
public class MessageLoggerImpl implements MessageLogger {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public void logMessage(Object payload, String queueName) {
        log.info("["+queueName+"] " + payload.toString().substring(0,50));
    }
}
