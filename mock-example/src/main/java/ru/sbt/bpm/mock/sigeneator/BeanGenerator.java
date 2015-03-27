package ru.sbt.bpm.mock.sigeneator;

import ru.sbt.bpm.mock.sigeneator.inentities.IntegrationPoint;
import ru.sbt.bpm.mock.sigeneator.inentities.SystemTag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sbt-hodakovskiy-da on 03.02.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */

// Генератор bean
// TODO создать писок классов которые могут указываться в аттрибуте CLASS тега BEAN, а так же продумать способ их выбора для генерации тегов.
// TODO Нужна возможность определения namespace alias для тега.
public class BeanGenerator {

    @Deprecated
    private static final String SPRING_FACTORY_CLASS = "org.springframework.jndi.JndiObjectFactoryBean";

    // Генерация beans
    @Deprecated
    private String generationJMSBean(String beanId, String propertyValue) {
        return "<bean id=\"" + beanId + "\" class=\"" + SPRING_FACTORY_CLASS + "\">\n"
                + "<property name=\"jndiName\" value=\"jms/" + propertyValue + "\"/>\n</bean>\n";
    }

    private List<SystemTag> aSystems;

    @Deprecated
    private Set<Pair<String, String>> aSetOfQueue;

    public BeanGenerator(List<SystemTag> systems) {
        this.aSystems = systems;
        this.aSetOfQueue = new HashSet<Pair<String, String>>();
    }

    // получаем список очередей для передачи сообщений
    @Deprecated
    public void putQueueIntoSet() {
        if (aSystems == null || aSystems.isEmpty())
            return;

        for (SystemTag system : aSystems)
            for (IntegrationPoint intPoint : system.getListOfIntegrationPoints())
                aSetOfQueue.add(new Pair<String, String>(intPoint.getaIncomeQueue(), intPoint.getaOutcomeQueue()));
    }

    // Получаем стоку описания JSM bean
    @Deprecated
    public String createBeans() {
        StringBuilder sb = new StringBuilder();

        // Создание фабрики
        sb.append(generationJMSBean("jndiConnectionFactory", "CF"));

        // Создание bean для каждой из очередей
        for (Pair<String, String> queue : aSetOfQueue) {
            sb.append(generationJMSBean("requestQueue", queue.getaFirst()));
            sb.append(generationJMSBean("replyQueue", queue.getaSecond()));
        }

        return sb.toString();
    }

    /**
     * Генериция bean любого типа и с любым количестовом property
     * @param beanId - аттрибут id
     * @param beanClass - аттрибут class
     * @param properties - список property, где в Pair первое значение - name, второе значение - value
     */
    private void generateBean(String beanId, String beanClass, List<Pair<String, String>> properties) {
        StringBuilder sb = new StringBuilder("<bean id=\"");
        sb.append(beanId);
        sb.append("\" class=\"");
        sb.append(beanClass);
        sb.append("\"");
        if (properties == null || properties.isEmpty())
            sb.append("/>\n");
        else {
            sb.append(">\n");
            sb.append(generateProperty(properties));
            sb.append("</bean>\n");
        }
    }

    /**
     * Создание property
     * @param properties - список property, где в Pair первое значение - name, второе значение - value
     */
    private String generateProperty(List<Pair<String, String>> properties) {
        StringBuilder sb = new StringBuilder();
        for (Pair<String, String> property : properties)
            sb.append("<property name=\"" + property.getaFirst() + "\" value=\"" + property.getaSecond() + "\"/>\n");
        return sb.toString();
    }

    /** Список классов для генерации bean */
    static class ListOfClasses {

        /** Spring factory */
        public static final String SPRING_FACTORY_BEAN_CLASS = "org.springframework.jndi.JndiObjectFactoryBean";

        /** JMS Spring Header Mapper */
        public static final String JMS_SPRING_HEADER_MAPPER_BEAN_CLASS = "org.springframework.integration.jms.DefaultJmsHeaderMapper";

        /** Refreshable transformer */
        public static final String REFRESHABLE_TRANSFORMER_BEAN_CLASS = "ru.sbt.bpm.mock.bean.RefreshableXSLTransformer";

        /** JSP Resolver */
        public static final String JSP_RESOLVER_BEAN_CLASS = "org.springframework.web.servlet.view.InternalResourceViewResolver";

        /** Channel service */
        public static final String CHANNEL_SERVICE_BEAN_CLASS = "ru.sbt.bpm.mock.service.ChannelService";

        /** Transform service */
        public static final String TRANSFORM_SERVICE_BEAN_CLASS = "ru.sbt.bpm.mock.service.TransformService";

        /** XML Data Service */
        public static final String XML_DATA_SERVICE_BEAN_CLASS = "ru.sbt.bpm.mock.service.XmlDataService";

        /** Client service */
        public static final String CLIENT_SERVICE_BEAN_CLASS = "ru.sbt.bpm.mock.gateway.ClientService";
    }
}
