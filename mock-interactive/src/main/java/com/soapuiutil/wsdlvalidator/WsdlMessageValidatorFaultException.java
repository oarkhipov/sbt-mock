package com.soapuiutil.wsdlvalidator;

/**
 * Exception class for handling wsdl message faults
 * Extends the WsdlMessageValidatorException class
 *
 * @author Oscar Bachtiar
 */

public class WsdlMessageValidatorFaultException extends WsdlMessageValidatorException {

    private static final long serialVersionUID = 1L;

    public WsdlMessageValidatorFaultException(final String message) {
        super(message);
    }

    public WsdlMessageValidatorFaultException(final Exception e) {
        super(e);
    }
}
