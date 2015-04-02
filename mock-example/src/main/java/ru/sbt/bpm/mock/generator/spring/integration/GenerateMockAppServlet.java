package ru.sbt.bpm.mock.generator.spring.integration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.config.entities.*;

import java.io.FileReader;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
// Singleton
// TODO Нужно решить проблему с путем к файлу кофигурации. Все файлы могут лежать в одном месте => путь статическиий и не изменнрый, задается только имя файла. Файл конфигурации может находится, где угодно => путь до этого файла задается полностью пользователем
@ToString
public class GenerateMockAppServlet {

    private static GenerateMockAppServlet INSTANCE = null;

    @Getter
    @Setter
    private String filePath;

    @Getter
    private MockConfig mockConfig;

    // Классы для spring integration context
    // Заголок и основоное тело
    private ContextHeader contextHeader;
    // генерация тегов bean
    private BeanGenerator beanGenerator;
    // генерация inbound & outbound gateway
    private GatewayContextGenerator gatewayContextGenerator;

    private GenerateMockAppServlet(String aFilePath) {
        this.filePath = aFilePath;
    }

    public void init() throws Exception{
        if (this.filePath == null || this.filePath.equals(""))
            throw new Exception();

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
        this.mockConfig = (MockConfig) xStream.fromXML(fileReader);

        // Дальше пишем куски для каждого блока SI context

    }

    public static GenerateMockAppServlet getInstance(String aFilePath) {
        if (INSTANCE == null)
            synchronized (GenerateMockAppServlet.class) {
                if(INSTANCE == null)
                    INSTANCE = new GenerateMockAppServlet(aFilePath);
            }
        return INSTANCE;
    }

    /**
     * Путь к корню
     * @return
     */
    private String getPath() {
        // TODO Это не правильный способ
        return System.getProperty("user.dir");
    }

    /**
     * Получение пути к файлам xml
     * @return
     */
    private String getMockConfigPath() {
        return getPath() + "\\src\\webapp\\web-inf\\mockconfigfiles\\";
    }
}
