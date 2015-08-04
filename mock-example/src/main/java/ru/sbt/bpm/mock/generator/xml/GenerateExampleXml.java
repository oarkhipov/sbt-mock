package ru.sbt.bpm.mock.generator.xml;

import ru.sbt.bpm.mock.generator.LocalPaths;
import ru.sbt.bpm.mock.spring.utils.SaveFile;
import ru.sbt.bpm.mock.generator.util.SimpleValidator;
import ru.sbt.bpm.mock.spring.utils.Xsl20Transformer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class GenerateExampleXml {
    private static GenerateExampleXml ourInstance = new GenerateExampleXml();

    public static GenerateExampleXml getInstance() {
        return ourInstance;
    }

    private GenerateExampleXml() {
    }


    /**
     * создает примеры xml запросов
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param params параметры xsl
     * @throws Exception
     */
    public void createRqExample(String system, String name, String msgType, Map<String, String> params) throws Exception{
        String exampleRq1 = Xsl20Transformer.transform(LocalPaths.getSrcResorcesXSLPath() + File.separator + "MSGTemplates" + File.separator + msgType + "SoapMSG.xsl", //TODO вынести этот путь в переменную
                LocalPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        SimpleValidator.getInstance().validateXML(exampleRq1);

        SaveFile.getInstance(LocalPaths.getPath()).writeStringToFile(new File(LocalPaths.getExamplesPath() + "\\" +  system + "\\" + name + "\\rq1.xml"), exampleRq1);

        params.put("showOptionalTags", "false");
        if (params.containsKey("tagNameToTakeLinkedTag")) {
            params.put("useLinkedTagValue","true");
        }
        String exampleRq2 = Xsl20Transformer.transform(LocalPaths.getSrcResorcesXSLPath() + File.separator + "MSGTemplates"  + File.separator + msgType + "SoapMSG.xsl", //TODO вынести этот путь в переменную
                LocalPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        SimpleValidator.getInstance().validateXML(exampleRq2);

        SaveFile.getInstance(LocalPaths.getPath()).writeStringToFile(new File(LocalPaths.getExamplesPath() + "\\" +  system + "\\" + name + "\\rq2.xml"), exampleRq2);
    }


    /**
     * создает примеры xml ответов
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param params параметры xsl
     * @throws Exception
     */
    public void createRsExample(String system, String name, String msgType,  Map<String, String> params) throws Exception{
        String exampleRs1 = Xsl20Transformer.transform(LocalPaths.getSrcResorcesXSLPath() + File.separator + "MSGTemplates"  + File.separator + msgType + "SoapMSG.xsl", //TODO вынести этот путь в переменную
                LocalPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        SimpleValidator.getInstance().validateXML(exampleRs1);

        SaveFile.getInstance(LocalPaths.getPath()).writeStringToFile(new File(LocalPaths.getExamplesPath() + "\\" +  system + "\\" + name + "\\rs1.xml"), exampleRs1);

        if (params == null) params = new HashMap<String, String>(1);
        params.put("showOptionalTags", "false");
        String exampleRs2 = Xsl20Transformer.transform(LocalPaths.getSrcResorcesXSLPath() + File.separator + "MSGTemplates"  + File.separator + msgType + "SoapMSG.xsl", //TODO вынести этот путь в переменную
                LocalPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        SimpleValidator.getInstance().validateXML(exampleRs2);

        SaveFile.getInstance(LocalPaths.getPath()).writeStringToFile(new File(LocalPaths.getExamplesPath() + "\\" + system + "\\" + name + "\\rs2.xml"), exampleRs2);
    }
}
