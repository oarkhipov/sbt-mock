/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.soapuiutil.wsdlvalidator;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlProjectFactory;
import com.eviware.soapui.impl.wsdl.mock.*;
import com.eviware.soapui.impl.wsdl.panels.mockoperation.WsdlMockRequestMessageExchange;
import com.eviware.soapui.impl.wsdl.panels.mockoperation.WsdlMockResponseMessageExchange;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlContext;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlValidator;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestRunContext;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStep;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.testsuite.AssertionError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlsoap.schemas.soap.envelope.Envelope;
import ru.sbt.bpm.mock.spring.service.message.validation.mockObjects.MockHttpServletRequest;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

//import org.apache.log4j.Logger;

/**
 * Class which contains methods that can be used to determine message validity against the wsdl.  Schema compliance
 * for example.
 *
 * @author kchan
 */
@Slf4j
public class WsdlMessageValidator {

    final private static String BODY = "Body";
    final private static String ENVELOPE = "Envelope";
    final private static String SERVICE_NAME = "SERVICE NAME";
    final private static String NO_TYPE = "Message type could not be determined";

    private Map<String, WsdlMockOperation> typeOperationMap = new HashMap<String, WsdlMockOperation>();
    private WsdlValidator wsdlValidator;
    private SoapVersion soapVersion;

    public WsdlProject getWsdlProject() {
        return wsdlProject;
    }

    private WsdlProject wsdlProject;

    /**
     * Sets up Exception
     *
     * @param wsdlUrl
     * @throws WsdlMessageValidatorException
     */
    @SuppressWarnings({"unchecked"})
    public WsdlMessageValidator(String wsdlUrl) throws WsdlMessageValidatorException {
        final WsdlContext wsdlContext = new WsdlContext(wsdlUrl);
        wsdlValidator = new WsdlValidator(wsdlContext);
        try {
            buildProject(wsdlUrl);
        } catch (final Exception e) {
            throw new WsdlMessageValidatorException(e);
        }
    }

    private void buildProject(String wsdlUrl) throws Exception {
        wsdlProject = new WsdlProjectFactory().createNew();
        final WsdlInterface[] wsdlInterfaces = WsdlInterfaceFactory.importWsdl(wsdlProject, wsdlUrl, true);
        if (wsdlInterfaces.length > 0) {
            soapVersion = wsdlInterfaces[0].getSoapVersion();
        }

        final WsdlMockService mockService = wsdlProject.addNewMockService(SERVICE_NAME);

        for (final WsdlInterface wsdlInterface : wsdlInterfaces) {
            for (final Operation operation : wsdlInterface.getOperationList()) {
                final WsdlOperation wsdlOperation = wsdlInterface.getOperationByName(operation.getName());

                final QName requestQname = wsdlOperation.getRequestBodyElementQName();
                final QName responseQname = wsdlOperation.getResponseBodyElementQName();
                final WsdlMockOperation mockOper = (WsdlMockOperation) mockService.addNewMockOperation(operation);
                if (requestQname != null) {
                    typeOperationMap.put(requestQname.getLocalPart(), mockOper);
                }
                if (responseQname != null) {
                    typeOperationMap.put(responseQname.getLocalPart(), mockOper);
                }
            }
        }
    }

