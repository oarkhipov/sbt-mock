package ru.sbt.bpm.mock.generator.xmldata;

import ru.sbt.bpm.mock.generator.LocalPaths;
import ru.sbt.bpm.mock.spring.utils.SaveFile;
import ru.sbt.bpm.mock.spring.utils.Validator;
import ru.sbt.bpm.mock.spring.utils.Xsl20Transformer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class generateDataXml {
    private static generateDataXml ourInstance = new generateDataXml();

    public static generateDataXml getInstance() {
        return ourInstance;
    }

    private generateDataXml() {
    }

    /**
     * создает пример Дата xml из примера ответа и уже существующего файла с даными
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param params параметры xsl
     * @throws Exception
     */
    public void createRsDataXml(String system, String name, Map<String, String> params) throws Exception{
        if (params == null) params = new HashMap<String, String>(2);
        params.put("replace","true");
        String dataXML = Xsl20Transformer.transform(LocalPaths.getSrcResorcesXSLPath() + "\\AddExampleToData.xsl",
                LocalPaths.getExamplesPath() + "\\" + system + "\\" + name + "\\rs1.xml",
                params);
        Validator.getInstance().validateXML(dataXML);

        SaveFile.getInstance(LocalPaths.getPath()).writeStringToFile(new File(LocalPaths.getWebInfPath() + "\\data\\" + system + "\\xml\\" + name + "Data.xml"), dataXML);

        params.put("replace","false");
        params.put("name","test1");
        dataXML = Xsl20Transformer.transform(LocalPaths.getSrcResorcesXSLPath() + "\\AddExampleToData.xsl",
                LocalPaths.getExamplesPath() + "\\" + system + "\\" + name + "\\rs2.xml",
                params);
        Validator.getInstance().validateXML(dataXML);

        SaveFile.getInstance(LocalPaths.getPath()).writeStringToFile(new File(LocalPaths.getWebInfPath() + "\\data\\" + system + "\\xml\\" + name + "Data.xml"), dataXML);
    }



    /**
     * создает пример Дата xml из примера запроса и уже существующего файла с даными
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param params параметры xsl
     * @throws Exception
     */
    public void createRqDataXml(String system, String name, Map<String, String> params) throws Exception{
        if (params == null) params = new HashMap<String, String>(3);
        params.put("replace","true");
        params.put("type","request");
        String dataXML = Xsl20Transformer.transform(LocalPaths.getSrcResorcesXSLPath() + "\\AddExampleToData.xsl",
                LocalPaths.getExamplesPath() + "\\" + system + "\\" + name + "\\rq1.xml",
                params);
        Validator.getInstance().validateXML(dataXML);

        SaveFile.getInstance(LocalPaths.getPath()).writeStringToFile(new File(LocalPaths.getWebInfPath() + "\\data\\" + system + "\\xml\\" + name + "Data.xml"), dataXML);

        params.put("replace","false");
        params.put("name","test1");
        params.put("type","request");
        dataXML = Xsl20Transformer.transform(LocalPaths.getSrcResorcesXSLPath() + "\\AddExampleToData.xsl",
                LocalPaths.getExamplesPath() + "\\" + system + "\\" + name + "\\rq2.xml",
                params);
        Validator.getInstance().validateXML(dataXML);

        SaveFile.getInstance(LocalPaths.getPath()).writeStringToFile(new File(LocalPaths.getWebInfPath() + "\\data\\" + system + "\\xml\\" + name + "Data.xml"), dataXML);
    }


}
