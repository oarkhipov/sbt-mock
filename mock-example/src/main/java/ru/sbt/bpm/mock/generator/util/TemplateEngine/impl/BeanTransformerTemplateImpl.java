package ru.sbt.bpm.mock.generator.util.TemplateEngine.impl;

import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.SystemTag;

/**
 * Created by sbt-vostrikov-mi on 13.04.2015.
 */
public class BeanTransformerTemplateImpl extends ChannelTemplateImpl {


    private String xmlCommentDelimeterTemplate = "\r\n\t\t<!-- ${name} -->\r\n";
    private String xmlMappingTemplate = "\t<beans:bean id=\"${systemName}_${name}\" class=\"ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer\">\r\n" +
            "\t\t<beans:property name=\"xslPath\" value=\"/WEB-INF/xsl/${systemName}/${name}.xsl\"/>\r\n" +
            "\t</beans:bean>\r\n";

    private static BeanTransformerTemplateImpl ourInstance = new BeanTransformerTemplateImpl();

    public static BeanTransformerTemplateImpl getInstance() {
        return ourInstance;
    }

    protected BeanTransformerTemplateImpl() {
    }

    @Override
    protected void systemChanels(StringBuilder result, SystemTag system) {
        String systemName = system.getSystemName();
        result.append(formXMLComment(systemName));
        for (IntegrationPoint point : system.getListIntegrationPoint()) {
            String name = integrationPointName(point, true); //true - не делать мапинги для драйверов
            if (name != null) {
                result.append(formBeanXML(name, systemName));
            }
        }
    }

    /**
     * создает строчку канала из name
     */
    protected String formBeanXML(String name, String sysName) {
        return xmlMappingTemplate.replace("${name}", name).replace("${systemName}", sysName);
    }

    /**
     * создает строчку канала из name
     */
    protected String formXMLComment(String name) {
        return xmlCommentDelimeterTemplate.replace("${name}", name);
    }
}
