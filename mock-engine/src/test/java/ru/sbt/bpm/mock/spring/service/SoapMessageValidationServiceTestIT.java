package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidationService;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * @author sbt-bochev-as on 31.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-soap-http.xml"})
public class SoapMessageValidationServiceTestIT extends AbstractSOAPSpyneVirtualHttpServerTransactionalTestNGSpringContextTests {

    @Autowired
    MessageValidationService messageValidationService;

    @Autowired
    XmlGeneratorService generatorService;

    @Test
    public void testInitHttpValidator() throws Exception {
        List<String> validationErrors = messageValidationService.validate(SIMPLE_RESPONSE, "Spyne");
        assertTrue(validationErrors.size() == 0, validationErrors.toString());


        validationErrors = messageValidationService.validate(SIMPLE_REQUEST, "Spyne");
        assertTrue(validationErrors.size() == 0, validationErrors.toString());
    }

    @Test
    public void testInitHttpValidatorWithError() throws Exception {
        String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spy=\"spyne.examples.hello\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <spy:say_helloResponse>\n" +
                "         <!--Optional:-->\n" +
                "           <spy:badTag>Bad Value</spy:badTag>" +
                "         <spy:say_helloResult>\n" +
                "            <!--Zero or more repetitions:-->\n" +
                "            <spy:string>test</spy:string>\n" +
                "         </spy:say_helloResult>\n" +
                "      </spy:say_helloResponse>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        assertTrue(!xml.isEmpty());
        List<String> validationErrors = messageValidationService.validate(xml, "Spyne");
        assertFalse(validationErrors.size() == 0, validationErrors.toString());

        xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spy=\"spyne.examples.hello\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <spy:say_hello>" +
                "        <badTag>test</badTag>" +
                "      </spy:say_hello>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        assertTrue(!xml.isEmpty());
        validationErrors = messageValidationService.validate(xml, "Spyne");
        assertFalse(validationErrors.size() == 0, validationErrors.toString());

    }

    @Test
    public void testInitHttpValidatorWithGenerator() throws Exception {
        String xml = generatorService.generate("Spyne", "say_hello", MessageType.RQ, true).replace("?", "test");
        assertTrue(!xml.isEmpty());
        List<String> validationErrors = messageValidationService.validate(xml, "Spyne");
        assertTrue(validationErrors.size() == 0, validationErrors.toString());

        xml = generatorService.generate("Spyne", "say_hello", MessageType.RS, true).replace("?", "test");
        assertTrue(!xml.isEmpty());
        validationErrors = messageValidationService.validate(xml, "Spyne");
        assertTrue(validationErrors.size() == 0, validationErrors.toString());
    }

    @Test
    public void testInitHttpValidatorWithGeneratorErrors() throws Exception {
        String xml = generatorService.generate("Spyne", "say_hello", MessageType.RQ, true).replace("<spy:say_hello/>", "<spy:say_hello><badTag>test</badTag></spy:say_hello>");
        assertTrue(!xml.isEmpty());
        List<String> validationErrors = messageValidationService.validate(xml, "Spyne");
        assertFalse(validationErrors.size() == 0, validationErrors.toString());

        xml = generatorService.generate("Spyne", "say_hello", MessageType.RS, true).replace("?", "<badTag>test</badTag>");
        assertTrue(!xml.isEmpty());
        validationErrors = messageValidationService.validate(xml, "Spyne");
        assertFalse(validationErrors.size() == 0, validationErrors.toString());
    }

}