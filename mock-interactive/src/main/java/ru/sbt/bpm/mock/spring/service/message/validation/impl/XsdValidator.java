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

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidator;
import ru.sbt.bpm.mock.spring.utils.FileResourceResolver;
import ru.sbt.bpm.mock.spring.utils.WebResourceResolver;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author sbt-bochev-as on 10.03.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
public class XsdValidator implements MessageValidator {

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    List<String> errors = new LinkedList<String>();
    private final Validator validator;

    public XsdValidator(List<File> xsdFiles) throws SAXException {
        StreamSource sources[] = new StreamSource[xsdFiles.size()];

//            Add Xsd files to source
        for (int i = 0; i < xsdFiles.size(); i++) {
//            System.out.println(xsdFiles.get(i).getAbsolutePath());
            sources[i] = new StreamSource(xsdFiles.get(i));
        }
        factory.setResourceResolver(new FileResourceResolver());
        Schema schema = factory.newSchema(sources);
        validator = schema.newValidator();
        setValidatorErrorHandler();
    }

    public XsdValidator(String schemaUri, String localRootSchemaDir) throws IOException, SAXException {
        factory.setResourceResolver(new WebResourceResolver(schemaUri, localRootSchemaDir));
        Schema schema = factory.newSchema(new URL(schemaUri));
        validator = schema.newValidator();
        setValidatorErrorHandler();
    }

    @Override
    public synchronized List<String> validate(String xml) {
        errors.clear();
        List<String> result = new ArrayList<String>();
        InputStream stream = null;
        try {
            stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            validator.validate(new StreamSource(stream));
        } catch (UnsupportedEncodingException e) {
            String message = "Unsupported Encoding UTF-8";
            result.add(message);
            log.error(message, e);
        } catch (IOException e) {
            String message = "IOException while validating message";
            result.add(message);
            log.error(message, e);
        } catch (SAXException e) {
            String message = "SAXException while validating message";
            result.add(message);
            log.error(message, e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return errors.size() > 0 ? errors : result;
    }

    private void setValidatorErrorHandler() {
        validator.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
//                handle(exception);
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                handle(exception);
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                handle(exception);
            }

            private void handle(SAXParseException exception) throws SAXException {
                errors.add("error [" + exception.getLineNumber() + ":" + exception.getColumnNumber() + "]: " + exception.getMessage());
            }
        });
    }
}