    /**
     * Checks if the given SOAP message is a Fault.
     * Adapted from the "isSoapFault" method from the SoapUtils class: http://www.soapui.org/xref/com/eviware/soapui/impl/wsdl/support/soap/SoapUtils.html
     * Difference: Instead of '+ "//env:Fault"', did '+ ".//env:Fault"' (an extra dot before //end)
     */
    private boolean isSoapFault(String message) throws XmlException {
        if (message == null) {
            return false;
        }
        if (message.indexOf(":Fault") > 0 || message.indexOf("<Fault") > 0) {
            XmlObject xml = XmlObject.Factory.parse(message);
            XmlObject[] paths = xml.selectPath("declare namespace env='" + soapVersion.getEnvelopeNamespace() + "' .//env:Fault");

            if (paths.length > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates the message against the wsdl.
     *
     * @param message
     * @return
     * @throws WsdlMessageValidatorException
     */
    public String[] validateSchemaCompliance(final String message)
            throws WsdlMessageValidatorException {

        WsdlMockOperation wsdlMockOperation;
        try {

            // parse for body in the response string
            final Envelope envelope = Envelope.Factory.parse(message);
            final Node node = envelope.getDomNode();
            final Node envelopeNode = node.getFirstChild();
            Node bodyNode = envelopeNode.getFirstChild();
            String messageType = null;

            while (BODY.equals(bodyNode.getLocalName()) == false && bodyNode != null) {

                bodyNode = bodyNode.getNextSibling();
            }

            if (ENVELOPE.equals(envelopeNode.getLocalName()) && bodyNode != null && BODY.equals(bodyNode.getLocalName())) {
                final NodeList childNodes = bodyNode.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    final String localName = childNodes.item(i).getLocalName();
                    if (localName != null && !localName.isEmpty()) {
                        messageType = localName;
                    }
                }
            }

            if (StringUtils.isEmpty(messageType)) {
                throw new WsdlMessageValidatorException(NO_TYPE);
            }


            wsdlMockOperation = typeOperationMap.get(messageType);

            if (wsdlMockOperation == null) {
                if (isSoapFault(message)) {

                    Node nodeType = bodyNode.getFirstChild();
                    NodeList faultList = nodeType.getChildNodes();
                    String errorMessage = "";

                    for (int i = 0; i < faultList.getLength(); i++) {
                        Node current = faultList.item(i);
                        if (current.getLocalName().equals("faultstring")) {
                            current = current.getFirstChild();
                            errorMessage = current.getNodeValue();
                            break;
                        }
                    }
                    throw new WsdlMessageValidatorFaultException(errorMessage);
                } else {
                    throw new WsdlMessageValidatorException(NO_TYPE);
                }
            }

            return performValidation(message, wsdlMockOperation, messageType);
        } catch (final WsdlMessageValidatorException e) {
            throw e;
        } catch (final Exception e) {
            throw new WsdlMessageValidatorException(e);
        }
    }

    private String[] performValidation(String message, WsdlMockOperation wsdlMockOperation, String messageType) throws Exception {
        WsdlMockResponse mockResponse = null;
        final String mockOperationName = wsdlMockOperation.getName();
        try {
            AssertionError[] assertionErrors = null;
            WsdlOperation operation = wsdlMockOperation.getOperation();
            boolean assertionPerformed = false;
            try {
                if (messageType.equals(operation.getResponseBodyElementQName().getLocalPart()) ) {
                    //validate response
                    mockResponse = wsdlMockOperation.addNewMockResponse(mockOperationName, true);
                    mockResponse.setResponseContent(message);
                    final WsdlMockResponseMessageExchange messageExchange = new WsdlMockResponseMessageExchange(mockResponse);
                    assertionErrors = wsdlValidator.assertResponse(messageExchange, false);
                    assertionPerformed = true;
                }
            } catch (Exception e) {
                log.debug("SOAP MessageTemplate validation error", e);
            }

            try {
                if (messageType.equals(operation.getRequestBodyElementQName().getLocalPart()) ) {
                    //validate request
                    MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
                    mockHttpServletRequest.setMethod("POST");
                    mockHttpServletRequest.setContent(message.getBytes("UTF-8"));

                    WsdlMockService mockService = wsdlProject.getMockServiceByName("SERVICE NAME");
                    WsdlTestStep testStepByName = wsdlProject.getTestSuiteAt(0).getTestCaseAt(0).getTestStepByName(operation.getName());
                    WsdlTestRunContext wsdlTestRunContext = new WsdlTestRunContext(testStepByName);
                    WsdlMockRunContext runContext = new WsdlMockRunContext(mockService, wsdlTestRunContext);

                    WsdlMockRequest wsdlMockRequest = new WsdlMockRequest(mockHttpServletRequest, null, runContext);
                    wsdlMockRequest.setRequestContent(message);

                    final WsdlMockRequestMessageExchange messageExchange = new WsdlMockRequestMessageExchange(wsdlMockRequest, wsdlMockOperation);
                    assertionErrors = wsdlValidator.assertRequest(messageExchange, false);
                    assertionPerformed = true;
                }
            } catch (Exception e) {
                log.debug("SOAP Request validation error", e);
            }

            if (!assertionPerformed) {
                throw new RuntimeException("No SOAP validation operation performed");
            }

            assert assertionErrors != null;
            final String[] stringErrors = new String[assertionErrors.length];

            for (int i = 0; i < assertionErrors.length; i++) {
                stringErrors[i] = assertionErrors[i].toString();
            }

            return stringErrors;
        } finally {
            if (mockResponse != null) {
                wsdlMockOperation.removeMockResponse(mockResponse);
            }
        }

    }
}
