package ru.sbt.bpm.mock.spring.controller;

import com.eviware.soapui.impl.wsdl.support.soap.SoapMessageBuilder;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sbt-bochev-as on 14.03.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class WebServiceEmulatorController {

    @RequestMapping(value = "/ws/{webServiceName}", method = RequestMethod.GET, produces = "application/xml")
    public
    @ResponseBody
    String getWsdl(@PathVariable String webServiceName, HttpServletRequest request) {
        if (request.getQueryString().equals("wsdl")) {
            return "some Wsdl of " + webServiceName;
        }
        return SoapMessageBuilder.buildFault("Server", "No such method", SoapVersion.Soap11);
    }

    @RequestMapping(value = "/ws/{webServiceName}", method = RequestMethod.POST, produces = "application/xml")
    public
    @ResponseBody
    String emulateWebService(@PathVariable String webServiceName) {
        return "some answer of " + webServiceName;
//        return SoapMessageBuilder.buildFault("Server", "No such endpoint "+ webServiceName, SoapVersion.Soap11);
    }
}
