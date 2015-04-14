package ru.sbt.bpm.mock.generator.util;

/**
 * класс для создания инкриментной строки
 * Created by sbt-vostrikov-mi on 10.04.2015.
 */
public class IncrimentingString {
    private int maxValue;
    private int chars;

    private int num;

    /**
     * @param maxValue предполагаемое максимальное значение. В зваисимости от него будет определн отступ - например 0, 00 или 000
     */
    public IncrimentingString(int maxValue) {
        this.maxValue = maxValue;
        this.chars = Integer.toString(maxValue).length();
        this.num = 0;
    }

    /**
     * @return возвращает каждый раз новое число из последовательности, например:
     * 0 1 2 3 4 5 6 ...
     * или если maxValue в конструкторе было от 10 до 99, то
     * 01 02 03 04 05 ...
     * или если maxValue в конструкторе было от 100 до 999, то
     * 001 002 003 004 005 ...
     * если maxValue в конструкторе было 1, то не возвращает вообще ничего
     */
    public String get() {
        return get("");
    }
    /**
     * @return возвращает каждый раз новое число из последовательности. Переде числом вставлеят startingDelimeter, например:
     * _0 _1 _2 _3 _4 _5 _6 ...
     * или если maxValue в конструкторе было от 10 до 99, то
     * _01 _02 _03 _04 _05 ...
     * или если maxValue в конструкторе было от 100 до 999, то
     * _001 _002 _003 _004 _005 ...
     * если maxValue в конструкторе было 1, то не возвращает вообще ничего
     */
    public String get(String startingDelimeter) {
        num++;
        if (maxValue <= 1 || num<=1) {
            return "";
        } else
        {
            return startingDelimeter + Integer.toString(num,chars);
        }
    }

}
