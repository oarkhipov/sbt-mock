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

package ru.sbt.bpm.mock.spring.service.message.validation.impl;

import com.eviware.soapui.impl.support.definition.DefinitionCache;
import com.eviware.soapui.impl.support.definition.InterfaceDefinitionPart;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.iface.Interface;
import com.soapuiutil.wsdlvalidator.WsdlMessageValidator;
import com.soapuiutil.wsdlvalidator.WsdlMessageValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidator;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.MockMessageValidationException;
import ru.sbt.bpm.mock.utils.ExceptionUtils;
import ru.sbt.bpm.mock.utils.XmlUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * @author sbt-bochev-as on 25.02.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
public class WsdlValidator implements MessageValidator {

    private WsdlMessageValidator wsdlMessageValidator;

    public WsdlValidator(String wsdlUrl) throws IOException {
        this(wsdlUrl, null);
    }

    public WsdlValidator(String wsdlUrl, File systemSchemaDirectory) throws IOException {
        if (systemSchemaDirectory != null) {
            FileUtils.cleanDirectory(systemSchemaDirectory);
        }
        try {
            wsdlMessageValidator = new WsdlMessageValidator(wsdlUrl);
            if (wsdlUrl.toLowerCase().startsWith("http://")) {
                if (systemSchemaDirectory == null) {
                    throw new IllegalStateException("No system schema root directory path specified");
                }
                for (Interface anInterface : getWsdlProject().getInterfaceList()) {
                    DefinitionCache definitionCache = anInterface.getDefinitionContext().getInterfaceDefinition().getDefinitionCache();
                    for (InterfaceDefinitionPart interfaceDefinitionPart : definitionCache.getDefinitionParts()) {
                        URL url = new URL(interfaceDefinitionPart.getUrl());
                        String path = url.getPath();
                        String content = interfaceDefinitionPart.getContent();
                        cacheResource(systemSchemaDirectory.getAbsolutePath(), path, content);
                    }
                }
            }
        } catch (WsdlMessageValidatorException e) {
            log.error("Exception in wsdl validator Constructor", e);
        } catch (Exception e) {
            log.error("Exception while wsdl content was caching");
        }
    }

    @Override
    public List<MockMessageValidationException> validate(String xml) {
        List<MockMessageValidationException> result = new ArrayList<MockMessageValidationException>();
        try {
            assert wsdlMessageValidator != null;
            result = wsdlMessageValidator.validateSchemaCompliance(XmlUtils.compactXml(xml));
        } catch (WsdlMessageValidatorException e) {
            result.add(new MockMessageValidationException(ExceptionUtils.getExceptionStackTrace(e)));
        }
        return result;
    }

    public WsdlProject getWsdlProject() {
        return wsdlMessageValidator == null ? null : wsdlMessageValidator.getWsdlProject();
    }

    private void cacheResource(String systemPath, String fileSubPath, String content) throws IOException {
        String filePath = systemPath + fileSubPath;
        File file = new File(filePath);
        FileUtils.forceMkdir(file.getParentFile());
        FileUtils.writeStringToFile(file, content);
    }
}
