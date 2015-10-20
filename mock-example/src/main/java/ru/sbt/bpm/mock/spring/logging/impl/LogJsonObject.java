package ru.sbt.bpm.mock.spring.logging.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by sbt-vostrikov-mi on 14.10.2015.
 */
public class LogJsonObject {

    @Getter
    @Setter
    int iTotalRecords;

    @Getter
    @Setter
    int iTotalDisplayRecords;

    @Getter
    @Setter
    String sEcho;

    @Getter
    @Setter
    String sColumns;

    @Getter
    @Setter
    List<LogEntry> aaData;
}
