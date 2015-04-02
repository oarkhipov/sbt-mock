package ru.sbt.bpm.mock.generator.xsddata;

import ru.sbt.bpm.mock.generator.LocalPaths;
import ru.sbt.bpm.mock.spring.utils.SaveFile;
import ru.sbt.bpm.mock.spring.utils.Validator;
import ru.sbt.bpm.mock.spring.utils.Xsl20Transformer;

import java.io.File;
import java.util.Map;

/**
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class generateDataXsd {
    private static generateDataXsd ourInstance = new generateDataXsd();

    public static generateDataXsd getInstance() {
        return ourInstance;
    }

    private generateDataXsd() {
    }

    /**
     * создает XSD для данных из XSD
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param type тип (Response/Request)
     * @param params параметры xsl
     * @throws Exception
     */
    public void createDataXSD(String system, String name, String type, Map<String, String> params) throws Exception{
        String xsdXml = Xsl20Transformer.transform(LocalPaths.getSrcResorcesXSLPath() + "\\xsdToDataXsd.xsl",
                LocalPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        Validator.getInstance().validateXML(xsdXml);

        SaveFile.getInstance(LocalPaths.getPath()).writeStringToFile(new File(LocalPaths.getWebInfPath() + "\\data\\" + system + "\\xsd\\" + name + type +"Data.xsd"), xsdXml);
    }
}
