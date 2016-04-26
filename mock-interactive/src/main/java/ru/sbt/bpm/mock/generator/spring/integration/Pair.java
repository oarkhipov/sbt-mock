package ru.sbt.bpm.mock.generator.spring.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 *
 * Company: SBT - Saint-Petersburg
 */
@Data
@AllArgsConstructor
public class Pair<A, B> implements Serializable {
    private A first;
    private B second;
}
