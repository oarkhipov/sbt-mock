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

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.soapuiutil.wsdlvalidator.WsdlMessageValidator;
import com.soapuiutil.wsdlvalidator.WsdlMessageValidatorException;
import lombok.extern.slf4j.Slf4j;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidator;
import ru.sbt.bpm.mock.spring.utils.ExceptionUtils;
import ru.sbt.bpm.mock.spring.utils.XmlUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author sbt-bochev-as on 25.02.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
public class WsdlValidator implements MessageValidator {

    private WsdlMessageValidator wsdlMessageValidator;

    public WsdlValidator(String wsdlUrl) {
        try {
            wsdlMessageValidator = new WsdlMessageValidator(wsdlUrl);
        } catch (WsdlMessageValidatorException e) {
            log.error("Exception in wsdl validator Constructor", e);
        }
    }

    @Override
    public List<String> validate(String xml) {
        List<String> result = new ArrayList<String>();
        try {
            assert wsdlMessageValidator != null;
            result = Arrays.asList(wsdlMessageValidator.validateSchemaCompliance(XmlUtils.compactXml(xml)));
        } catch (WsdlMessageValidatorException e) {
            result.add(ExceptionUtils.getExceptionStackTrace(e));
        }
        return result;
    }

    public WsdlProject getWsdlProject() {
        return wsdlMessageValidator.getWsdlProject();
    }
}
