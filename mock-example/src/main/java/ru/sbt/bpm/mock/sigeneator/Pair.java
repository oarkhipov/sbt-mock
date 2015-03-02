package ru.sbt.bpm.mock.sigeneator;

import java.io.Serializable;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
public class Pair<A, B> implements Serializable {

    private A aFirst;
    private B aSecond;

    public Pair() {

    }

    public Pair(A aFirst, B aSecond) {
        this.aFirst = aFirst;
        this.aSecond = aSecond;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof Pair))
            return false;

        Pair<?, ?> that = (Pair<?, ?>) obj;
        // Решение build для Java 1.6!!!
        return this.aFirst.equals(that.aFirst) && this.aSecond.equals(that.aSecond);
    }


    public A getaFirst() {
        return aFirst;
    }

    public void setaFirst(A aFirst) {
        this.aFirst = aFirst;
    }

    public B getaSecond() {
        return aSecond;
    }

    public void setaSecond(B aSecond) {
        this.aSecond = aSecond;
    }

    @Override
    public String toString() {
        return "Pair { aFirst = " + aFirst +  ", aSecond = " + aSecond + '}';
    }
}
