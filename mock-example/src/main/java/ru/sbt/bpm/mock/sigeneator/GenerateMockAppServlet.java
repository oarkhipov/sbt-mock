package ru.sbt.bpm.mock.sigeneator;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import ru.sbt.bpm.mock.sigeneator.inentities.*;

import java.io.FileReader;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
// Singleton
public class GenerateMockAppServlet {

    private static GenerateMockAppServlet INSTANCE = null;

    private String aFilePath;

    private MockConfig aMockConfig;

    private GenerateMockAppServlet() {
        this.aFilePath = "";
    }

    private GenerateMockAppServlet(String aFilePath) {
        this.aFilePath = aFilePath;
    }

    public void init() throws Exception{
        if (this.aFilePath == null || this.aFilePath.equals(""))
            throw new Exception();

        FileReader fileReader = new FileReader(aFilePath);
        XStream xStream = new XStream(new DomDriver());

        // Mapping данных из xml в классы
        xStream.processAnnotations(MockConfig.class);
        xStream.processAnnotations(SystemsTag.class);
        xStream.processAnnotations(SystemTag.class);
        xStream.processAnnotations(IntegrationPoints.class);
        xStream.processAnnotations(IntegrationPoint.class);
        xStream.processAnnotations(LinkedTagSequence.class);
        xStream.processAnnotations(LinkedTag.class);
        xStream.processAnnotations(Dependencies.class);
        xStream.processAnnotations(Dependency.class);
        // parse
        this.aMockConfig = (MockConfig) xStream.fromXML(fileReader);

        // Дальше пишем куски для каждого блока SI context

        System.out.println("End");
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

    public MockConfig getaMockConfig() {
        return aMockConfig;
    }

    @Override
    public String toString() {
        return "GenerateMockAppServlet{" +
                "aFilePath='" + aFilePath + '\'' +
                '}';
    }
}
