package ru.sbt.bpm.mock.spring.controller;

import com.eviware.soapui.impl.wsdl.support.soap.SoapMessageBuilder;
import com.eviware.soapui.impl.wsdl.support.soap.SoapVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.sbt.bpm.mock.spring.bean.ResponseGenerator;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sbt-bochev-as on 14.03.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Controller
public class WebServiceEmulatorController {

    @Autowired
    ResponseGenerator responseGenerator;

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
    String emulateWebService(@PathVariable String webServiceName, @RequestBody String payload) {
        final MockMessage mockMessage = responseGenerator.proceedWsRequest(new MockMessage(payload), webServiceName);
        return mockMessage.getPayload();
    }
}
