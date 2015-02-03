package ru.sbt.bpm.mock.config.generator.util;

import ru.sbt.bpm.mock.utils.SaveFile;
import ru.sbt.bpm.mock.utils.Xsl20Transformer;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
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
        return System.getProperty("user.dir");
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
     * @param params параметры xsl
     * @throws Exception
     *
     */
    private void createRqExample(String system, String name, String msgType, Map<String, String> params) throws Exception{
        String exampleRq1 = useXSLT(getWebInfPath() + "\\xsl\\util\\"+msgType+"SoapMSG.xsl", //TODO выбор другого KD4SoapMsg.xsl
                getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        validateXML(exampleRq1);

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getExamplesPath() + "\\" +  system + "\\" + name + "\\rq1.xml"), exampleRq1);

        params.put("showOptionalTags", "false");
        if (params.containsKey("tagNameToTakeLinkedTag")) {
            params.put("useLinkedTagValue","true");
        }
        String exampleRq2 = useXSLT(getWebInfPath() + "\\xsl\\util\\"+msgType+"SoapMSG.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        validateXML(exampleRq2);

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
    private void createRsExample(String system, String name, Map<String, String> params) throws Exception{
        String exampleRs1 = useXSLT(getWebInfPath() + "\\xsl\\util\\NCPSoapMSG.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        validateXML(exampleRs1);

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getExamplesPath() + "\\" +  system + "\\" + name + "\\rs1.xml"), exampleRs1);

        if (params == null) params = new HashMap<String, String>(1);
        params.put("showOptionalTags", "false");
        String exampleRs2 = useXSLT(getWebInfPath() + "\\xsl\\util\\NCPSoapMSG.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        validateXML(exampleRs2);

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getExamplesPath() + "\\" + system + "\\" + name + "\\rs2.xml"), exampleRs2);
    }

    /**
     * создает XSD для данных из XSD
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param type тип (Response/Request)
     * @param params параметры xsl
     * @throws Exception
     */
    private void createDataXSD(String system, String name, String type, Map<String, String> params) throws Exception{
        String xsdXml = useXSLT(getWebInfPath() + "\\xsl\\util\\xsdToDataXsd.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        validateXML(xsdXml);

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
    private void createMockXSL(String system, String name, Map<String, String> params) throws Exception{
        String xsltXml = useXSLT(getWebInfPath() + "\\xsl\\util\\responceXSDtoXSL.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        validateXML(xsltXml);

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getWebInfPath() + "\\xsl\\" + system + "\\" + name + ".xsl"), xsltXml);
    }

    /**
     * создает XSL из XSD ля mock драйвера
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param params параметры xsl
     * @throws Exception
     */
    private void createDriverXSL(String system, String name, Map<String, String> params) throws Exception{
        String xsltXml = useXSLT(getWebInfPath() + "\\xsl\\util\\requestXSDtoXSL.xsl",
                getWebInfPath() + "\\xsd\\" + system + "\\" + params.get("xsdBase"),
                params);
        validateXML(xsltXml);

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
    private void createRsDataXml(String system, String name, Map<String, String> params) throws Exception{
        if (params == null) params = new HashMap<String, String>(2);
        params.put("replace","true");
        String dataXML = useXSLT(getWebInfPath() + "\\xsl\\util\\AddExampleToData.xsl",
                getExamplesPath() + "\\" +  system + "\\" + name + "\\rs1.xml",
                params);
        validateXML(dataXML);
        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getWebInfPath() + "\\data\\" + system + "\\xml\\" + name + "Data.xml"), dataXML);

        params.put("replace","false");
        params.put("name","test1");
        dataXML = useXSLT(getWebInfPath() + "\\xsl\\util\\AddExampleToData.xsl",
                getExamplesPath() + "\\" +  system + "\\" + name + "\\rs2.xml",
                params);
        validateXML(dataXML);

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
    private void createRqDataXml(String system, String name, Map<String, String> params) throws Exception{
        if (params == null) params = new HashMap<String, String>(3);
        params.put("replace","true");
        params.put("type","request");
        String dataXML = useXSLT(getWebInfPath() + "\\xsl\\util\\AddExampleToData.xsl",
                getExamplesPath() + "\\" +  system + "\\" + name + "\\rq1.xml",
                params);
        validateXML(dataXML);
        //TODO backup

        SaveFile.getInstance(getPath()).writeStringToFile(new File(getWebInfPath() + "\\data\\" + system + "\\xml\\" + name + "Data.xml"), dataXML);

        params.put("replace","false");
        params.put("name","test1");
        params.put("type","request");
        dataXML = useXSLT(getWebInfPath() + "\\xsl\\util\\AddExampleToData.xsl",
                getExamplesPath() + "\\" +  system + "\\" + name + "\\rq2.xml",
                params);
        validateXML(dataXML);

        //TODO backup
        SaveFile.getInstance(getPath()).writeStringToFile(new File(getWebInfPath() + "\\data\\" + system + "\\xml\\" + name + "Data.xml"), dataXML);
    }

    /**
     * полный цикл создания mock-сервиса
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param params параметры xsl
     */
    public void mockCycle(String system, String name, String msgType, Map<String, String> params) {
        try
        {
            if (params==null) {
                params = new HashMap<String, String>(1);
            }
            if (!params.containsKey("operationsXSD")) {
                params.put("operationsXSD", "../../xsd/"+system+"/"+name+"Response.xsd");
            }
            if (!params.containsKey("dataFileName")) {
                params.put("dataFileName", name + "Data.xml");
            }
            if (!params.containsKey("system")) {
                params.put("system", system);
            }
            Map <String, String> altParams = new HashMap<String, String>(params);
            altParams.put("rootElementName", params.get("RqRootElementName"));
            altParams.put("operationsXSD", "../../xsd/"+system+"/"+name+"Request.xsd");
            altParams.put("operation-name", params.get("rootElementName"));

            //не вставляем в этветы комменты с обозначением сколько элементов доступно
            params.put("omitComments", "true");

            createRqExample(system, name, msgType, altParams);
            createRsExample(system, name, params);
            createDataXSD(system, name, "Response", params);
            createMockXSL(system, name, params);
            createRsDataXml(system, name, params);
            System.out.println(system + " " + name + " mock Done");
        } catch (Exception e) {
            System.out.println(system + " " + name + " mock Failed");
            e.printStackTrace();

        }
    }

    /**
     * полный цикл создания драйвера
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param params параметры xsl
     */
    public void driverCycle(String system, String name, String msgType, Map<String, String> params) {
        try
        {
            if (params==null) {
                params = new HashMap<String, String>(1);
            }
            if (!params.containsKey("dataFileName")) {
                params.put("dataFileName", name + "Data.xml");
            }
            if (!params.containsKey("operationsXSD")) {
                params.put("operationsXSD", "../../xsd/"+system+"/"+name+"Request.xsd");
            }
            if (!params.containsKey("system")) {
                params.put("system", system);
            }

            createRqExample(system, name, msgType, params);
            createDataXSD(system, name, "Request", params);
            createDriverXSL(system, name, params);
            createRqDataXml(system, name, params);
            System.out.println(system + " " + name + " driver Done");
        } catch (Exception e) {
            System.out.println(system + " " + name + " driver Failed");
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

        instance.renewDataBPM();
        instance.renewDataBBMO();
    }

    public void renewDataBPM() {
        Map<String, String> params = null;
        params = new HashMap<String, String>();

//TODO засунуть все в xml конфиг

        params.clear();
        params.put("rootElementName", "forceSignalRq");
        params.put("xsdBase","CRM.xsd");
        driverCycle("CRM", "ForceSignal", "NCP", params);

        params.clear();
        params.put("rootElementName", "updateDealRq");
        params.put("xsdBase","CRM.xsd");
        driverCycle("CRM", "UpdateDeal", "NCP", params);

        params.clear();
        params.put("tagNameToTakeLinkedTag", "comment");
        params.put("rootElementName", "createTaskRs");
        params.put("RqRootElementName", "createTaskRq");
        params.put("xsdBase","CRM.xsd");
        mockCycle("CRM", "CreateTask", "NCP", params);

        params.clear();
        params.put("dataFileName", "GetParticipantsData.xml");
        params.put("tagNameToTakeLinkedTag","performer");
        params.put("rootElementName", "prtspRs");
        params.put("RqRootElementName", "prtspRq");
        params.put("xsdBase","CRM.xsd");
        mockCycle("CRM", "GetParticipants", "NCP", params);

        params.clear();
        params.put("tagNameToTakeLinkedTag", "dealType");
        params.put("rootElementName", "saveDealRs");
        params.put("RqRootElementName", "saveDealRq");
        params.put("xsdBase","CRM.xsd");
        mockCycle("CRM", "SaveDeal", "NCP",params);

        params.clear();
        params.put("tagNameToTakeLinkedTag", "referenceItem");
        params.put("rootElementName", "updateRefRs");
        params.put("RqRootElementName", "updateRefRq");
        params.put("xsdBase","CRM.xsd");
        mockCycle("CRM", "UpdateRef", "NCP",params);

        params.clear();
        params.put("systemName", "AMRLiRT");
        params.put("parrentXSDPath","../../xsd/AMRLiRT/AMRLIRT.xsd");
        params.put("dataFileName", "CalculateDebtCapacityData.xml");
        params.put("tagNameToTakeLinkedTag","model");
        params.put("rootElementName", "calculateDCRs");
        params.put("RqRootElementName", "calculateDCRq");
        params.put("xsdBase","AMRLiRT.xsd");
        mockCycle("AMRLiRT", "CalculateDebtCapacity", "NCP", params);

        params.clear();
        params.put("systemName", "AMRLiRT");
        params.put("dataFileName","CalculateRatingData.xml");
        params.put("tagNameToTakeLinkedTag","model");
        params.put("rootElementName", "calculateRatingRs");
        params.put("RqRootElementName", "calculateRatingRq");
        params.put("xsdBase","AMRLiRT.xsd");
        mockCycle("AMRLiRT", "CalculateRating", "NCP", params);

        params.clear();
        params.put("systemName", "AMRLiRT");
        params.put("dataFileName","ConfirmRatingData.xml");
        params.put("tagNameToTakeLinkedTag","siebelMessage");
        params.put("rootElementName", "confirmRatingRs");
        params.put("RqRootElementName", "confirmRatingRq");
        params.put("xsdBase","AMRLiRT.xsd");
        mockCycle("AMRLiRT", "ConfirmRating", "NCP", params);

        params.clear();
        params.put("systemName", "AMRLiRT");
        params.put("parrentXSDPath","../../xsd/AMRLiRT/AMRLIRT.xsd");
        params.put("dataFileName","CorrectRatingData.xml");
        params.put("tagNameToTakeLinkedTag","siebelMessage");
        params.put("rootElementName", "correctRatingRs");
        params.put("RqRootElementName", "correctRatingRq");
        params.put("xsdBase","AMRLiRT.xsd");
        mockCycle("AMRLiRT", "CorrectRating", "NCP", params);

        params.clear();
        params.put("systemName", "AMRLiRT");
        params.put("parrentXSDPath","../../xsd/AMRLiRT/AMRLIRT.xsd");
        params.put("dataFileName","FinalizeLGDData.xml");
        params.put("tagNameToTakeLinkedTag","type");
        params.put("rootElementName", "finalizeLGDRs");
        params.put("RqRootElementName", "finalizeLGDRq");
        params.put("xsdBase","AMRLiRT.xsd");
        mockCycle("AMRLiRT", "FinalizeLGD", "NCP", params);

        params.clear();
        params.put("systemName", "AMRLiRT");
        params.put("parrentXSDPath","../../xsd/AMRLiRT/AMRLIRT.xsd");
        params.put("dataFileName","CalculateLGDData.xml");
        params.put("tagNameToTakeLinkedTag","comment");
        params.put("rootElementName", "calculateLGDRs");
        params.put("RqRootElementName", "calculateLGDRq");
        params.put("xsdBase","AMRLiRT.xsd");
        mockCycle("AMRLiRT", "CalculateLGD", "NCP", params);

        params.clear();
        params.put("systemName", "FinRep");
        params.put("parrentXSDPath","../../xsd/FinRep/ASFO.xsd");
        params.put("tagNameToTakeLinkedTag","dealId");
        params.put("rootElementName", "getFinAnalysisRs");
        params.put("RqRootElementName", "getFinAnalysisRq");
        params.put("xsdBase","ASFO.xsd");
        mockCycle("FinRep", "FinAnalysisImport", "NCP", params);

        params.clear();
        params.put("systemName", "FinRep");
        params.put("parrentXSDPath","../../xsd/FinRep/ASFO.xsd");
        params.put("tagNameToTakeLinkedTag","finReportType");
        params.put("rootElementName", "getFinReportRs");
        params.put("RqRootElementName", "getFinReportRq");
        params.put("xsdBase","ASFO.xsd");
        mockCycle("FinRep", "FinReportImport", "NCP", params);

        params.clear();
        params.put("systemName", "FinRep");
        params.put("parrentXSDPath","../../xsd/FinRep/ASFO.xsd");
        params.put("tagNameToTakeLinkedTag","entityType");
        params.put("rootElementName", "getRatingsAndFactorsRs");
        params.put("RqRootElementName", "getRatingsAndFactorsRq");
        params.put("xsdBase","ASFO.xsd");
        mockCycle("FinRep", "ImportRating", "NCP", params);

        params.clear();
        params.put("systemName", "FinRep");
        params.put("parrentXSDPath","../../xsd/FinRep/ASFO.xsd");
        params.put("tagNameToTakeLinkedTag","status");
        params.put("rootElementName", "updateRatingRs");
        params.put("RqRootElementName", "updateRatingRq");
        params.put("xsdBase","ASFO.xsd");
        mockCycle("FinRep", "UpdateRating", "NCP", params);
    }

    public void renewDataBBMO() {

        Map<String, String> params = null;
        params = new HashMap<String, String>();

        params.clear();
        params.put("rootElementName", "SrvPutRemoteLegalAccOperAppRq");
        params.put("operationsXSD", "../../xsd/CBBOL/BBMOOperationElements.xsd");
        params.put("systemName","CBBOL");
        params.put("xsdBase","BBMOOperationElements.xsd");
        driverCycle("CBBOL", "SrvPutRemoteLegalAccOperAppRq", "KD4", params);

        params.clear();
        params.put("rootElementName", "SrvUpdateClientReferenceDataRq");
        params.put("operationsXSD", "../../xsd/BBMO/BBMOOperationElements.xsd");
        params.put("systemName","BBMO");
        params.put("xsdBase","BBMOOperationElements.xsd");
        driverCycle("BBMO", "SrvUpdateClientReferenceDataRq", "KD4", params);

        params.clear();
        params.put("rootElementName", "CKPITProductsDepositsNSOReq");
        params.put("operationsXSD", "../../xsd/CKPIT/ckpit_integration.xsd");
        params.put("systemName","CKPIT");
        params.put("xsdBase","ckpit_integration.xsd");
        driverCycle("CKPIT", "CKPITProductsDepositsNSOReq", "NCP", params);

        params.clear();
        params.put("rootElementName", "CKPITProductsLoansReq");
        params.put("operationsXSD", "../../xsd/CKPIT/ckpit_integration.xsd");
        params.put("systemName","CKPIT");
        params.put("xsdBase","ckpit_integration.xsd");
        driverCycle("CKPIT", "CKPITProductsLoansReq", "NCP", params);
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
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(xml.getBytes());
        builder.parse(stream);

        //TODO Эта валидация гораздо круче, но она не пашет. Надо разобратьтся почему.
//        if (validator == null) throw new NullPointerException();
//        Source xmlReader = new StreamSource(new StringReader(xml));
//        validator.validate(xmlReader);
        return true;
    }
}
