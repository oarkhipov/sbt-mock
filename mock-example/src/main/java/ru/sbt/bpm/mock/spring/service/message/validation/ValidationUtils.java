package ru.sbt.bpm.mock.spring.service.message.validation;

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author sbt-bochev-as on 10.03.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class ValidationUtils {
    public static String getSolidErrorMessage(List<String> errors) {
        final String errorString = StringUtils.join(errors, ",\n");
        return "Validation errors list:\n" + errorString;
    }
}
