package ru.sbt.bpm.mock.generator.spring.integration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
@ToString
public class Pair<A, B> implements Serializable {

    @Getter
    @Setter
    private A first;

    @Getter
    @Setter
    private B second;

    public Pair() {

    }

    public Pair(A aFirst, B aSecond) {
        this.first = aFirst;
        this.second = aSecond;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof Pair))
            return false;

        Pair<?, ?> that = (Pair<?, ?>) obj;
        // Решение build для Java 1.6!!!
        return this.first.equals(that.first) && this.second.equals(that.second);
    }
}
