package ru.sbt.bpm.mock.spring.logging.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by sbt-vostrikov-mi on 14.10.2015.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogJsonObject {
    int iTotalRecords;
    int iTotalDisplayRecords;
    String sEcho;
    String sColumns;
    List<LogEntry> aaData;
}
