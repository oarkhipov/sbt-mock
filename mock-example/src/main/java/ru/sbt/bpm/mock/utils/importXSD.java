package ru.sbt.bpm.mock.utils;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.springframework.core.io.Resource;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для импорта новой xsd и замены его на старые
 * Created by sbt-vostrikov-mi on 14.01.2015.
 */
public class importXSD {


    private Validator validator;

    /**
     * путь к корню
     * @return
     */
    private String getPath() {
        //TODO это не совсем правильный способ получения пути.
        return this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..";
    }

    /**
     * путь к папке WEB-INF
     * @return
     */
    private String getWebInfPath() {
        return getPath() + "\\src\\main\\webapp\\WEB-INF";
    }

    /**
     * путь к папке с примерами xml для тестов
     * @return
     */
    private String getExamplesPath() {
        return getPath() + "\\src\\test\\resources\\xml";
    }

    /**
     * создает примеры xml запросов
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param Params параметры xsl
     * @throws Exception
     */
    private void createRqExample(String system, String name, Map<String, String> Params) throws Exception{
        String exampleRq1 = useXSLT(getWebInfPath() + "\\xsl\\util\\XSDToExampleXML.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + name + "Request.xsd",
                Params);
        //validateXML(exampleRq1);
        //TODO тщательно проверить выход

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getExamplesPath() + "\\" +  system + "\\" + name + "\\rq1.xml"), exampleRq1);

        if (Params == null) Params = new HashMap<String, String>(2);
        Params.put("showOptionalTags", "false");
        String exampleRq2 = useXSLT(getWebInfPath() + "\\xsl\\util\\XSDToExampleXML.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + name + "Request.xsd",
                Params);
        //validateXML(exampleRq1);
        //TODO тщательно проверить выход

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getExamplesPath() + "\\" +  system + "\\" + name + "\\rq2.xml"), exampleRq2);
    }

    /**
     * создает примеры xml ответов
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param Params параметры xsl
     * @throws Exception
     */
    private void createRsExample(String system, String name, Map<String, String> Params) throws Exception{
        String exampleRs1 = useXSLT(getWebInfPath() + "\\xsl\\util\\XSDToExampleXML.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + name + "Response.xsd",
                Params);
        //validateXML(exampleRq1);
        //TODO тщательно проверить выход

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getExamplesPath() + "\\" +  system + "\\" + name + "\\rs1.xml"), exampleRs1);

        if (Params == null) Params = new HashMap<String, String>(2);
        Params.put("showOptionalTags", "false");
        String exampleRs2 = useXSLT(getWebInfPath() + "\\xsl\\util\\XSDToExampleXML.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + name + "Response.xsd",
                Params);
        //validateXML(exampleRq1);
        //TODO тщательно проверить выход

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getExamplesPath() + "\\" +  system + "\\" + name + "\\rs2.xml"), exampleRs2);
    }

    /**
     * создает XSD для данных из XSD
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param type тип (Response/Request)
     * @param Params параметры xsl
     * @throws Exception
     */
    private void createDataXSD(String system, String name, String type, Map<String, String> Params) throws Exception{
        String xsdXml = useXSLT(getWebInfPath() + "\\xsl\\util\\xsdToDataXsd.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + name + type +".xsd",
                Params);
        //validateXML(exampleRq1);
        //TODO тщательно проверить выход

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getWebInfPath() + "\\data\\" + system + "\\xsd\\" + name + type +"Data.xsd"), xsdXml);
    }

    /**
     * создает XSL из XSD ля mock сервиса
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param Params параметры xsl
     * @throws Exception
     */
    private void createMockXSL(String system, String name, Map<String, String> Params) throws Exception{
        String xsltXml = useXSLT(getWebInfPath() + "\\xsl\\util\\responceXSDtoXSL.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + name + "Response.xsd",
                Params);
        //validateXML(exampleRq1);
        //TODO тщательно проверить выход

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getWebInfPath() + "\\xsl\\" + system + "\\" + name + ".xsl"), xsltXml);
    }

    /**
     * создает XSL из XSD ля mock драйвера
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param Params параметры xsl
     * @throws Exception
     */
    private void createDriverXSL(String system, String name, Map<String, String> Params) throws Exception{
        String xsltXml = useXSLT(getWebInfPath() + "\\xsl\\util\\requestXSDtoXSL.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + name + "Request.xsd",
                Params);
        //validateXML(exampleRq1);
        //TODO тщательно проверить выход

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getWebInfPath() + "\\xsl\\" + system + "\\" + name + ".xsl"), xsltXml);
    }

    /**
     * создает пример Дата xml из примера ответа и уже существующего файла с даными
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param Params параметры xsl
     * @throws Exception
     */
    private void createRsDataXml(String system, String name, Map<String, String> Params) throws Exception{
        if (Params == null) Params = new HashMap<String, String>(2);
        Params.put("dataFileName","../../data/" +  system + "/xml/" + name + "Data.xml");
        Params.put("replace","true");
        String dataXML = useXSLT(getWebInfPath() + "\\xsl\\util\\AddExampleToData.xsl",
                getExamplesPath() + "\\" +  system + "\\" + name + "\\rs1.xml",
                Params);
        //TODO тщательно проверить выход
        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getWebInfPath() + "\\data\\" + system + "\\xml\\" + name + "Data.xml"), dataXML);

        Params.put("replace","false");
        Params.put("name","test1");
        dataXML = useXSLT(getWebInfPath() + "\\xsl\\util\\AddExampleToData.xsl",
                getExamplesPath() + "\\" +  system + "\\" + name + "\\rs2.xml",
                Params);
        //validateXML(exampleRq1);
        //TODO тщательно проверить выход

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getWebInfPath() + "\\data\\" + system + "\\xml\\" + name + "Data.xml"), dataXML);
    }

    /**
     * создает пример Дата xml из примера запроса и уже существующего файла с даными
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param Params параметры xsl
     * @throws Exception
     */
    private void createRqDataXml(String system, String name, Map<String, String> Params) throws Exception{
        if (Params == null) Params = new HashMap<String, String>(3);
        Params.put("dataFileName","../../data/" +  system + "/xml/" + name + "Data.xml");
        Params.put("replace","true");
        Params.put("type","request");
        String dataXML = useXSLT(getWebInfPath() + "\\xsl\\util\\AddExampleToData.xsl",
                getExamplesPath() + "\\" +  system + "\\" + name + "\\rq1.xml",
                Params);
        //validateXML(exampleRq1);
        //TODO тщательно проверить выход

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getWebInfPath() + "\\data\\" + system + "\\xml\\" + name + "Data.xml"), dataXML);
    }

    /**
     * полный цикл создания mock-сервиса
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param Params параметры xsl
     */
    public void mockCycle(String system, String name, Map<String, String> Params) {
        try
        {
            Map<String, String> altParams = null;
            if (Params!=null) {
                //иногда имя главного тэга не ясно, и надо задавать его как параметр entryPointName.
                //Также возможен случай, когда это значения надо задавать как и для запроса так и для ответа
                //в этом случае задается параметр RqEntryPointName - для создания ответа его значение передается как entryPointName
                altParams = new HashMap<String, String>(Params);
                if (Params.containsKey("entryPointName")) {
                    altParams.put("operation-name", Params.get("entryPointName"));
                }
                if (Params.containsKey("RqEntryPointName")) {
                    altParams.put("entryPointName", Params.get("RqEntryPointName"));
                }
            } else
            {
                altParams = new HashMap<String, String>(1);
            }

            //не вставляем в этветы комменты с обозначением сколько элементов доступно
            altParams.put("omitComments", "true");

            createRqExample(system, name, altParams);
            createRsExample(system, name, Params);
            createDataXSD(system, name, "Response", Params);
            createMockXSL(system, name, Params);
            createRsDataXml(system, name, Params);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * полный цикл создания драйвера
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param Params параметры xsl
     */
    public void driverCycle(String system, String name, Map<String, String> Params) {
        try
        {
            createRqExample(system, name, Params);
            createDataXSD(system, name, "Request", Params);
            createDriverXSL(system, name, Params);
            createRqDataXml(system, name, Params);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * точка входа для запуска вне сервиса
     * @throws Exception
     */
    public static void main(String [] args) throws Exception {
        importXSD instance = new importXSD();
        instance.initValidator(new File(instance.getWebInfPath() + "\\xsd"));

        instance.renewData();
    }

    public void renewData() {
        Map<String, String> params = null;
        params = new HashMap<String, String>();


        driverCycle("CRM", "ForceSignal", null);
        driverCycle("CRM", "UpdateDeal", null);

        params.clear();
        params.put("entryPointName", "CreateTaskRs");
        params.put("RqEntryPointName","CreateTaskRq");
        mockCycle("CRM", "CreateTask", params);

        params.clear();
        params.put("entryPointName","PrtspRs");
        params.put("RqEntryPointName","PrtspRq");
        params.put("dataFileName","GetParticipantsData.xml");
        mockCycle("CRM", "GetParticipants", params);

        params.clear();
        params.put("entryPointName", "SaveDealRs");
        params.put("RqEntryPointName","SaveDealRq");
        mockCycle("CRM", "SaveDeal", params);

        params.clear();
        params.put("entryPointName", "UpdateRefRs");
        params.put("RqEntryPointName","UpdateRefRq");
        mockCycle("CRM", "UpdateRef", params);

        params.clear();
        params.put("entryPointName","DebtCapacityCalculationResponse");
        params.put("RqEntryPointName","DebtCapacityCalculationRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentNS", "http://sbrf.ru/NCP/ASFO/");
        params.put("dataFileName", "CalculateDebtCapacityData.xml");
        mockCycle("AMRLiRT", "CalculateDebtCapacity", params);

        params.clear();
        params.put("entryPointName", "CalcRatingResponse");
        params.put("RqEntryPointName","CalcRatingRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        params.put("dataFileName","CalculateRatingData.xml");
        mockCycle("AMRLiRT", "CalculateRating", params);

        params.clear();
        params.put("entryPointName", "ConfirmResponse");
        params.put("RqEntryPointName","ConfirmRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        params.put("dataFileName","ConfirmRatingData.xml");
        mockCycle("AMRLiRT", "ConfirmRating", params);

        params.clear();
        params.put("entryPointName", "CorrectResponse");
        params.put("RqEntryPointName","CorrectRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        params.put("dataFileName","CorrectRatingData.xml");
        mockCycle("AMRLiRT", "CorrectRating", params);

        params.clear();
        params.put("entryPointName", "LgdFinalizationResponse");
        params.put("RqEntryPointName","LgdFinalizationRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        params.put("dataFileName","FinalizeLGDData.xml");
        mockCycle("AMRLiRT", "FinalizeLGD", params);

        params.clear();
        params.put("entryPointName","LgdCalculationResponse");
        params.put("RqEntryPointName","LgdCalculationRequest");
        params.put("systemName","AMRLiRT");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        params.put("dataFileName","CalculateLGDData.xml");
        mockCycle("AMRLiRT", "CalculateLGD", params);

        params.clear();
        params.put("entryPointName","FinAnalysisImportResponse");
        params.put("RqEntryPointName","FinAnalysisImportRequest");
        params.put("systemName","FinRep");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        mockCycle("FinRep", "FinAnalysisImport", params);

        params.clear();
        params.put("entryPointName","FinReportImportResponse");
        params.put("RqEntryPointName","FinReportImportRequest");
        params.put("systemName","FinRep");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        mockCycle("FinRep", "FinReportImport", params);

        params.clear();
        params.put("entryPointName","ImportRatingResponse");
        params.put("RqEntryPointName","ImportRatingRequest");
        params.put("systemName","FinRep");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        mockCycle("FinRep", "ImportRating", params);

        params.clear();
        params.put("entryPointName","UpdateRatingResponse");
        params.put("RqEntryPointName","UpdateRatingRequest");
        params.put("systemName","FinRep");
        params.put("parrentNS","http://sbrf.ru/NCP/ASFO/");
        mockCycle("FinRep", "UpdateRating", params);
    }

    /**
     * применение XSLTFile над XMLFile с параметрами params
     * @param XSLTFile xslt
     * @param XMLFile входная xml
     * @param params параметры
     * @return текст исходящей xml
     * @throws Exception
     */
    protected String useXSLT (String XSLTFile, String XMLFile, Map<String,String> params ) throws Exception {

        String result = Xsl20Transformer.transform(XSLTFile, XMLFile, params);

        return result;
    }

    /**
     * применение XSLTFile над xml в строке с параметрами params
     * @param XSLTFile xslt
     * @param xml строка с xml
     * @param params параметры
     * @return текст исходящей xml
     * @throws Exception
     */
    protected String useXSLTonString (String XSLTFile, String xml, Map<String,String> params ) throws Exception {

        Source xslt = new StreamSource(new File(XSLTFile));
        Source xmlSource = new StreamSource(new StringReader(xml));

        String result = Xsl20Transformer.transform(xslt, xmlSource, params);
        return result;
    }

    /**
     * инициализация валидатора
     * @param dir путь к xsd
     */
    private void initValidator(File dir) throws Exception {

        ArrayList<File> xsdFiles = new ArrayList<File>();
        searchFiles(dir, xsdFiles, ".xsd");

        SchemaFactory factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        StreamSource sources[] = new StreamSource[xsdFiles.size()];

//            Add Xsd files to source
        for (int i = 0; i < xsdFiles.size(); i++) {
            sources[i] = new StreamSource(xsdFiles.get(i));
        }
        Schema schema = factory.newSchema(sources);
        validator = schema.newValidator();
    }

    /**
     * поиск файлов по маске
     * @param rootDir дирректория
     * @param files исходящий массив
     * @param ext разрешение
     */
    private void searchFiles(File rootDir, ArrayList<File> files, String ext) throws Exception {
        File[] listFiles = rootDir.listFiles();
        if (listFiles == null) throw new FileNotFoundException(rootDir.getAbsolutePath().toString() + " is empty");
        for (File file : listFiles) {
            if(file.isDirectory()) {
                searchFiles(file, files, ext);
            }
            else if(file.getName().toLowerCase().endsWith(ext)){
                files.add(file);
            }
        }
    }


    public boolean validateXML(String xml) throws Exception {
        if (validator == null) throw new NullPointerException();
        Source xmlReader = new StreamSource(new StringReader(xml));
        validator.validate(xmlReader);
        return true;
    }
}
