package ru.sbt.bpm.mock.spring.logging.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sbt-bochev-as on 28.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@NoArgsConstructor
@Data
public class LogResponseEntity {
    long draw;
    long recordsTotal;
    long recordsFiltered;
    LogResponseDataEntity[] data;
}
