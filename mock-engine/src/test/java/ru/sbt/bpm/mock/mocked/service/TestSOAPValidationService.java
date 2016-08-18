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
