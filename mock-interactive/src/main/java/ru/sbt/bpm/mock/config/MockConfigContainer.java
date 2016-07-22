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
import java.util.*;

/**
 * Created by sbt-bochev-as on 02.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@NoArgsConstructor
@Component
public class MockConfigContainer {

    private static MockConfigContainer INSTANCE = null;
    @Autowired
    ApplicationContext applicationContext;
    @Getter
    private String basePath;
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
     * Получение экземпляра объекта синглтона средствами core Java
     *
     * @param filePath путь до файла конфига
     */
    public static MockConfigContainer getInstance(String filePath) {
        if (INSTANCE == null)
            synchronized (MockConfigContainer.class) {
                if (INSTANCE == null)
                    INSTANCE = new MockConfigContainer(filePath);
            }
        return INSTANCE;
    }

    /**
     * десерализация файла в объект конфига при инициализации
     *
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
        if (this.filePath == null || this.filePath.isEmpty())
            throw new IOException("no config file [" + filePath + "]!");

        File resourceFile;
        if (applicationContext == null) {
            resourceFile = new File(filePath);
        } else {
            basePath = applicationContext.getResource("").getFile().getAbsolutePath();
            resourceFile = applicationContext.getResource(filePath).getFile();
        }


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
        sortConfig();

    }

    public void sortConfig() {
        List<System> systems = config.getSystems().getSystems();
        if (systems != null) {
            Collections.sort(systems, new SystemComparator());
            for (System system : systems) {
                List<IntegrationPoint> integrationPoints = system.getIntegrationPoints().getIntegrationPoints();
                if (integrationPoints != null) {
                    Collections.sort(integrationPoints, new IntegrationPointComparator());
                }
            }
        }
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

    public void reInit() throws IOException {
        init();
    }

    class SystemComparator implements Comparator<System> {
        @Override
        public int compare(System o1, System o2) {
            return (o1.getProtocol().name()+o1.getSystemName()).compareTo(o2.getProtocol().name()+o2.getSystemName());
        }
    }

    class IntegrationPointComparator implements Comparator<IntegrationPoint> {
        @Override
        public int compare(IntegrationPoint o1, IntegrationPoint o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
