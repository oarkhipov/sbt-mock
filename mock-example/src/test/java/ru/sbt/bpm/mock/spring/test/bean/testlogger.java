package ru.sbt.bpm.mock.spring.test.bean;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbt.bpm.mock.spring.logging.MessageLogger;
import ru.sbt.bpm.mock.spring.logging.impl.LogEntry;
import ru.sbt.bpm.mock.spring.logging.impl.MemoryMessageLoggerImpl;

import java.util.Iterator;

/**
 * Created by sbt-vostrikov-mi on 08.10.2015.
 */
public class testlogger {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testMemoryLogger() {
        MemoryMessageLoggerImpl messageLogger = new MemoryMessageLoggerImpl();

        for (int i=0; i<1000; i++) {
            messageLogger.logMessage("some logg message", "TEST.QUEUE");
            try {
                Thread.sleep(100);
            } catch (Throwable e){

            }
        }
        log.info(messageLogger.getQueueList().toString());

        Iterator<LogEntry> it = messageLogger.getLogList("TEST.QUEUE");
        while (it.hasNext()) {
            log.info(it.next().toString());
        }
    }
}
