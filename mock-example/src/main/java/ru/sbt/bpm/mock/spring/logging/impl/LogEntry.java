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
    Long timestamp;
    @Getter @Setter
    String payload;

    public LogEntry(Long timestamp, String payload) {
        setPayload(payload);
        setTimestamp(timestamp);
    }

    @Override
    public String toString() {
        Date date = new Date(timestamp);
        return date.toString() + ":" + payload;
    }
}
