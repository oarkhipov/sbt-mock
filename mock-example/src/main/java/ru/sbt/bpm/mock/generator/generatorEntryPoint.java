package ru.sbt.bpm.mock.generator;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * класс будет содержать только main-метод для вызова генероатора
 * временное решение пока мы не придумаем как вызывать его по=лругому
 * //TODO возможно, есть более элегентный способ вызова генератора, не через main
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class generatorEntryPoint {

    /**
     * точка входа для запуска вне сервиса
     * @throws Exception
     */
    public static void main(String [] args) throws Exception {
        configLoader instance = configLoader.getInstance();

        for (String arg : args) {
            if (arg.equals("NCP")) {
                instance.loadConfig("NCPConfig.xml");
            }
            if (arg.equals("BBMO")) {
                instance.loadConfig("BBMOConfig.xml");
            }
            if (arg.equals("KKMB")) {
                instance.loadConfig("KKMBConfig.xml");
            }
            if (arg.equals("clear")) {
                clear();
            }
        }
    }

    /**
     * нужно для очистки рабочих дирректорий
     */
    public static void clear() throws Exception {
        File data = new File(localPaths.getWebInfPath() + File.separator + "data");
        File xsd = new File(localPaths.getWebInfPath() + File.separator + "xsd");
        File xsl = new File(localPaths.getWebInfPath() + File.separator + "xsl");
        File testxml = new File(localPaths.getExamplesPath());

        clearDir(data);
        clearDir(xsd);
        clearDir(xsl);
        clearDir(testxml);

    }

    private static void clearDir(File f) throws Exception {
        for (File toDel : f.listFiles()) {
            if (toDel.isDirectory() && !toDel.getName().equals("util")
                    && !toDel.getName().equals("MockConfigFiles")
                    && !toDel.getName().equals("test") ) {
                FileUtils.deleteDirectory(toDel);
            }
        }
    }

}
