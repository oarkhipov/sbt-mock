package ru.sbt.bpm.mock.spring.service;

import org.custommonkey.xmlunit.Diff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;
import ru.sbt.bpm.mock.spring.utils.XmlUtils;

import static org.testng.Assert.assertTrue;

/**
 * @author sbt-bochev-as on 14.07.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ContextConfiguration(value = {"classpath:/env/mockapp-servlet-soap-http.xml", "classpath:/contextConfigs/logging-config.xml"})
public class MessageSendingServiceTest extends AbstractSOAPSpyneVirtualHttpServerTransactionalTestNGSpringContextTests {

    @Autowired
    MessageSendingService messageSendingService;

    @Autowired
    MockConfigContainer configContainer;

    @Test
    public void testSendWs() throws Exception {
        MockMessage message = new MockMessage(SIMPLE_REQUEST);
        ru.sbt.bpm.mock.config.entities.System system = configContainer.getSystemByName("Spyne");
        system.setDriverWebServiceEndpoint("http://localhost:8080/path/to/dir/spyneResponse.xml");
        message.setSystem(system);
        message.setIntegrationPoint(system.getIntegrationPoints().getIntegrationPointByName("say_hello"));
        String response = messageSendingService.sendWs(message);
        Diff diff = new Diff(XmlUtils.compactXml(SIMPLE_RESPONSE), XmlUtils.compactXml(response));
        assertTrue(diff.identical(), diff.toString());
    }
}