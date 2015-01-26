package ru.sbt.bpm.mock.tests;


import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import ru.sbt.bpm.mock.utils.XmlUtil;
import ru.sbt.bpm.mock.utils.XslTransformer;

import java.util.List;

import static org.junit.Assert.assertEquals;
/**
 * Created by sbt-hodakovskiy-da on 23.01.2015.
 * <p/>
 * Company SBT - Saint-Petersburg
 */
public class XslTransformTestKKMB {

    /** Тест трансформации RQ в RS для точки интеграции CRM с АККМБ "Импорт сделки" */
    @Test
    public void testExportContract() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        checkXSLT(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\CRM\\ExportContractInfo.xsl",
                  dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\CRM\\xml\\ExportContractInfoData.xml",
                  "xml/CRM/ExportContractInfo/rs1.xml");
    }

    protected void checkXSLT (String XSLTFile, String XMLFile, String validateFile) throws Exception{
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        String result = XslTransformer.transform(XSLTFile, XMLFile);
        String validateFileXML = XmlUtil.docAsString(XmlUtil.createXmlMessageFromResource(validateFile).getPayload());

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        Diff diff = new Diff(validateFileXML,result);
        if (!diff.identical()) {
            DetailedDiff detailedDiff = new DetailedDiff(diff);
            List differences = detailedDiff.getAllDifferences();
            for (Object difference : differences) {
                System.out.println("***********************");
                System.out.println(String.valueOf((Difference) difference));
            }
            assertEquals(validateFileXML, result);
        }
    }
}
