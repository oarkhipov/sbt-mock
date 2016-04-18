package ru.sbt.bpm.mock.spring.service;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.iface.Interface;
import com.eviware.soapui.model.iface.Operation;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.utils.XpathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-bochev-as on 15.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Service
public class IntegrationPointNameSuggestionService {

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    XmlGeneratorService generatorService;

    public List<String> suggestName(System system) throws Exception {
        Protocol protocol = system.getProtocol();
        if (protocol == Protocol.JMS) {
            return suggestJmsName(system);
        }
        if (protocol == Protocol.SOAP) {
            return suggestWsName(system);
        }
        throw new IllegalStateException("No such protocol: " + protocol);
    }

    private List<String> suggestWsName(System system) {
        WsdlProject wsdlProject = configContainer.getWsdlProjectMap().get(system);
        ArrayList<String> operationNames = new ArrayList<String>();
        Interface anInterface = wsdlProject.getInterfaceList().get(0);
        for (Operation operation : anInterface.getOperationList()) {
            operationNames.add(operation.getName());
        }
        return operationNames;
    }

    private List<String> suggestJmsName(System system) throws Exception {
        List<String> integrationPointNames = new ArrayList<String>();
        String fullMessage = generatorService.generateJmsSystemMessage(system.getSystemName());
        String xpath = system.getIntegrationPointSelector().toXpath();
        XdmValue value = XpathUtils.evaluateXpath(fullMessage, xpath);
        for (XdmItem xdmItem : value) {
            String stringValue = ((XdmNode) xdmItem).getNodeName().getLocalName();
            integrationPointNames.add(stringValue);
        }
        return integrationPointNames;
    }
}
