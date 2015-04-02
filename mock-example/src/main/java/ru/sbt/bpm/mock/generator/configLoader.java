package ru.sbt.bpm.mock.generator;

import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.LinkedTag;
import ru.sbt.bpm.mock.config.entities.MappedTagSequence;
import ru.sbt.bpm.mock.config.entities.SystemTag;
import ru.sbt.bpm.mock.generator.xmldata.generateDataXml;
import ru.sbt.bpm.mock.generator.xsd.importXSD;
import ru.sbt.bpm.mock.generator.spring.integration.GenerateMockAppServlet;
import ru.sbt.bpm.mock.generator.xml.generateExampleXml;
import ru.sbt.bpm.mock.generator.xsddata.generateDataXsd;
import ru.sbt.bpm.mock.generator.xsl.generateXsl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * файл для запуска генерации заглушки по конфигу.
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class configLoader {
    private static configLoader ourInstance = new configLoader();

    public static configLoader getInstance() {
        return ourInstance;
    }

    private configLoader() {
    }

    public void loadConfig(String configFilename) throws Exception {
        final String file = localPaths.getWebInfPath() + "\\MockConfigFiles\\" + configFilename;
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
        importXSD instance = importXSD.getInstance();
        instance.copyXSDFiles(system, point);
        if (point.getaIntegrationPointType().equals("Mock")) {
            importIntegrationPointMock(system, point);
        } else if (point.getaIntegrationPointType().equals("Driver")) {
            importIntegrationPointDriver(system, point);
        } else {
            throw new IllegalArgumentException("Integration point type {"+point.getaIntegrationPointType()+"} not implemented");
        }
    }

    private void importIntegrationPointMock(SystemTag system, IntegrationPoint point) {
        importXSD instance = importXSD.getInstance();
        Map<String, String> params = null;
        params = new HashMap<String, String>();

        String systemName = system.getaSystemName();
        String headerType = getHeaderTypeByHeaderNamespace(system);
        List<LinkedTag> linkedTagList = point.getaLinkedTagSequence().getaListOfLinkedTags();
        String linkedTag = linkedTagList.get(linkedTagList.size()-1).getaTag(); //TODo пока планируемый функционал реализован не полностью в разрезе тестов. Сейчас в создании примеров сообщений учитывается только последний тэг последовательности. Это может вызвать проблемы с автотестами в глубоких или повтаряющихся линкедтагах. Это не должно повлиять на работосопособность самих заглушек - только на создание примеров и прохождение автотестов
        String linkedTagQuerry = formLinkedTagSequenceQuerry(linkedTagList);

        params.put("rootElementName", point.getaRsRootElementName()); //имя html-тэга операции
        params.put("RqRootElementName", point.getaRqRootElementName()); //имя html-тэга запроса операции
        params.put("operationsXSD",  getUriFilename(localPaths.getWebInfPath() + "/xsd/" + systemName + "/" + point.getaXsdFile())); //путь к xsd
        params.put("xsdBase", system.getaRootXSD()); //родительский xsd - xsd общий для всех ТИ системы
        params.put("tagNameToTakeLinkedTag", linkedTag); //linkedTag
        if (linkedTagQuerry!=null) {
            params.put("tagQuerryToTakeLinkedTag", linkedTagQuerry); //запрос на нахождение linkedTag в запросе
        }
        if (point.getaOperationName() != null & !point.getaOperationName().isEmpty()) {
            params.put("operationName", point.getaOperationName()); //имя операции. Если не задано - возьмет html-тэг ответа
        }
        mockCycle(systemName, point.getaIntegrationPointName(), headerType, params, point.getaMappedTags());
    }

    private void importIntegrationPointDriver(SystemTag system, IntegrationPoint point) {
        importXSD instance = importXSD.getInstance();
        Map<String, String> params = null;
        params = new HashMap<String, String>();

        String systemName = system.getaSystemName();
        String headerType = getHeaderTypeByHeaderNamespace(system);

        params.put("rootElementName", point.getaRqRootElementName());
        params.put("operationsXSD", getUriFilename(localPaths.getWebInfPath() + "/xsd/" + systemName + "/" + point.getaXsdFile()));
        params.put("xsdBase", system.getaRootXSD());
        if (point.getaOperationName() != null & !point.getaOperationName().isEmpty()) {
            params.put("operationName", point.getaOperationName());
        }
        driverCycle(systemName, point.getaIntegrationPointName(), headerType, params);
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
     * создание x-path запроса по последовательности тэгов
     * @param linkedTagList список тэгов
     * @return x-path запрос на последовательность тэгов
     */
    private String formLinkedTagSequenceQuerry(List<LinkedTag> linkedTagList) {
        String querry = "/";
        if (linkedTagList== null || linkedTagList.isEmpty()) {
            return null;
        } else {
            for (LinkedTag tag : linkedTagList) {
                if (tag.getaNameSpace() != null && !tag.getaNameSpace().isEmpty()) {
                    querry += "/*[local-name()='" + tag.getaTag() + "' and namespace-uri()='" + tag.getaNameSpace() + "']";
                } else {
                    querry += "/*[local-name()='" + tag.getaTag() + "']";
                }
            }
            querry += "/text()";
        }
        return querry;
    }


    /**
     * полный цикл создания mock-сервиса
     * @param system имя подпапки
     * @param name имя сервиса(имя файла)
     * @param params параметры xsl
     */
    public void mockCycle(String system, String name, String msgType, Map<String, String> params, MappedTagSequence mappedTags) {
        try
        {
            if (params==null) {
                params = new HashMap<String, String>(1);
            }
            if (!params.containsKey("dataFileName")) {
                params.put("dataFileName", name + "Data.xml"); //имя дата-файла
            }
            if (!params.containsKey("system")) {
                params.put("system", system); //имя системы //TODO один из этих параметров неправильно приплыл из мерджинга, надо убрать лишнее
            }
            if (!params.containsKey("systemName")) {
                params.put("systemName", system); //имя системы //TODO один из этих параметров неправильно приплыл из мерджинга, надо убрать лишнее
            }
            if (!params.containsKey("headerType")) {
                params.put("headerType", msgType); //тип заголовка из реаилзованных
            }

            //для создания запроса тем же xsl-файлом, что и ответ нужно поменять несколько параметров. Для этого и делаем altParams
            Map <String, String> altParams = new HashMap<String, String>(params);
            altParams.put("rootElementName", params.get("RqRootElementName"));
            if (!params.containsKey("operationsXSD")) {
                params.put("operationsXSD", getUriFilename(localPaths.getWebInfPath() + "/xsd/" + system + "/" + name + "Response.xsd"));

                if (!params.containsKey("altOperationsXSD")) {
                    altParams.put("operationsXSD", getUriFilename(localPaths.getWebInfPath() + "/xsd/" + system + "/" + name + "Request.xsd"));
                }
            }

            if (params.containsKey("altOperationsXSD")) {
                altParams.put("operationsXSD", params.get("altOperationsXSD"));
            }

            altParams.put("operation-name", params.get("rootElementName"));

            //не вставляем в этветы комменты с обозначением сколько элементов доступно
            params.put("omitComments", "true");

            generateExampleXml.getInstance().createRqExample(system, name, msgType, altParams);
            generateExampleXml.getInstance().createRsExample(system, name, msgType, params);
            generateDataXsd.getInstance().createDataXSD(system, name, "Response", params);
            File xslFile = generateXsl.getInstance().createMockXSL(system, name, params);
            generateDataXml.getInstance().createRsDataXml(system, name, params);

            //applyMappedTags(xslFile, mappedTags, params);

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
                params.put("operationsXSD", new File(localPaths.getWebInfPath() + "/xsd/"+system+"/"+name+"Request.xsd").toURI().toString());
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

            generateExampleXml.getInstance().createRqExample(system, name, msgType, params);
            generateDataXsd.getInstance().createDataXSD(system, name, "Request", params);
            generateXsl.getInstance().createDriverXSL(system, name, params);
            generateDataXml.getInstance().createRqDataXml(system, name, params);
            System.out.println(system + " " + name + " driver Done");
        } catch (Exception e) {
            System.out.println(system + " " + name + " driver Failed");
            e.printStackTrace();
        }
    }

    /**
     * превращает строку с путем к файлу в URI-строку
     */
    private String getUriFilename(String path) {
        File f = new File(path);
        return f.toURI().toString();
    }
}
