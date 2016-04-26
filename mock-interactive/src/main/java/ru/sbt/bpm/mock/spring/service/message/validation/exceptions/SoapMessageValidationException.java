package ru.sbt.bpm.mock.spring.service.message.validation.exceptions;

/**
 * @author sbt-bochev-as on 26.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class SoapMessageValidationException extends MessageValidationException {


    private final String expectedElementName;
    private final String actualElementName;

    public SoapMessageValidationException(String expectedElementName, String actualElementName) {
        this.expectedElementName = expectedElementName;
        this.actualElementName = actualElementName;
    }

    @Override
    public String getMessage() {
        return "SOAP Validation exception: element local name expected [" + expectedElementName + "] but actual is [" + actualElementName + "]!";
    }
}
