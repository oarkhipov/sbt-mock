package ru.sbt.bpm.mock.logging.entities;

import lombok.Data;

import java.util.List;

/**
 * @author sbt-bochev-as on 24.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Data
public class LogsJsonEntity {
    int requestNum;
    int start;
    int length;
    boolean searchRegex;
    String searchValue;
    List<LogsJsonColumnEntity> logsJsonColumnEntities;
    List<LogsJsonOrderEntity> logsJsonOrderEntities;
}
