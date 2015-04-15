package ru.sbt.bpm.mock.generator.spring.integration;

import lombok.*;

import java.io.Serializable;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 *
 * Company: SBT - Saint-Petersburg
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Pair<A, B> implements Serializable {

    @Getter
    @Setter
    private A first;

    @Getter
    @Setter
    private B second;
}
