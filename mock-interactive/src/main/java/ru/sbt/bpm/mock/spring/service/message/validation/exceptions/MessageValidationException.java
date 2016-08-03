package ru.sbt.bpm.mock.spring.service.message.validation.exceptions;

/**
 * @author sbt-bochev-as on 26.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class MessageValidationException extends RuntimeException {
    public MessageValidationException(String message) {
        super(message);
    }

    public MessageValidationException() {
    }
}
