package ru.sbt.bpm.mock.spring.service;

import com.soapuiutil.wsdlvalidator.WsdlMessageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;


/**
 * @author sbt-bochev-as on 25.02.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Service
public class WsdlService {

    @Autowired
    MockConfigContainer configContainer;

    public void validate(String xml, String systemName) throws Exception {

        ru.sbt.bpm.mock.config.entities.System system = configContainer.getSystemByName(systemName);
        WsdlMessageValidator wsdlMessageValidator = new WsdlMessageValidator(system.getRootXSD());
        wsdlMessageValidator.validateSchemaCompliance(xml);


    }
}
