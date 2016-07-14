package ru.sbt.bpm.mock.config;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.sbt.bpm.mock.config.entities.*;
import ru.sbt.bpm.mock.config.entities.System;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-bochev-as on 02.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@NoArgsConstructor
@Component
public class MockConfigContainer {

    @Autowired
    ApplicationContext applicationContext;

    @Getter
    private MockConfig config;

    //map of wsdl projects for message generation and validation. It initializes at validator initialization method
    @Getter
    private Map<String, WsdlProject> wsdlProjectMap = new HashMap<String, WsdlProject>();


    @Getter
    private String filePath = null;

    /**
     * Создание экземпляра объекта из метода getInstance (CoreJava) или из бина с вызовом конструктора (Java EE)
     *
     * @param filePath путь до конфига
     */
    public MockConfigContainer(String filePath) {
        this.filePath = filePath;
        config = new MockConfig();
    }

    /**
     * десерализация файла в объект конфига при инициализации
     *
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException{
        if (this.filePath == null || this.filePath.equals(""))
            throw new IOException("no config file [" + filePath +"]!");

        File resourceFile;
        if(applicationContext == null) {
            resourceFile = new File(filePath);
        }
        else {
            resourceFile = applicationContext.getResource(filePath).getFile();
        }

        assert resourceFile.exists() : resourceFile.toString() + " not exists";

        FileReader fileReader = new FileReader(resourceFile);
        XStream xStream = new XStream(new DomDriver());

        // Mapping данных из xml в классы
        xStream.processAnnotations(MockConfig.class);
        xStream.processAnnotations(Systems.class);
        xStream.processAnnotations(System.class);
        xStream.processAnnotations(ElementSelector.class);
        xStream.processAnnotations(XpathSelector.class);
        xStream.processAnnotations(IntegrationPoints.class);
        xStream.processAnnotations(IntegrationPoint.class);

        this.config = (MockConfig) xStream.fromXML(fileReader);

    }

    private static MockConfigContainer INSTANCE = null;

    /**
     * Получение экземпляра объекта синглтона средствами core Java
     *
     * @param filePath путь до файла конфига
     */
    public static MockConfigContainer getInstance(String filePath) {
        if (INSTANCE == null)
            synchronized (MockConfigContainer.class) {
                if(INSTANCE == null)
                    INSTANCE = new MockConfigContainer(filePath);
            }
        return INSTANCE;
    }

    public String toXml() {
        XStream xStream = new XStream(new DomDriver());

        // Mapping данных из xml в классы
        xStream.processAnnotations(MockConfig.class);
        xStream.processAnnotations(Systems.class);
        xStream.processAnnotations(System.class);
        xStream.processAnnotations(ElementSelector.class);
        xStream.processAnnotations(XpathSelector.class);
        xStream.processAnnotations(IntegrationPoints.class);
        xStream.processAnnotations(IntegrationPoint.class);

        return xStream.toXML(config);
    }

    /**
     * Returns System by it's name
     *
     * @param systemName name of system
     * @return @ru.sbt.bpm.mock.config.entities.System
     */
    public System getSystemByName(String systemName) {
        return config.getSystems().getSystemByName(systemName);
    }

    /**
     * Returns @integrationPoint by System name and integration point name
     *
     * @param systemName           name of system, which integration point belongs
     * @param integrationPointName name of integration point
     * @return @ru.sbt.bpm.mock.config.entities.IntegrationPoint
     */
    public IntegrationPoint getIntegrationPointByName(String systemName, String integrationPointName) {
        return getSystemByName(systemName).getIntegrationPoints().getIntegrationPointByName(integrationPointName);
    }
}
