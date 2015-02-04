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

// Генератор JMS bean
public class BeanGenerator {

    public static final String SPRING_FACTORY_CLASS = "org.springframework.jndi.JndiObjectFactoryBean";

    // Генерация beans
    private String generationJMSBean(String beanId, String propertyValue) {
        return "<bean id=\"" + beanId + "\" class=\"" + SPRING_FACTORY_CLASS + "\">\n"
                + "<property name=\"jndiName\" value=\"jms/" + propertyValue + "\"/>\n</bean>\n";
    }

    private List<SystemTag> aSystems;

    private Set<Pair<String, String>> aSetOfQueue;

    public BeanGenerator(List<SystemTag> systems) {
        this.aSystems = systems;
        this.aSetOfQueue = new HashSet<Pair<String, String>>();
    }

    // получаем список очередей для передачи сообщений
    public void putQueueIntoSet() {
        if (aSystems == null || aSystems.isEmpty())
            return;

        for (SystemTag system : aSystems)
            for (IntegrationPoint intPoint : system.getListOfIntegrationPoints())
                aSetOfQueue.add(new Pair<String, String>(intPoint.getaIncomeQueue(), intPoint.getaOutcomeQueue()));
    }

    // Получаем стоку описания JSM bean
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
}
