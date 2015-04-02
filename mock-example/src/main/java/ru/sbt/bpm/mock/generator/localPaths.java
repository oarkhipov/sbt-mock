package ru.sbt.bpm.mock.generator;

/**
 * статичный класс для доступа к локальным путям в проекте во время генерации
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class localPaths {
    /**
     * @return путь к корню
     */
    public static String getPath() {
        //TODO это не совсем правильный способ получения пути.
        return System.getProperty("user.dir");
    }

    /**
     * @return путь к папке WEB-INF
     */
    public static String getWebInfPath() {
        return getPath() + "\\src\\main\\webapp\\WEB-INF";
    }

    /**
     * @return путь к папке \src\main\resources
     */
    public static String getSrcResorcesPath() {
        return getPath() + "\\src\\main\\resources";
    }

    /**
     * @return путь к папке с примерами xml для тестов
     */
    public static String getExamplesPath() {
        return getPath() + "\\src\\test\\resources\\xml";
    }
}
