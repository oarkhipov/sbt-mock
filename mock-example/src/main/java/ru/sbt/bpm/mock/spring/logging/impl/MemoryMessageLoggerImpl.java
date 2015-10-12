package ru.sbt.bpm.mock.spring.logging.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.spring.logging.MemoryMessageLogger;
import ru.sbt.bpm.mock.spring.logging.MessageLogger;

import java.util.*;


/**
 * Created by sbt-bochev-as on 26.05.2015.
 *
 * Company: SBT - Moscow
 */
@Service
public class MemoryMessageLoggerImpl implements MemoryMessageLogger {

    //TODO в конфиг
    private int logSize=100;

    private HashMap<String, CircularFifoQueue<LogEntry>> logList;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public MemoryMessageLoggerImpl(){
        logList = new HashMap<String, CircularFifoQueue<LogEntry>>();
    }

    public void logMessage(Object payload, String queueName) {
        if (!logList.containsKey(queueName)) {
            logList.put(queueName, new CircularFifoQueue<LogEntry>(logSize));
        }
        logMessage(payload, logList.get(queueName));
    }

    private void logMessage(Object payload, CircularFifoQueue<LogEntry> queue) {
        Date date = new Date();
        queue.add(new LogEntry(date.getTime(), payload.toString()));
    }

    public Set<String> getQueueList() {
        return logList.keySet();
    }

    public Iterator<LogEntry> getLogList(String queueName) {
        return logList.get(queueName).iterator();
    }
}
