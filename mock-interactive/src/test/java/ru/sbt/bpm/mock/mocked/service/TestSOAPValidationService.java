package ru.sbt.bpm.mock.mocked.service;

import com.eviware.soapui.model.iface.Operation;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.MessageType;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidator;
import ru.sbt.bpm.mock.spring.service.message.validation.SOAPValidationService;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.SoapMessageValidationException;

import java.io.IOException;
import java.util.Map;

/**
 * @author sbt-bochev-as on 09.08.2016.
 *         <p>
 *         Company: SBT - Moscow
 */
@Service
@Qualifier("soapValidationService")
public class TestSOAPValidationService extends SOAPValidationService {
    @Override
    public void init(Map<String, MessageValidator> validator, System system, String systemName, String remoteRootSchema, String localRootSchema, String remoteSchemaInLowerCase) throws IOException {
//        super.init(validator, system, systemName, remoteRootSchema, localRootSchema, remoteSchemaInLowerCase);
    }

    @Override
    public boolean assertByOperation(String xml, System system, IntegrationPoint integrationPoint, MessageType messageType) throws XmlException, SoapMessageValidationException {
        return true;
    }

    @Override
    public String getSoapMessageElementName(String compactXml) throws XmlException {
        return super.getSoapMessageElementName(compactXml);
    }

    @Override
    public String getOperationByElementName(String systemName, String elementName, MessageType messageType) {
        return super.getOperationByElementName(systemName, elementName, messageType);
    }

    @Override
    public String getDefaultRootElement(Operation operation, MessageType messageType) {
        return super.getDefaultRootElement(operation, messageType);
    }
}
