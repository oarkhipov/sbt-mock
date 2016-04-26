package ru.sbt.bpm.mock.spring.service.message;

import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.spring.utils.XpathUtils;

import java.util.NoSuchElementException;

/**
 * @author sbt-bochev-as on 17.03.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
@Service
public class JmsService {
    @Autowired
    private MockConfigContainer configContainer;

    public ru.sbt.bpm.mock.config.entities.System getSystemByPayload(String payload) {
        return getSystemByPayload(payload, "");
    }

    public System getSystemByPayload(String payload, String queue) {
        for (System system : configContainer.getConfig().getSystems().getSystems()) {
            //check queue name if parameter is not empty
            if (queue.isEmpty() || queue.equals(system.getMockIncomeQueue())) {
                String xpath = system.getIntegrationPointSelector().toXpath();
                try {
                    XdmNode xdmItems = (XdmNode) XpathUtils.evaluateXpath(payload, xpath);
                    if (!xdmItems.getNodeName().getLocalName().isEmpty()) {
                        return system;
                    }
                } catch (SaxonApiException e) {
//                e.printStackTrace();
                    //this is not system, that we are looking for
                    log.debug(e.getMessage(), e);
                } catch (ClassCastException e) {
                    //this is not system, that we are looking for
                    log.debug(e.getMessage(), e);
                }
            }
        }
        throw new NoSuchElementException("No JMS system found by payload:\n" + payload);
    }
}
