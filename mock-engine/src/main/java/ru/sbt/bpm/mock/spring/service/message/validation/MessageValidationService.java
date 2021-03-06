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

package ru.sbt.bpm.mock.spring.service.message.validation;

import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.entities.Systems;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.MessageValidationException;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.MockMessageValidationException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Created by sbt-bochev-as on 13.12.2014.
 *
 * @author sbt-vostrikov-mi
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
@Service
public class MessageValidationService {

    @Autowired
    private DataFileService dataFileService;
    @Autowired
    private MockConfigContainer configContainer;
    @Autowired
    private JMSValidationService jmsMessageValidationService;
    @Autowired
    private SOAPValidationService soapValidationService;

    private Map<String, MessageValidator> validator = new HashMap<String, MessageValidator>();


    @PostConstruct
    protected void init() throws IOException, SAXException {

        /*
         * FileResourceResolver добавлен для корректной работы с XSD с одиннаковыми именами, но в разных директориях
         */
        Systems systemsContainer = configContainer.getConfig().getSystems();
        if (systemsContainer != null) {
            Set<System> systems = systemsContainer.getSystems();
            if (systems != null) {
                for (System system : systems) {
                    initValidator(system);
                }
            }
        }
    }

    /**
     * Init the validator
     *
     * @param system name of system, for which validator need to be init
     * @throws IOException
     * @throws SAXException
     */
    public void initValidator(System system) throws IOException, SAXException {
        String systemName = system.getSystemName();
        File systemXsdDirectory = dataFileService.getSystemXsdDirectoryFile(systemName);
        String remoteRootSchema = system.getRemoteRootSchema();
        String localRootSchema = system.getLocalRootSchema();
        Protocol protocol = system.getProtocol();
        String remoteSchemaInLowerCase = remoteRootSchema.toLowerCase();
        log.info(String.format("Init [%s] validator for system [%s]", protocol, systemName));
        switch (protocol) {
            case JMS:
                jmsMessageValidationService.init(validator, system, systemName, systemXsdDirectory, remoteRootSchema, remoteSchemaInLowerCase);
                break;
            case SOAP:
                soapValidationService.init(validator, system, systemName, remoteRootSchema, localRootSchema, remoteSchemaInLowerCase);
                break;
            default:
                throw new RuntimeException("Unknown system protocol!");
        }
    }


    /**
     * Валидирует xml на соответствие схем
     *
     * @param xml        спец имя xml
     * @param systemName подпапка из директорий xsd и data, по которым будет производится ваидация
     * @return признак валидности
     */
    public List<MockMessageValidationException> validate(String xml, final String systemName) {
        if (validator.get(systemName) == null) {
            return new ArrayList<MockMessageValidationException>(){{
                add(new MockMessageValidationException(String.format("Validator for system [%s] is not initiated!", systemName)));
            }};
        }
        return validator.get(systemName).validate(xml);
    }


    /**
     * Get element by xpath and asserts, that it exists
     *
     * @param xml                  xml to search into
     * @param systemName           name of system, which xml belongs
     * @param integrationPointName name of integration point, which message represents
     * @param messageType          request or response message
     * @return true if one or more elements match the xpath
     * @throws SaxonApiException if xml cannot be parsed
     */
    public boolean assertMessageElementName(String xml, String systemName, String integrationPointName, MessageType messageType) throws SaxonApiException, XmlException, MessageValidationException {
        System system = configContainer.getSystemByName(systemName);
        IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);
        return assertMessageElementName(xml, system, integrationPoint, messageType);
    }

    public boolean assertMessageElementName(String xml, System system, IntegrationPoint integrationPoint, MessageType messageType) throws SaxonApiException, XmlException, MessageValidationException {
        Protocol protocol = system.getProtocol();
        log.info(String.format("Assert xml [%s] for system: [%s]", protocol, system.getSystemName()));
        if (protocol == Protocol.JMS) {
            return jmsMessageValidationService.assertByXpath(xml, system, integrationPoint, messageType);
        }
        if (protocol == Protocol.SOAP) {
            return soapValidationService.assertByOperation(xml, system, integrationPoint, messageType);
        }
        throw new IllegalStateException("Unknown protocol: " + messageType.name());
    }

    /**
     * reInitialize validator by it's system name if xsd data was changed
     *
     * @param systemName name of system to re-init
     * @throws IOException
     * @throws SAXException
     */
    public void reInitValidator(String systemName) throws IOException, SAXException {
        removeValidator(systemName);
        System system = configContainer.getConfig().getSystems().getSystemByName(systemName);
        initValidator(system);
    }

    public void removeValidator(String systemName) {
        validator.remove(systemName);
    }


}
