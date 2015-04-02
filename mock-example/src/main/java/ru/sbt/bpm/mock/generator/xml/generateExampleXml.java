package ru.sbt.bpm.mock.generator.xml;

import ru.sbt.bpm.mock.generator.localPaths;
import ru.sbt.bpm.mock.spring.utils.SaveFile;
import ru.sbt.bpm.mock.spring.utils.Validator;
import ru.sbt.bpm.mock.spring.utils.Xsl20Transformer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class generateExampleXml {
    private static generateExampleXml ourInstance = new generateExampleXml();

    public static generateExampleXml getInstance() {
        return ourInstance;
    }

    private generateExampleXml() {
    }


    /**
     * создает примеры xml запросов
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param params параметры xsl
     * @throws Exception
     */
    public void createRqExample(String system, String name, String msgType, Map<String, String> params) throws Exception{
        String exampleRq1 = Xsl20Transformer.transform(localPaths.getSrcResorcesPath() + "\\xsl\\" + msgType + "SoapMSG.xsl",
                localPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        Validator.getInstance().validateXML(exampleRq1);

        //TODO backup
        SaveFile.getInstance(localPaths.getPath()).writeStringToFile(new File(localPaths.getExamplesPath() + "\\" +  system + "\\" + name + "\\rq1.xml"), exampleRq1);

        params.put("showOptionalTags", "false");
        if (params.containsKey("tagNameToTakeLinkedTag")) {
            params.put("useLinkedTagValue","true");
        }
        String exampleRq2 = Xsl20Transformer.transform(localPaths.getSrcResorcesPath() + "\\xsl\\" + msgType + "SoapMSG.xsl",
                localPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        Validator.getInstance().validateXML(exampleRq2);

        //TODO backup
        SaveFile.getInstance(localPaths.getPath()).writeStringToFile(new File(localPaths.getExamplesPath() + "\\" +  system + "\\" + name + "\\rq2.xml"), exampleRq2);
    }


    /**
     * создает примеры xml ответов
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param params параметры xsl
     * @throws Exception
     */
    public void createRsExample(String system, String name, String msgType,  Map<String, String> params) throws Exception{
        String exampleRs1 = Xsl20Transformer.transform(localPaths.getSrcResorcesPath() + "\\xsl\\" + msgType + "SoapMSG.xsl",
                localPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        Validator.getInstance().validateXML(exampleRs1);

        //TODO backup
        SaveFile.getInstance(localPaths.getPath()).writeStringToFile(new File(localPaths.getExamplesPath() + "\\" +  system + "\\" + name + "\\rs1.xml"), exampleRs1);

        if (params == null) params = new HashMap<String, String>(1);
        params.put("showOptionalTags", "false");
        String exampleRs2 = Xsl20Transformer.transform(localPaths.getSrcResorcesPath() + "\\xsl\\" + msgType + "SoapMSG.xsl",
                localPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        Validator.getInstance().validateXML(exampleRs2);

        //TODO backup
        SaveFile.getInstance(localPaths.getPath()).writeStringToFile(new File(localPaths.getExamplesPath() + "\\" + system + "\\" + name + "\\rs2.xml"), exampleRs2);
    }
}
