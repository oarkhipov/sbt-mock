package ru.sbt.bpm.mock.generator.util.TemplateEngine.impl;

import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.SystemTag;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-vostrikov-mi on 10.04.2015.
 */
public class RouterMappingTemplateImpl extends ChannelTemplateImpl {


    private String xmlCommentDelimeterTemplate = "\r\n\t\t<!-- ${name} -->\r\n";
    private String xmlMappingTemplate = "\t\t<ix:mapping value=\"${operationName}\" channel=\"${name}Channel\"/>\r\n";

    private Map<String, String> operationNames;
    private static RouterMappingTemplateImpl ourInstance = new RouterMappingTemplateImpl();

    public static RouterMappingTemplateImpl getInstance() {
        return ourInstance;
    }

    protected RouterMappingTemplateImpl() {
    }

    @Override
    protected void systemChanels(StringBuilder result, SystemTag system) {
        operationNames = new HashMap<String, String>();
        result.append(formXMLComment(system.getSystemName()));
        for (IntegrationPoint point : system.getListIntegrationPoint()) {
            String name = integrationPointName(point, true); //true - не делать мапинги для драйверов
            if (name != null) {
                String operationName;
                //если в конфиге есть имя операции, то берем его. Если нет - используем name.
                //TODO теоретически возможно что мапинг придется делать по какому-тодургому поля, не имени операции и не полю operationName. Для этого случая следует ввести еще одно необязавтельное поле в кинфиг. Но пока таких случаев нет.
                if (point.getOperationName() != null && !point.getOperationName().isEmpty()) {
                    operationName = point.getOperationName();
                } else {
                    operationName = name;
                }
                operationNames.put(name, operationName);
                result.append(formMappingXML(name, operationName));
            }
        }
    }

    /**
     * создает строчку канала из name
     */
    protected String formMappingXML(String name, String opName) {
        return xmlMappingTemplate.replace("${name}", name).replace("${operationName}", opName);
    }

    /**
     * создает строчку канала из name
     */
    protected String formXMLComment(String name) {
        return xmlCommentDelimeterTemplate.replace("${name}", name);
    }
}
