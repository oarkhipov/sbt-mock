package ru.sbt.bpm.mock.generator;

import java.io.File;

/**
 * статичный класс для доступа к локальным путям в проекте во время генерации
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class LocalPaths {
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
    public static String getSrcResorcesXSLPath() throws NullPointerException {
        //LocalPaths.class.getClassLoader().getResource("xsl").getFile();
        return LocalPaths.class.getClassLoader().getResource("xsl").getFile();
    }

    public static String getSrcResorcesInPath() throws NullPointerException {
        File file = new File(LocalPaths.class.getClassLoader().getResource("in").getFile());
        return file.getAbsolutePath();
//        return LocalPaths.class.getClassLoader().getResource("in").getFile();
    }

    /**
     * @return путь к папке с примерами xml для тестов
     */
    public static String getExamplesPath() {
        return getPath() + "\\src\\test\\resources\\xml";
    }
}
