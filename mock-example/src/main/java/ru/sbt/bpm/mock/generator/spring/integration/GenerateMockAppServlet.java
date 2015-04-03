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

    @Getter
    private MockConfig mockConfig;

    // Классы для spring integration context
    // Заголок и основоное тело
    private ContextHeader contextHeader;
    // генерация тегов bean
    private BeanGenerator beanGenerator;
    // генерация inbound & outbound gateway
    private GatewayContextGenerator gatewayContextGenerator;

}
