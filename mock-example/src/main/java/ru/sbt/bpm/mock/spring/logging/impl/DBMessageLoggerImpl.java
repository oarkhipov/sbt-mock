package ru.sbt.bpm.mock.spring.logging.impl;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.spring.logging.MemoryMessageLogger;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by sbt-bochev-as on 26.05.2015.
 *
 * Company: SBT - Moscow
 */
@Service
public class DBMessageLoggerImpl implements MemoryMessageLogger {

    //TODO в конфиг
    private int logSize = 100;

    MongoClient m;
    MongoDatabase mdb;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public DBMessageLoggerImpl() {
        m = new MongoClient();
        mdb = m.getDatabase("logDB");
    }

    public void logMessage(Object payload, String queueName) {
        if (mdb.getCollection(queueName)==null) {
            mdb.createCollection(queueName);
        }
        logMessage(payload, mdb.getCollection(queueName));
    }

    private void logMessage(Object payload, MongoCollection collection) {
        Date date = new Date();
    }

    public Set<String> getQueueList() {
        return null;
    }

    public Iterator<LogEntry> getLogList(String queueName) {
        return null;
    }
}


