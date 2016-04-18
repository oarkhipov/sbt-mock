package ru.sbt.bpm.mock.spring.service;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockDispatcher;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.service.message.validation.mockObjects.MockHttpServletResponse;

import java.io.File;
import java.net.URL;

import static org.testng.Assert.assertTrue;

/**
 * @author sbt-bochev-as on 02.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class XmlGeneratorServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    XmlGeneratorService xmlGeneratorService;

    @Test
    public void testGenerate() throws Exception {
        String message = xmlGeneratorService.generate("CRM", "sendAdditionalInfo", MessageType.RS, true);
        assertTrue(message.contains("Envelope"));
        assertTrue(message.contains("sendAdditionalInfo"));
    }

    @Test
    public void testWsdlGenerate() throws Exception {
        URL resource = this.getClass().getClassLoader().getResource("");
        assert resource != null;
        String baseDir = resource.getFile();
        assert baseDir != null;
        final String wsdlUrl = baseDir + File.separator + "wsdl" + File.separator + "spyne.wsdl";
        WsdlProject wsdlProject = new WsdlProject();
        WsdlInterfaceFactory.importWsdl(wsdlProject, wsdlUrl, true);
        WsdlInterface wsdlInterface = (WsdlInterface) wsdlProject.getInterfaceList().get(0);
        String request = wsdlInterface.getOperationByName("say_hello").createRequest(true);
        assertTrue(request.contains("Body"));
        assertTrue(request.contains("say_hello"));

        wsdlProject.addNewMockService("mock");
        WsdlMockService wsdlMockService = wsdlProject.getMockServiceAt(0);
        wsdlMockService.addNewMockOperation(wsdlInterface.getOperationAt(0));
        wsdlMockService.setPort(80);
        wsdlMockService.setPath("/");
        wsdlMockService.setHost("someHost");

        WsdlMockDispatcher wsdlMockDispatcher = new WsdlMockDispatcher(wsdlMockService, null);
        MockHttpServletResponse response = new MockHttpServletResponse();
        wsdlMockDispatcher.printWsdl(response);
        String responseString = response.getContentAsString();
        assertTrue(responseString.contains("address location=\"http://someHost:80/\""));
        assertTrue(responseString.contains("wsdl:operation>"));
        assertTrue(responseString.contains("wsdl:definitions"));
    }
}