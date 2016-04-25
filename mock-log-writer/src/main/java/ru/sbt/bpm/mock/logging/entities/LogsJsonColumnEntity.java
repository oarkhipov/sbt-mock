package ru.sbt.bpm.mock.logging.entities;

import lombok.Data;

/**
 * @author sbt-bochev-as on 24.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Data
public class LogsJsonColumnEntity {
    private int num;
    private String data;
    private String name;
    private boolean searchable;
    private boolean orderable;
    private String searchValue;
    private boolean searchRegex;
}
