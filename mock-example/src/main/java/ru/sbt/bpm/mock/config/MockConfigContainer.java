package ru.sbt.bpm.mock.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.sbt.bpm.mock.config.entities.*;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by sbt-bochev-as on 02.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@Component
public class MockConfigContainer {

    @Getter
    private MockConfig config;

    @Getter
    private String filePath = null;


    /**
     * Создание экземпляра объекта из метода getInstance (CoreJava) или из бина с вызовом конструктора (Java EE)
     *
     * @param filePath путь до конфига
     */
    private MockConfigContainer(String filePath) {
        this.filePath = filePath;
        config = new MockConfig();
    }

    /**
     * десерализация файла в объект конфига
     *
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException{
        if (this.filePath == null || this.filePath.equals(""))
            throw new IOException("no config file!");

        FileReader fileReader = new FileReader(filePath);
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
        //TODO Не работает, хотя должен
//        xStream.autodetectAnnotations(true);
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
}
