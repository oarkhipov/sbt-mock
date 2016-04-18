package ru.sbt.bpm.mock.spring.service;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.iface.Interface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.Protocol;

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


    public List<String> suggestName(System system) {
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
        Interface anInterface = wsdlProject.getInterfaceList().get(0);
        anInterface.getOperationList();
        return null;
    }

    private List<String> suggestJmsName(System system) {
        return null;
    }
}
