package ru.sbt.bpm.mock.generator.spring.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.generator.spring.context.bean.*;
import ru.sbt.bpm.mock.spring.service.SpringContextGeneratorService;

import java.util.ArrayList;

import static org.testng.Assert.assertEquals;

/**
 * @author sbt-bochev-as on 13.01.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ContextConfiguration(locations = {"/env/mockapp-servlet.xml"})
public class SpringContextGeneratorServiceServiceTest extends AbstractTestNGSpringContextTests {

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
        Bean bean = new Bean(null, "beanId3", "className3", new ArrayList<ConstructorArg>(), null);
        bean.getConstructorArgs().add(new ConstructorArg("argValue", null));
        beanContainer.getBeans().add(bean);

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
                                                                         + "      <constructor-arg value=\"argValue\"/>\n"
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

    @Test
     public void testToXmlWithChannels() throws Exception {
        BeanContainer beanContainer = new BeanContainer();
        beanContainer.setBeans(new ArrayList<Bean>());
        beanContainer.setChannels(new ArrayList<Channel>());
        beanContainer.getBeans().add(new Bean(null, "beanId1", "className1", null, null));
        beanContainer.getChannels().add(new Channel(null, "channel", new Interceptors(new WireTap("logChannel"))));


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
                                                                         + "   <int:channel id=\"channel\">\n"
                                                                         + "      <interceptors>\n"
                                                                         + "         <wire-tap "
                                                                         + "channel=\"logChannel\"/>\n"
                                                                         + "      </interceptors>\n"
                                                                         + "   </int:channel>\n"
                                                                         + "</beans>\n");
    }

    @Test
    public void testToXmlWithServiceActivator() throws Exception {
        BeanContainer beanContainer = new BeanContainer();
        beanContainer.setBeans(new ArrayList<Bean>());
        beanContainer.setChannels(new ArrayList<Channel>());
        beanContainer.setServiceActivators(new ArrayList<ServiceActivator>());
        Bean bean = new Bean(null, null, "className1", new ArrayList<ConstructorArg>(), null);
        bean.getConstructorArgs().add(new ConstructorArg("JMS", "ru.sbt.bpm.mock.config.enums.Protocol"));
        bean.getConstructorArgs().add(new ConstructorArg("#{mockConnectionOKIInputString}", "java.lang.String"));
        bean.getConstructorArgs().add(new ConstructorArg("#{mockConnectionOKIInputString}", "java.lang.String"));
        beanContainer.getServiceActivators().add(new ServiceActivator("", "input", "output", "method", null, bean));

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
                                                                         + "   <int:service-activator "
                                                                         + "input-channel=\"input\" "
                                                                         + "output-channel=\"output\" "
                                                                         + "method=\"method\">\n"
                                                                         + "      <bean class=\"className1\">\n"
                                                                         + "         <constructor-arg value=\"JMS\" "
                                                                         + "type=\"ru.sbt.bpm.mock.config.enums"
                                                                         + ".Protocol\"/>\n"
                                                                         + "         <constructor-arg "
                                                                         + "value=\"#{mockConnectionOKIInputString}\""
                                                                         + " type=\"java.lang.String\"/>\n"
                                                                         + "         <constructor-arg "
                                                                         + "value=\"#{mockConnectionOKIInputString}\""
                                                                         + " type=\"java.lang.String\"/>\n"
                                                                         + "      </bean>\n"
                                                                         + "   </int:service-activator>\n"
                                                                         + "</beans>\n");
    }

    @Test
    public void testToXmlWithGateway() throws Exception {
        BeanContainer beanContainer = new BeanContainer();
        beanContainer.setGateway(new Gateway(null, "id", "errorChannel", "serviceInterface", "30000", "30000", new Method("name", "in", "out")));

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
                                                                         + "   <int:gateway id=\"id\"\n"
                                                                         + "                "
                                                                         + "error-channel=\"errorChannel\"\n"
                                                                         + "                "
                                                                         + "service-interface=\"serviceInterface\"\n"
                                                                         + "                "
                                                                         + "default-reply-timeout=\"30000\"\n"
                                                                         + "                "
                                                                         + "default-request-timeout=\"30000\">\n"
                                                                         + "      <method name=\"name\" "
                                                                         + "request-channel=\"in\" "
                                                                         + "reply-channel=\"out\"/>\n"
                                                                         + "   </int:gateway>\n"
                                                                         + "</beans>\n");
    }
}