package ru.sbt.bpm.mock.generator.util.TemplateEngine.impl;

import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.SystemTag;

/**
 * Created by sbt-vostrikov-mi on 14.04.2015.
 */
public class TransformerTemplateImpl extends ChannelTemplateImpl {


    private String xmlCommentDelimeterTemplate = "\r\n\t\t<!-- ${name} -->\r\n";
    private String xmlMappingTemplate = "\t<transformer input-channel=\"${name}\" output-channel=\"MockOutboundResponse\" method=\"transform\" ref=\"${systemName}_${name}\"/>\r\n";

    private static TransformerTemplateImpl ourInstance = new TransformerTemplateImpl();

    public static TransformerTemplateImpl getInstance() {
        return ourInstance;
    }

    protected TransformerTemplateImpl() {
    }

    @Override
    protected void systemChanels(StringBuilder result, SystemTag system) {
        String systemName = system.getSystemName();
        result.append(formXMLComment(systemName));
        for (IntegrationPoint point : system.getListIntegrationPoint()) {
            String name = integrationPointName(point, true); //true - не делать мапинги для драйверов
            if (name != null) {
                result.append(formTransformerXML(name, systemName));
            }
        }
    }

    /**
     * создает строчку канала из name
     */
    protected String formTransformerXML(String name, String sysName) {
        return xmlMappingTemplate.replace("${name}", name).replace("${systemName}", sysName);
    }

    /**
     * создает строчку канала из name
     */
    protected String formXMLComment(String name) {
        return xmlCommentDelimeterTemplate.replace("${name}", name);
    }
}