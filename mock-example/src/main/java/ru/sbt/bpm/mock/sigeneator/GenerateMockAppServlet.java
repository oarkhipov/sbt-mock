package ru.sbt.bpm.mock.sigeneator;

import javafx.util.Pair;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
// Singleton
public class GenerateMockAppServlet {

    private static GenerateMockAppServlet INSTANCE = null;

    private String aFilePath;

    private GenerateMockAppServlet() {
        this.aFilePath = "";
    }

    private GenerateMockAppServlet(String aFilePath) {
        this.aFilePath = aFilePath;
    }

    public static GenerateMockAppServlet getInstance() {
        if (INSTANCE == null)
            synchronized (GenerateMockAppServlet.class) {
                if(INSTANCE == null)
                    INSTANCE = new GenerateMockAppServlet();
            }
        return INSTANCE;
    }

    public static GenerateMockAppServlet getInstance(String aFilePath) {
        if (INSTANCE == null)
            synchronized (GenerateMockAppServlet.class) {
                if(INSTANCE == null)
                    INSTANCE = new GenerateMockAppServlet(aFilePath);
            }
        return INSTANCE;
    }

    public String getaFilePath() {
        return aFilePath;
    }

    public void setaFilePath(String aFilePath) {
        this.aFilePath = aFilePath;
    }

    @Override
    public String toString() {
        return "GenerateMockAppServlet{" +
                "aFilePath='" + aFilePath + '\'' +
                '}';
    }
}
