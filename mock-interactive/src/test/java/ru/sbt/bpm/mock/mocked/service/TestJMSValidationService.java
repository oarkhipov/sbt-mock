package ru.sbt.bpm.mock.mocked.service;

import net.sf.saxon.s9api.SaxonApiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.service.message.validation.JMSValidationService;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.JmsMessageValidationException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author sbt-bochev-as on 09.08.2016.
 *         <p>
 *         Company: SBT - Moscow
 */
@Service
@Qualifier("jmsValidationService")
public class TestJMSValidationService extends JMSValidationService {

    @Override
    public void init(Map validator, System system, String systemName, File systemXsdDirectory, String remoteRootSchema, String remoteSchemaInLowerCase) throws IOException, SAXException {

    }

    @Override
    public boolean assertByXpath(String xml, System system, IntegrationPoint integrationPoint, MessageType messageType) throws SaxonApiException, JmsMessageValidationException {
        return true;
    }
}
