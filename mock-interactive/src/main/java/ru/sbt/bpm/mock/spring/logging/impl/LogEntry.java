package ru.sbt.bpm.mock.spring.logging.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by sbt-vostrikov-mi on 09.10.2015.
 */
@Data
@AllArgsConstructor
public class LogEntry {
    String timestamp;
    String queue;
    String msgtype;
    String payload;
}
