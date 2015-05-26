package ru.sbt.bpm.mock.generator.util.TemplateEngine.impl;

import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.SystemTag;
import ru.sbt.bpm.mock.generator.util.IncrimentingString;
import ru.sbt.bpm.mock.generator.util.TemplateEngine.ConfigTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-vostrikov-mi on 10.04.2015.
 */
public class ChannelTemplateImpl implements ConfigTemplate {

    private String xmlCommentDelimeterTemplate = "\r\n\t<!-- ${name} -->\r\n";
    private String xmlChanelTemplate = "\t<channel id=\"${name}Channel\"/>\r\n";

    protected List<String> allIntegrationPoints;

    protected IncrimentingString inrc;

    private static ChannelTemplateImpl ourInstance = new ChannelTemplateImpl();

    public static ChannelTemplateImpl getInstance() {
        return ourInstance;
    }

    protected ChannelTemplateImpl() {
    }

    public String getValue(MockConfig config, String[] Args) {
        return getValue(config);
    }


    public String getValue(MockConfig config) {
        allIntegrationPoints = config.getIntegrationPointNames();
        inrc = new IncrimentingString(allIntegrationPoints.size());
        StringBuilder result = new StringBuilder();
        for (SystemTag system : config.getListOfSystems()) {
            systemChanels(result, system);
        }
        return result.toString();
    }

    protected void systemChanels(StringBuilder result, SystemTag system) {
        result.append(formXMLComment(system.getSystemName()));
        for (String name : integrationPointNames(system.getListIntegrationPoint())) {
            result.append(formChanelXML(name));
        }
    }

    /**
     * @param points список точек интеграции
     * @return список имен точек интеграции
     */
    protected List<String> integrationPointNames(List<IntegrationPoint> points) {
        return integrationPointNames(points, false);
    }

    /**
     * @param points список точек интеграции
     * @param mockOnly только заглуш   ки, без драйверов?
     * @return список имен точек интеграции
     */
    protected List<String> integrationPointNames(List<IntegrationPoint> points, boolean mockOnly) {
        List<String> result = new ArrayList<String>();
        for (IntegrationPoint point : points) {
            String name = integrationPointName(point, mockOnly);
            if (name != null) {
                result.add(name);
            }
        }
        return result;
    }

    protected String integrationPointName(IntegrationPoint point, boolean mockOnly) {
        String result = null;
        if (!mockOnly || point.getIntegrationPointType().equals(IntegrationPoint.MOCK)) {
            String name = point.getIntegrationPointName();
            if (allIntegrationPoints.indexOf(name) != allIntegrationPoints.lastIndexOf(name)) { //если у нас есть две точки с одиноковым именем, то добавим к ним номер
                name+=inrc.get();
            }
            result = name;
        }
        return result;
    }

    /**
     * создает строчку канала из name
     */
    private String formChanelXML(String name) {
        return xmlChanelTemplate.replace("${name}", name);
    }

    /**
     * создает строчку канала из name
     */
    private String formXMLComment(String name) {
        return xmlCommentDelimeterTemplate.replace("${name}", name);
    }
}
