package ru.sbt.bpm.mock.generator.xsl;

import ru.sbt.bpm.mock.generator.LocalPaths;
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
        String xsltXml = Xsl20Transformer.transform(LocalPaths.getSrcResorcesXSLPath() + "\\responceXSDtoXSL.xsl",
                LocalPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        Validator.getInstance().validateXML(xsltXml);

        File xslFile = new File(LocalPaths.getWebInfPath() + "\\xsl\\" + system + "\\" + name + ".xsl");

        SaveFile.getInstance(LocalPaths.getPath()).writeStringToFile(xslFile, xsltXml);
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
        String xsltXml = Xsl20Transformer.transform(LocalPaths.getSrcResorcesXSLPath() + "\\requestXSDtoXSL.xsl",
                LocalPaths.getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        Validator.getInstance().validateXML(xsltXml);

        SaveFile.getInstance(LocalPaths.getPath()).writeStringToFile(new File(LocalPaths.getWebInfPath() + "\\xsl\\" + system + "\\" + name + ".xsl"), xsltXml);
    }


}
