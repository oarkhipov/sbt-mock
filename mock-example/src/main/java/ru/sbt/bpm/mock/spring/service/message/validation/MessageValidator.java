package ru.sbt.bpm.mock.spring.service.message.validation;

import java.util.List;

/**
 * @author sbt-bochev-as on 10.03.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public interface MessageValidator {
    /**
     * Validate message
     *
     * @param xml message to validate
     * @return list of errors
     */
    List<String> validate(String xml);
}
