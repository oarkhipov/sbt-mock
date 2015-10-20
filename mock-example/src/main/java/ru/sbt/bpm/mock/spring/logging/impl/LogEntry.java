package ru.sbt.bpm.mock.spring.logging.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by sbt-vostrikov-mi on 09.10.2015.
 */
public class LogEntry {
    @Getter
    @Setter
    String timestamp;
    @Getter @Setter
    String queue;
    @Getter @Setter
    String msgtype;
    @Getter @Setter
    String payload;

    public LogEntry(String timestamp, String queue, String msgtype, String payload) {
        setPayload(payload);
        setQueue(queue);
        setMsgtype(msgtype);
        setTimestamp(timestamp);
    }

    @Override
    public String toString() {
        return timestamp + ":" + queue+"/"+msgtype+"["+payload+"]";
    }
}
