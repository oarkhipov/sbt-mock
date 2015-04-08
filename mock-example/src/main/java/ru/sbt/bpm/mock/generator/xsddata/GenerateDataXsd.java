package ru.sbt.bpm.mock.generator.xsddata;

import ru.sbt.bpm.mock.generator.LocalPaths;
import ru.sbt.bpm.mock.generator.util.SimpleValidator;
import ru.sbt.bpm.mock.spring.utils.SaveFile;
import ru.sbt.bpm.mock.spring.utils.Xsl20Transformer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class GenerateDataXsd {
    private static GenerateDataXsd ourInstance = new GenerateDataXsd();

    public static GenerateDataXsd getInstance() {
        return ourInstance;
    }

    private GenerateDataXsd() {
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
        if (params == null) params = new HashMap<String, String>();
        if (type.equals("Request")) params.put("msgType", "request");
        if (type.equals("Response")) params.put("msgType", "response");
        String xsdXml = Xsl20Transformer.transform(LocalPaths.getSrcResorcesXSLPath() + "\\xsdToDataXsd.xsl",
                LocalPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        SimpleValidator.getInstance().validateXML(xsdXml);

        SaveFile.getInstance(LocalPaths.getPath()).writeStringToFile(new File(LocalPaths.getWebInfPath() + "\\data\\" + system + "\\xsd\\" + name + type +"Data.xsd"), xsdXml);
    }
}
