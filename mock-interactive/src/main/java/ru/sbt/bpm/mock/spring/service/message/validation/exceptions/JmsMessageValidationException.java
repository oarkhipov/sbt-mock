package ru.sbt.bpm.mock.spring.service.message.validation.exceptions;

/**
 * @author sbt-bochev-as on 26.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class JmsMessageValidationException extends MessageValidationException {

    private String xpath;
    private String expectedValue;
    private String actualValue;

    public JmsMessageValidationException(String xpath) {
        this.xpath = xpath;
    }

    public JmsMessageValidationException(String xpath, String expectedValue, String actualValue) {
        this.xpath = xpath;
        this.expectedValue = expectedValue;
        this.actualValue = actualValue;
    }

    @Override
    public String getMessage() {
        if (expectedValue == null && actualValue == null) {
            return "JMS Validation Exception: message did not pass xpath [" + super.getMessage() + "] validation!";
        } else {
            return "JMS Validation Exception: message did not pass xpath [" + xpath + "] validation!\n" +
                    "Expected [" + expectedValue + "] but actual is [" + actualValue + "].";
        }
    }
}
