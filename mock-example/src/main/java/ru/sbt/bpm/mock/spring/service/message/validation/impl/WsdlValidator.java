package ru.sbt.bpm.mock.spring.service.message.validation.impl;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.soapuiutil.wsdlvalidator.WsdlMessageValidator;
import com.soapuiutil.wsdlvalidator.WsdlMessageValidatorException;
import lombok.extern.slf4j.Slf4j;
import ru.sbt.bpm.mock.spring.service.message.validation.MessageValidator;
import ru.sbt.bpm.mock.spring.utils.ExceptionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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
            result = Arrays.asList(wsdlMessageValidator.validateSchemaCompliance(compactXml(xml)));
        } catch (WsdlMessageValidatorException e) {
            result.add(ExceptionUtils.getExceptionStackTrace(e));
        }
        return result;
    }

    private static String compactXml(String xml) {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(xml));
        String line;
        StringWriter stringWriter = new StringWriter();

        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringWriter.append(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        xml = stringWriter.toString();
        log.debug("Parsed xml: " + xml);
        return xml;
    }

    public WsdlProject getWsdlProject() {
        return wsdlMessageValidator.getWsdlProject();
    }
}
