package com.soapuiutil.wsdlvalidator;

/**
 * Exception class for the wsdl message validator.
 *
 * @author kchan
 */
public class WsdlMessageValidatorException extends Exception {

    private static final long serialVersionUID = 1L;

    public WsdlMessageValidatorException(final String message) {
        super(message);
    }

    public WsdlMessageValidatorException(final Exception e) {
        super(e);
    }

}
