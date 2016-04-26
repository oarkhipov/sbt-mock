package ru.sbt.bpm.mock.spring.logging;

import ru.sbt.bpm.mock.spring.logging.impl.LogEntry;
import ru.sbt.bpm.mock.spring.logging.impl.MemoryMessageLoggerImpl;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by sbt-vostrikov-mi on 09.10.2015.
 */
public interface MemoryMessageLogger extends MessageLogger {

    Set<String> getQueueList();
    Iterator<LogEntry> getLogList(String queueName);
}
