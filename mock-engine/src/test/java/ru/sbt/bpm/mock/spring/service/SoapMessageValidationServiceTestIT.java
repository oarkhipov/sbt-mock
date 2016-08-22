/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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