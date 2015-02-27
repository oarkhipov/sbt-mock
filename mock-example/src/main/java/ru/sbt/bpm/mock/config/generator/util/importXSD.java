package ru.sbt.bpm.mock.config.generator.util;

import org.apache.commons.io.FileUtils;
import ru.sbt.bpm.mock.sigeneator.GenerateMockAppServlet;
import ru.sbt.bpm.mock.sigeneator.inentities.Dependency;
import ru.sbt.bpm.mock.sigeneator.inentities.IntegrationPoint;
import ru.sbt.bpm.mock.sigeneator.inentities.LinkedTag;
import ru.sbt.bpm.mock.sigeneator.inentities.SystemTag;
import ru.sbt.bpm.mock.utils.SaveFile;
import ru.sbt.bpm.mock.utils.Xsl20Transformer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.nio.file.Path;
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
     * @param params параметры xsl
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
     * @param params параметры xsl
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
     * @param params параметры xsl
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
     * @param params параметры xsl
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
            if (!params.containsKey("systemName")) {
                params.put("systemName", system);
            }
            if (!params.containsKey("headerType")) {
                params.put("headerType", msgType);
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
            if (!params.containsKey("systemName")) {
                params.put("systemName", system);
            }
            if (!params.containsKey("headerType")) {
                params.put("headerType", msgType);
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

        //instance.loadConfig("NCPConfig.xml");
        instance.loadConfig("BBMOConfig.xml");
        //instance.loadConfig("KKMBConfig.xml");
    }

    public void loadConfig(String configFilename) throws Exception {
        final String file = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF\\MockConfigFiles\\" + configFilename;
        GenerateMockAppServlet gen1 = GenerateMockAppServlet.getInstance(file);
        gen1.setaFilePath(file);
        gen1.init();

        for (SystemTag system : gen1.getaMockConfig().getListOfSystems())
        {
            for (IntegrationPoint point : system.getListOfIntegrationPoints()) {
                importIntegrationPoint(system, point);
            }
        }
    }


    /***
     * создание точки интеграции
     * @param system результат парсинга конфига
     * @param point результат парсинга конфига
     */
    public void importIntegrationPoint(SystemTag system, IntegrationPoint point) throws Exception {
        copyXSDFiles(system, point);
        if (point.getaIntegrationPointType().equals("Mock")) {
            importIntegrationPointMock(system, point);
        } else if (point.getaIntegrationPointType().equals("Driver")) {
            importIntegrationPointDriver(system, point);
        } else {
            throw new IllegalArgumentException("Integration point type {"+point.getaIntegrationPointType()+"} not implemented");
        }
    }

    /**
     * копирует входящие xsd-файлы с зависимостями
     * @param system результат парсинга конфига
     * @param point результат парсинга конфига
     * @throws Exception
     */
    public void copyXSDFiles(SystemTag system, IntegrationPoint point) throws Exception {
        File baseDir = findFolder(system);
        System.out.println("Используется дирректория {"+baseDir.getAbsolutePath()+"}" );
        importFile(baseDir.getAbsolutePath() + File.separator + system.getaRootXSD(), system.getaSystemName());
        importFile(baseDir.getAbsolutePath() + File.separator + point.getaXsdFile(), system.getaSystemName());
        for (Dependency dependency : point.getaDependencies().getaDependency()) {
            String subfolder = system.getaSystemName();
            if (dependency.getaXsdFile().contains("\\") || dependency.getaXsdFile().contains("/") ) { //TODO тот код надо вынести в функцию и применить и к point.getaXsdFile()
                int i = dependency.getaXsdFile().lastIndexOf("/");
                int j = dependency.getaXsdFile().lastIndexOf("\\");
                int k = i>j?i:j;
                subfolder += File.separator + dependency.getaXsdFile().substring(0, k);
            }
            importFile(baseDir.getAbsolutePath() + File.separator + dependency.getaXsdFile(), subfolder);
        }
    }



    /**
     * Получает путь к папке и проверяет разные возможные пути, чтобы найти нужную нам папку
     * @param system результат парсинга конфига
     * @throws Exception
     */
    private File findFolder(SystemTag system) throws FileNotFoundException {
        List<String> possiblePaths = new ArrayList<String>(); // записываем в список всякие разные пути, по которым можна найти папку
        possiblePaths.add(getPath()); //основной путь
        possiblePaths.add(getWebInfPath()); //WebInf папку
        possiblePaths.add(getExamplesPath()); //папку с примерами
        possiblePaths.add(""); //корень для абсолютных путей и рабочей дирректории
        for (String subPath : possiblePaths ) { //и по очереди проверям эти пути
            try {
                File baseDir = new File(subPath + system.getaPathToXSD());
                if (!baseDir.exists()) throw new FileNotFoundException(); //папка есть?
                if (!baseDir.isDirectory()) throw new FileNotFoundException(); //это папка?
                File baseXSD = new File(subPath + system.getaPathToXSD() + File.separator + system.getaRootXSD() ); //также проверим что в этой папке есть xsd
                if (!baseXSD.exists()) throw new FileNotFoundException(); //xsd есть?
                if (baseXSD.isDirectory()) throw new FileNotFoundException(); //xsd это не папка?
                if (baseXSD.getTotalSpace() == 0) throw new FileNotFoundException(); //xsd не пустой?
                return baseDir; //нашли нужный нам файл
            } catch (Exception e) {
                //не нашли по этому пути, посмотрим в следующем
            }
        }
        throw new FileNotFoundException(system.getaPathToXSD() + "\\" + system.getaRootXSD());
    }

    /**
     * копирование одного конкретного xsd-файла в дирректроию WEB-INF\xsd\{subFolder}
     * Проверяет хэш файла, и если такой уже есть, то не копирует
     * @param filePathToImport файл, который надо скопировать
     * @param subFolder подпапка в WEB-INF\xsd куда будет помешен файл
     */
    private void importFile(String filePathToImport, String subFolder) throws Exception {
        File fileToImport = new File(filePathToImport);
        File copyTo = new File(getWebInfPath()+ File.separator + "xsd" + File.separator + subFolder + File.separator + fileToImport.getName());
        if (copyTo.exists()) {
            long cheksumFrom = FileUtils.checksumCRC32(fileToImport);
            long cheksumTo = FileUtils.checksumCRC32(copyTo);
            if (cheksumTo == cheksumFrom) {
                System.out.println("Файл {"+fileToImport.getName()+"} не нуждается в обновлении");
                return; //файлы одинаковые, копировать не нужно
            }
            FileUtils.copyFile(fileToImport, copyTo);
            System.out.println("Файл {" + fileToImport.getName() + "} обновлен");
            return;
        }
        FileUtils.copyFile(fileToImport, copyTo);
        System.out.println("Файл {" + fileToImport.getName() + "} скопирован");
        return;
    }

    private void importIntegrationPointDriver(SystemTag system, IntegrationPoint point) {
        Map<String, String> params = null;
        params = new HashMap<String, String>();

        String systemName = system.getaSystemName();
        String headerType = getHeaderTypeByHeaderNamespace(system);

        params.put("rootElementName", point.getaRqRootElementName());
        params.put("operationsXSD", "../../xsd/"+systemName+"/"+point.getaXsdFile() );
        params.put("xsdBase", system.getaRootXSD());
        driverCycle(systemName, point.getaIntegrationPointName(), headerType, params);
    }

    private void importIntegrationPointMock(SystemTag system, IntegrationPoint point) {
        Map<String, String> params = null;
        params = new HashMap<String, String>();

        String systemName = system.getaSystemName();
        String headerType = getHeaderTypeByHeaderNamespace(system);
        List<LinkedTag> linkedTagList = point.getaLinkedTagSequence().getaListOfLinkedTags();
        String linkedTag = linkedTagList.get(linkedTagList.size()-1).getaTag(); //TODo пока планируемый функционал реализован не полность. Сейчас проверяется только последний тэг из последовательности


        params.put("rootElementName", point.getaRsRootElementName());
        params.put("RqRootElementName", point.getaRqRootElementName());
        params.put("operationsXSD", "../../xsd/"+systemName+"/"+point.getaXsdFile() );
        params.put("xsdBase", system.getaRootXSD());
        params.put("tagNameToTakeLinkedTag", linkedTag);
        mockCycle(systemName, point.getaIntegrationPointName(), headerType, params);
    }


    /**
     * определяет тип заголовка из параметра из конфига
     * @param system результат парсинга конфига
     * @return
     */
    public static String getHeaderTypeByHeaderNamespace(SystemTag system) {
        if (system.getaHeaderNamespace().equals("http://sbrf.ru/prpc/mq/headers")) {
            return "KD4";
        } else if (system.getaHeaderNamespace().equals("http://sbrf.ru/NCP/esb/envelope/")) {
            return "NCP";
        }
        throw new IllegalArgumentException("Header namespace {"+system.getaHeaderNamespace()+"} not implemented");
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
