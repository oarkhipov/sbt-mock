package ru.sbt.bpm.mock.spring.service.message.validation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author sbt-bochev-as on 10.03.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class ValidationUtils {
    public static String getSolidErrorMessage(List<String> errors) {
        Collection errorCollection = CollectionUtils.collect(errors, new Transformer() {
            @Override
            public Object transform(Object o) {
                String error = (String) o;
                String[] errorLines = error.split("\\r?\\n");
                if (errorLines.length > 10) {
                    errorLines = Arrays.copyOf(errorLines, 10);
                }
                return StringUtils.join(errorLines, "\n");
            }
        });
        final String errorString = StringUtils.join(errorCollection.iterator(), ",\n");
        return "Validation errors list:\n" + errorString;
    }
}
