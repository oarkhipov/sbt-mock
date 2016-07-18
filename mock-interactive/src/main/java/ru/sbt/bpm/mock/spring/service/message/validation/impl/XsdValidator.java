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
