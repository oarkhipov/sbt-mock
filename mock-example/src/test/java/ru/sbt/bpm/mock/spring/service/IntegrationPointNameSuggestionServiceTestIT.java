package ru.sbt.bpm.mock.spring.service;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.MockConfigContainer;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * @author sbt-bochev-as on 18.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ContextConfiguration({"/env/mockapp-servlet-soap-http.xml"})
public class IntegrationPointNameSuggestionServiceTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    IntegrationPointNameSuggestionService suggestionService;

    private Server server;

    @Test
    public void testSuggestSOAPName() throws Exception {
        ru.sbt.bpm.mock.config.entities.System spyne = configContainer.getSystemByName("Spyne");
        List<String> pointList = suggestionService.suggestName(spyne, false);
        assertEquals(pointList, new ArrayList<Object>() {{
            add("say_hello");
        }});
    }

    @Test
    public void testSuggestSOAPNameFiltered() throws Exception {
        ru.sbt.bpm.mock.config.entities.System spyne = configContainer.getSystemByName("Spyne");
        List<String> pointList = suggestionService.suggestName(spyne, true);
        assertTrue(pointList.size()==0);
    }

    @Test
    public void testSuggestJMSName() throws Exception {
        ru.sbt.bpm.mock.config.entities.System spyne = configContainer.getSystemByName("CRM");
        List<String> pointList = suggestionService.suggestName(spyne, false);

        assertTrue(pointList.contains("getReferenceData"));
        assertTrue(pointList.contains("getAvailableExecutors"));
        assertTrue(pointList.contains("getAvailableExecutorsResponse"));
        assertTrue(pointList.contains("sendReferenceData"));
        assertTrue(pointList.contains("confirmationMessage"));
        assertTrue(pointList.contains("sendAdditionalInfo"));
        assertTrue(pointList.contains("getAdditionalInfo"));
        assertTrue(pointList.contains("sendLegalEnquiryExecutionResult"));
        assertTrue(pointList.contains("assessQuality"));
        assertTrue(pointList.contains("cancelLegalEnquiry"));
        assertTrue(pointList.contains("sendLegalEnquiryCreationResult"));
        assertTrue(pointList.contains("sendLegalEnquiryStatus"));
        assertTrue(pointList.contains("createLegalEnquiry"));
    }

    @Test
    public void testSuggestJMSNameFiltered() throws Exception {
        ru.sbt.bpm.mock.config.entities.System spyne = configContainer.getSystemByName("CRM");
        List<String> pointList = suggestionService.suggestName(spyne, true);

        assertTrue(pointList.contains("confirmationMessage"));
        assertTrue(pointList.contains("sendLegalEnquiryCreationResult"));
        assertTrue(pointList.contains("getAvailableExecutorsResponse"));
    }


    @BeforeClass
    @Override
    protected void springTestContextPrepareTestInstance() throws Exception {
        super.springTestContextPrepareTestInstance();
    }

    @BeforeClass
    @Override
    protected void springTestContextBeforeTestClass() throws Exception {
        super.springTestContextBeforeTestClass();
        server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(SoapMessageValidationServiceTestIT.MockServlet.class, "/");
        server.start();
    }

    @AfterClass
    @Override
    protected void springTestContextAfterTestClass() throws Exception {
        super.springTestContextAfterTestClass();
        server.stop();
    }
}