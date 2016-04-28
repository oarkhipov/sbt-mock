package ru.sbt.bpm.mock.logging.entities;

import lombok.Data;
import ru.sbt.bpm.mock.logging.enums.OrderDirection;

/**
 * @author sbt-bochev-as on 24.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Data
public class LogsApiOrderEntity {
    private int columnNum;
    private OrderDirection direction;
}