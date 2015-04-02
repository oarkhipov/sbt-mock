package ru.sbt.bpm.mock.generator.xsl;

import ru.sbt.bpm.mock.generator.localPaths;
import ru.sbt.bpm.mock.spring.utils.SaveFile;
import ru.sbt.bpm.mock.spring.utils.Validator;
import ru.sbt.bpm.mock.spring.utils.Xsl20Transformer;

import java.io.File;
import java.util.Map;

/**
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class generateXsl {
    private static generateXsl ourInstance = new generateXsl();

    public static generateXsl getInstance() {
        return ourInstance;
    }

    private generateXsl() {
    }


    /**
     * создает XSL из XSD ля mock сервиса
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param params параметры xsl
     * @throws Exception
     */
    public File createMockXSL(String system, String name, Map<String, String> params) throws Exception{
        String xsltXml = Xsl20Transformer.transform(localPaths.getSrcResorcesPath() + "\\xsl\\responceXSDtoXSL.xsl",
                localPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        Validator.getInstance().validateXML(xsltXml);

        File xslFile = new File(localPaths.getWebInfPath() + "\\xsl\\" + system + "\\" + name + ".xsl");
        //TODO backup
        SaveFile.getInstance(localPaths.getPath()).writeStringToFile(xslFile, xsltXml);
        return xslFile;
    }



    /**
     * создает XSL из XSD ля mock драйвера
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param params параметры xsl
     * @throws Exception
     */
    public void createDriverXSL(String system, String name, Map<String, String> params) throws Exception{
        String xsltXml = Xsl20Transformer.transform(localPaths.getSrcResorcesPath() + "\\xsl\\requestXSDtoXSL.xsl",
                localPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        Validator.getInstance().validateXML(xsltXml);

        //TODO backup
        SaveFile.getInstance(localPaths.getPath()).writeStringToFile(new File(localPaths.getWebInfPath() + "\\xsl\\" + system + "\\" + name + ".xsl"), xsltXml);
    }


}
