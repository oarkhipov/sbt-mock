package ru.sbt.bpm.mock.generator.spring.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.generator.spring.context.bean.*;
import ru.sbt.bpm.mock.generator.spring.context.gateway.InboundGateway;
import ru.sbt.bpm.mock.generator.spring.context.gateway.OutboundGateway;
import ru.sbt.bpm.mock.spring.service.SpringContextGeneratorService;

import java.util.ArrayList;

import static org.testng.Assert.assertEquals;

/**
 * @author sbt-bochev-as on 13.01.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ContextConfiguration(locations = {"/env/mockapp-servlet.xml"})
public class SpringContextGeneratorServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    SpringContextGeneratorService springContextGeneratorService;

    @Autowired


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

    @Test
    public void testToXmlWithConstructorArgs() throws Exception {
        BeanContainer beanContainer = new BeanContainer();
        beanContainer.setBeans(new ArrayList<Bean>());
        beanContainer.getBeans().add(new Bean(null, "beanId1", "className1", null, null));
        beanContainer.getBeans().add(new Bean("test comment", "beanId2", "className2", null, null));
        beanContainer.getBeans().add(new Bean(null, "beanId3", "className3", new ConstructorArg("arg"), null));

        assertEquals(springContextGeneratorService.toXml(beanContainer), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                                                         + "<beans xmlns:int=\"http://www"
                                                                         + ".springframework.org/schema/integration\"\n"
                                                                         + "       xmlns:xsi=\"http://www.w3"
                                                                         + ".org/2001/XMLSchema-instance\"\n"
                                                                         + "       xmlns:int-jms=\"http://www"
                                                                         + ".springframework"
                                                                         + ".org/schema/integration/jms\"\n"
                                                                         + "       xmlns=\"http://www.springframework"
                                                                         + ".org/schema/beans\"\n"
                                                                         + "       xsi:schemaLocation=\"http://www"
                                                                         + ".springframework.org/schema/beans   "
                                                                         + "http://www.springframework"
                                                                         + ".org/schema/beans/spring-beans.xsd "
                                                                         + "http://www.springframework"
                                                                         + ".org/schema/integration   http://www"
                                                                         + ".springframework"
                                                                         + ".org/schema/integration/spring-integration.xsd http://www.springframework.org/schema/integration   http://www.springframework.org/schema/integration/spring-integration.xsd http://www.springframework.org/schema/integration/jms   http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd\">\n"
                                                                         + "   <imports "
                                                                         + "resource=\"contextConfigs/base-config"
                                                                         + ".xml\"/>\n"
                                                                         + "   <imports "
                                                                         + "resource=\"contextConfigs/logging-config"
                                                                         + ".xml\"/>\n"
                                                                         + "   <bean id=\"beanId1\" "
                                                                         + "class=\"className1\"/>\n"
                                                                         + "   <!--test comment--><bean "
                                                                         + "id=\"beanId2\" class=\"className2\"/>\n"
                                                                         + "   <bean id=\"beanId3\" "
                                                                         + "class=\"className3\">\n"
                                                                         + "      <constructor-arg value=\"arg\"/>\n"
                                                                         + "   </bean>\n"
                                                                         + "</beans>\n");
    }

    @Test
    public void testToXmlWithProperties() throws Exception {
        BeanContainer beanContainer = new BeanContainer();
        beanContainer.setBeans(new ArrayList<Bean>());
        beanContainer.getBeans().add(new Bean(null, "beanId1", "className1", null, null));
        beanContainer.getBeans().add(new Bean("test comment", "beanId2", "className2", null, null));
        beanContainer.getBeans().add(new Bean(null, "beanId3", "className3", null, new Property("prop", "val")));

        assertEquals(springContextGeneratorService.toXml(beanContainer), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                                                         + "<beans xmlns:int=\"http://www"
                                                                         + ".springframework.org/schema/integration\"\n"
                                                                         + "       xmlns:xsi=\"http://www.w3"
                                                                         + ".org/2001/XMLSchema-instance\"\n"
                                                                         + "       xmlns:int-jms=\"http://www"
                                                                         + ".springframework"
                                                                         + ".org/schema/integration/jms\"\n"
                                                                         + "       xmlns=\"http://www.springframework"
                                                                         + ".org/schema/beans\"\n"
                                                                         + "       xsi:schemaLocation=\"http://www"
                                                                         + ".springframework.org/schema/beans   "
                                                                         + "http://www.springframework"
                                                                         + ".org/schema/beans/spring-beans.xsd "
                                                                         + "http://www.springframework"
                                                                         + ".org/schema/integration   http://www"
                                                                         + ".springframework"
                                                                         + ".org/schema/integration/spring-integration.xsd http://www.springframework.org/schema/integration   http://www.springframework.org/schema/integration/spring-integration.xsd http://www.springframework.org/schema/integration/jms   http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd\">\n"
                                                                         + "   <imports "
                                                                         + "resource=\"contextConfigs/base-config"
                                                                         + ".xml\"/>\n"
                                                                         + "   <imports "
                                                                         + "resource=\"contextConfigs/logging-config"
                                                                         + ".xml\"/>\n"
                                                                         + "   <bean id=\"beanId1\" "
                                                                         + "class=\"className1\"/>\n"
                                                                         + "   <!--test comment--><bean "
                                                                         + "id=\"beanId2\" class=\"className2\"/>\n"
                                                                         + "   <bean id=\"beanId3\" "
                                                                         + "class=\"className3\">\n"
                                                                         + "      <property name=\"prop\" "
                                                                         + "value=\"val\"/>\n"
                                                                         + "   </bean>\n"
                                                                         + "</beans>\n");
    }

    @Test
    public void testToXmlWithInboundGateways() throws Exception {
        BeanContainer beanContainer = new BeanContainer();
        beanContainer.setBeans(new ArrayList<Bean>());
        beanContainer.setInboundGateways(new ArrayList<InboundGateway>());
        beanContainer.getBeans().add(new Bean(null, "beanId1", "className1", null, null));
        beanContainer.getInboundGateways().add(new InboundGateway(null, "jmsin", "dest", "channel", "reply", "replyDest", "factory"));


        assertEquals(springContextGeneratorService.toXml(beanContainer), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                                                         + "<beans xmlns:int=\"http://www"
                                                                         + ".springframework.org/schema/integration\"\n"
                                                                         + "       xmlns:xsi=\"http://www.w3"
                                                                         + ".org/2001/XMLSchema-instance\"\n"
                                                                         + "       xmlns:int-jms=\"http://www"
                                                                         + ".springframework"
                                                                         + ".org/schema/integration/jms\"\n"
                                                                         + "       xmlns=\"http://www.springframework"
                                                                         + ".org/schema/beans\"\n"
                                                                         + "       xsi:schemaLocation=\"http://www"
                                                                         + ".springframework.org/schema/beans   "
                                                                         + "http://www.springframework"
                                                                         + ".org/schema/beans/spring-beans.xsd "
                                                                         + "http://www.springframework"
                                                                         + ".org/schema/integration   http://www"
                                                                         + ".springframework"
                                                                         + ".org/schema/integration/spring-integration.xsd http://www.springframework.org/schema/integration   http://www.springframework.org/schema/integration/spring-integration.xsd http://www.springframework.org/schema/integration/jms   http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd\">\n"
                                                                         + "   <imports "
                                                                         + "resource=\"contextConfigs/base-config"
                                                                         + ".xml\"/>\n"
                                                                         + "   <imports "
                                                                         + "resource=\"contextConfigs/logging-config"
                                                                         + ".xml\"/>\n"
                                                                         + "   <bean id=\"beanId1\" "
                                                                         + "class=\"className1\"/>\n"
                                                                         + "   <int-jms:inbound-gateway id=\"jmsin\"\n"
                                                                         + "                            "
                                                                         + "request-destination=\"dest\"\n"
                                                                         + "                            "
                                                                         + "request-channel=\"channel\"\n"
                                                                         + "                            "
                                                                         + "reply-channel=\"reply\"\n"
                                                                         + "                            "
                                                                         + "default-reply-destination=\"replyDest\"\n"
                                                                         + "                            "
                                                                         + "connection-factory=\"factory\"/>\n"
                                                                         + "</beans>\n");
    }

    @Test
    public void testToXmlWithOutboundGateways() throws Exception {
        BeanContainer beanContainer = new BeanContainer();
        beanContainer.setBeans(new ArrayList<Bean>());
        beanContainer.setOutboundGateways(new ArrayList<OutboundGateway>());
        beanContainer.getBeans().add(new Bean(null, "beanId1", "className1", null, null));
        beanContainer.getOutboundGateways().add(new OutboundGateway(null, "jmsout", "requestDest", "replyDest", "requestChannel", "replyChannel", "headerMapper", "cf", "30000", "30000"));


        assertEquals(springContextGeneratorService.toXml(beanContainer), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                                                         + "<beans xmlns:int=\"http://www"
                                                                         + ".springframework.org/schema/integration\"\n"
                                                                         + "       xmlns:xsi=\"http://www.w3"
                                                                         + ".org/2001/XMLSchema-instance\"\n"
                                                                         + "       xmlns:int-jms=\"http://www"
                                                                         + ".springframework"
                                                                         + ".org/schema/integration/jms\"\n"
                                                                         + "       xmlns=\"http://www.springframework"
                                                                         + ".org/schema/beans\"\n"
                                                                         + "       xsi:schemaLocation=\"http://www"
                                                                         + ".springframework.org/schema/beans   "
                                                                         + "http://www.springframework"
                                                                         + ".org/schema/beans/spring-beans.xsd "
                                                                         + "http://www.springframework"
                                                                         + ".org/schema/integration   http://www"
                                                                         + ".springframework"
                                                                         + ".org/schema/integration/spring-integration.xsd http://www.springframework.org/schema/integration   http://www.springframework.org/schema/integration/spring-integration.xsd http://www.springframework.org/schema/integration/jms   http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd\">\n"
                                                                         + "   <imports "
                                                                         + "resource=\"contextConfigs/base-config"
                                                                         + ".xml\"/>\n"
                                                                         + "   <imports "
                                                                         + "resource=\"contextConfigs/logging-config"
                                                                         + ".xml\"/>\n"
                                                                         + "   <bean id=\"beanId1\" "
                                                                         + "class=\"className1\"/>\n"
                                                                         + "   <int-jms:outbound-gateway "
                                                                         + "id=\"jmsout\"\n"
                                                                         + "                             "
                                                                         + "request-destination=\"requestDest\"\n"
                                                                         + "                             "
                                                                         + "reply-destination=\"replyDest\"\n"
                                                                         + "                             "
                                                                         + "request-channel=\"requestChannel\"\n"
                                                                         + "                             "
                                                                         + "reply-channel=\"replyChannel\"\n"
                                                                         + "                             "
                                                                         + "header-mapper=\"headerMapper\"\n"
                                                                         + "                             "
                                                                         + "connection-factory=\"cf\"\n"
                                                                         + "                             "
                                                                         + "reply-timeout=\"30000\"\n"
                                                                         + "                             "
                                                                         + "receive-timeout=\"30000\"/>\n"
                                                                         + "</beans>\n");
    }
}