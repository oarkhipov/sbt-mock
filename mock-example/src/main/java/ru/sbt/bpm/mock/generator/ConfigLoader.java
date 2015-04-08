package ru.sbt.bpm.mock.generator;

import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.LinkedTag;
import ru.sbt.bpm.mock.config.entities.MappedTagSequence;
import ru.sbt.bpm.mock.config.entities.SystemTag;
import ru.sbt.bpm.mock.generator.xmldata.GenerateDataXml;
import ru.sbt.bpm.mock.generator.xsd.ImportXSD;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.generator.xml.GenerateExampleXml;
import ru.sbt.bpm.mock.generator.xsddata.GenerateDataXsd;
import ru.sbt.bpm.mock.generator.xsl.GenerateXsl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * файл для запуска генерации заглушки по конфигу.
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class ConfigLoader {
    private static ConfigLoader ourInstance = new ConfigLoader();

    public static ConfigLoader getInstance() {
        return ourInstance;
    }

    private ConfigLoader() {
    }

    public void loadConfig(String configFilename) throws Exception {
        final String file = LocalPaths.getWebResPath() + "\\MockConfigFiles\\" + configFilename;
        MockConfigContainer gen1 = MockConfigContainer.getInstance(file);
        gen1.init();

        for (SystemTag system : gen1.getConfig().getListOfSystems())
        {
            for (IntegrationPoint point : system.getListIntegrationPoint()) {
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
        ImportXSD instance = ImportXSD.getInstance();
        instance.copyXSDFiles(system, point);
        if (point.getIntegrationPointType().equals("Mock")) {
            importIntegrationPointMock(system, point);
        } else if (point.getIntegrationPointType().equals("Driver")) {
            importIntegrationPointDriver(system, point);
        } else {
            throw new IllegalArgumentException("Integration point type {"+point.getIntegrationPointType()+"} not implemented");
        }
    }

    private void importIntegrationPointMock(SystemTag system, IntegrationPoint point) {
        ImportXSD instance = ImportXSD.getInstance();
        Map<String, String> params = null;
        params = new HashMap<String, String>();

        String systemName = system.getSystemName();
        String headerType = getHeaderTypeByHeaderNamespace(system);
        List<LinkedTag> linkedTagList = point.getLinkedTagSequence().getListOfLinkedTags();
        String linkedTag = linkedTagList.get(linkedTagList.size()-1).getTag(); //TODo пока планируемый функционал реализован не полностью в разрезе тестов. Сейчас в создании примеров сообщений учитывается только последний тэг последовательности. Это может вызвать проблемы с автотестами в глубоких или повтаряющихся линкедтагах. Это не должно повлиять на работосопособность самих заглушек - только на создание примеров и прохождение автотестов
        String linkedTagQuerry = formLinkedTagSequenceQuerry(linkedTagList);

        params.put("rootElementName", point.getRsRootElementName()); //имя html-тэга операции
        params.put("RqRootElementName", point.getRqRootElementName()); //имя html-тэга запроса операции
        params.put("operationsXSD",  getUriFilename(LocalPaths.getWebInfPath() + "/xsd/" + systemName + "/" + point.getXsdFile())); //путь к xsd
        params.put("xsdBase", system.getRootXSD()); //родительский xsd - xsd общий для всех ТИ системы
        params.put("tagNameToTakeLinkedTag", linkedTag); //linkedTag
        if (linkedTagQuerry!=null) {
            params.put("tagQuerryToTakeLinkedTag", linkedTagQuerry); //запрос на нахождение linkedTag в запросе
        }
        if (point.getOperationName() != null && !point.getOperationName().isEmpty()) {
            params.put("operationName", point.getOperationName()); //имя операции. Если не задано - возьмет html-тэг ответа
        }
        if (point.getRqXsdFile()!= null && !point.getRqXsdFile().isEmpty()) {
            params.put("altOperationsXSD", getUriFilename(LocalPaths.getWebInfPath() + "/xsd/" + systemName + "/" + point.getRqXsdFile()));
        }
        params.put("dataFolderPath",getUriFilename(LocalPaths.getWebInfPath()+"\\data"));
        mockCycle(systemName, point.getIntegrationPointName(), headerType, params, null); //point.getMappedTagSequence());
    }

    private void importIntegrationPointDriver(SystemTag system, IntegrationPoint point) {
        ImportXSD instance = ImportXSD.getInstance();
        Map<String, String> params = null;
        params = new HashMap<String, String>();

        String systemName = system.getSystemName();
        String headerType = getHeaderTypeByHeaderNamespace(system);

        params.put("rootElementName", point.getRqRootElementName());
        params.put("operationsXSD", getUriFilename(LocalPaths.getWebInfPath() + "/xsd/" + systemName + "/" + point.getXsdFile()));
        params.put("xsdBase", system.getRootXSD());
        if (point.getOperationName() != null && !point.getOperationName().isEmpty()) {
            params.put("operationName", point.getOperationName());
        }
        params.put("dataFolderPath",getUriFilename(LocalPaths.getWebInfPath()+"\\data"));
        driverCycle(systemName, point.getIntegrationPointName(), headerType, params);
    }

    /**
     * определяет тип заголовка из параметра из конфига
     * @param system результат парсинга конфига
     * @return
     */
    public static String getHeaderTypeByHeaderNamespace(SystemTag system) {
        if (system.getHeaderNamespace().equals("http://sbrf.ru/prpc/mq/headers")) {
            return "KD4";
        } else if (system.getHeaderNamespace().equals("http://sbrf.ru/NCP/esb/envelope/")) {
            return "NCP";
        } else if (system.getHeaderNamespace().equals("http://schemas.xmlsoap.org/soap/envelope/")) {
            return "KKMB";
        }
        throw new IllegalArgumentException("Header namespace {"+system.getHeaderNamespace()+"} not implemented");
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
                if (tag.getNameSpace() != null && !tag.getNameSpace().isEmpty()) {
                    querry += "/*[local-name()='" + tag.getTag() + "' and namespace-uri()='" + tag.getNameSpace() + "']";
                } else {
                    querry += "/*[local-name()='" + tag.getTag() + "']";
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
                params.put("operationsXSD", getUriFilename(LocalPaths.getWebInfPath() + "/xsd/" + system + "/" + name + "Response.xsd"));

                if (!params.containsKey("altOperationsXSD")) {
                    altParams.put("operationsXSD", getUriFilename(LocalPaths.getWebInfPath() + "/xsd/" + system + "/" + name + "Request.xsd"));
                }
            }

            if (params.containsKey("altOperationsXSD")) {
                altParams.put("operationsXSD", params.get("altOperationsXSD"));
            }

            altParams.put("operation-name", params.get("rootElementName"));

            //не вставляем в этветы комменты с обозначением сколько элементов доступно
            params.put("omitComments", "true");

            GenerateExampleXml.getInstance().createRqExample(system, name, msgType, altParams);
            GenerateExampleXml.getInstance().createRsExample(system, name, msgType, params);
            GenerateDataXsd.getInstance().createDataXSD(system, name, "Response", params);
            File xslFile = GenerateXsl.getInstance().createMockXSL(system, name, params);
            GenerateDataXml.getInstance().createRsDataXml(system, name, params);

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
                params.put("operationsXSD", new File(LocalPaths.getWebInfPath() + "/xsd/"+system+"/"+name+"Request.xsd").toURI().toString());
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

            GenerateExampleXml.getInstance().createRqExample(system, name, msgType, params);
            GenerateDataXsd.getInstance().createDataXSD(system, name, "Request", params);
            GenerateXsl.getInstance().createDriverXSL(system, name, params);
            GenerateDataXml.getInstance().createRqDataXml(system, name, params);
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
