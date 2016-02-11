package ru.sbt.bpm.mock.generator.spring.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.generator.spring.context.bean.Bean;
import ru.sbt.bpm.mock.generator.spring.context.bean.BeanContainer;
import ru.sbt.bpm.mock.spring.service.SpringContextGeneratorService;

import java.util.ArrayList;

import static org.testng.Assert.*;

/**
 * @author sbt-bochev-as on 13.01.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ContextConfiguration(locations = {"/env/mockapp-servlet.xml"})
public class SpringContextGeneratorServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    SpringContextGeneratorService springContextGeneratorService;

    @Test
    public void testToXml() throws Exception {
        BeanContainer beanContainer = new BeanContainer();
        beanContainer.setBeans(new ArrayList<Bean>());
        beanContainer.getBeans().add(new Bean(null, "beanId1", "className1", null, null));
        beanContainer.getBeans().add(new Bean("test comment", "beanId2", "className2", null, null));

        assertEquals(springContextGeneratorService.toXml(beanContainer), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<beans xmlns:int=\"http://www.springframework.org/schema/integration\"\n" +
                "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "       xmlns:int-jms=\"http://www.springframework.org/schema/integration/jms\"\n" +
                "       xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans   http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/integration   http://www.springframework.org/schema/integration/spring-integration.xsd http://www.springframework.org/schema/integration   http://www.springframework.org/schema/integration/spring-integration.xsd http://www.springframework.org/schema/integration/jms   http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd\">\n" +
                "   <imports resource=\"contextConfigs/base-config.xml\"/>\n" +
                "   <imports resource=\"contextConfigs/logging-config.xml\"/>\n" +
                "   <bean id=\"beanId1\" class=\"className1\"/>\n" +
                "   <!--test comment--><bean id=\"beanId2\" class=\"className2\"/>\n" +
                "</beans>\n" +
                "");
    }
}