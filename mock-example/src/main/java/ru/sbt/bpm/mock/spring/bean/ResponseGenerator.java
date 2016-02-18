package ru.sbt.bpm.mock.spring.bean;

import lombok.extern.log4j.Log4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.entities.XpathSelector;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.DataService;
import ru.sbt.bpm.mock.spring.service.GroovyService;
import ru.sbt.bpm.mock.spring.utils.ExceptionUtils;

import javax.xml.xpath.XPathExpressionException;
import java.util.NoSuchElementException;

/**
 * @author sbt-bochev-as on 23.11.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Log4j
@Component
public class ResponseGenerator {

    @Autowired
    GroovyService groovyService;

    @Autowired
    DataService dataService;

    @Autowired
    DataFileService dataFileService;

    @Autowired
    MockConfigContainer configContainer;

    public String routeAndGenerate(String payload) throws Exception {
        return routeAndGenerate(payload, "");
    }
    public String routeAndGenerate(String payload, String queue) throws Exception {
        try {
            System system = getSystemName(payload, queue);

            try {
                dataService.validate(payload, system.getSystemName());
            } catch (Exception e){
                String message = "Validate error " + ExceptionUtils.getExceptionStackTrace(e);
                log.error(message);
                return message;
            }

            IntegrationPoint integrationPoint = getIntegrationPoint(system, payload);
            log.debug(integrationPoint.getName());

            Boolean answerRequired = integrationPoint.getAnswerRequired();
            if (answerRequired) {
                return generate(system.getSystemName(), integrationPoint.getName(), payload);
            } else {
                return null;
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    protected System getSystemName(String payload) {
        return getSystemName(payload, "");
    }

    protected System getSystemName(String payload, String queue) {
        for (System system : configContainer.getConfig().getSystems().getSystems()) {
            if (queue.isEmpty() || queue.equals(system.getMockIncomeQueue())) {
                String xpath = system.getIntegrationPointSelector().toXpath();
                try {
                    XdmNode xdmItems = (XdmNode) dataService.evaluateXpath(payload, xpath);
                    if (!xdmItems.getNodeName().getLocalName().isEmpty()) {
                        return system;
                    }
                } catch (SaxonApiException e) {
//                e.printStackTrace();
                    //this is not system, that we are looking for
                }
            }
        }
        throw new NoSuchElementException("No system found by payload:\n" + payload);
    }

    /**
     * Returns integration point by system name and payload
     *
     * @param system system object in configuration
     * @param payload request message
     * @return Integration point
     * @throws XPathExpressionException
     * @throws SaxonApiException
     */
    protected IntegrationPoint getIntegrationPoint(System system, String payload) throws XPathExpressionException, SaxonApiException {
        final XpathSelector integrationPointSelector = system.getIntegrationPointSelector();
        final int xpathSize = integrationPointSelector.getElementSelectors().size();
        final String lastElement = integrationPointSelector.getElementSelectors().get(xpathSize - 1).getElement();
        String integrationPointSelectorXpath = integrationPointSelector.toXpath();
        XdmValue value = dataService.evaluateXpath(payload, integrationPointSelectorXpath);
        String integrationPointName = null;



        if (lastElement.isEmpty()) {
            //return element name
            integrationPointName = ((XdmNode) value).getNodeName().getLocalName();
        }
        else {
            //return element value
            integrationPointName = ((XdmNode) value).getStringValue();
        }
        assert integrationPointName!=null;
        return system.getIntegrationPoints().getIntegrationPointByName(integrationPointName);

    }

    /**
     * Generates response
     *
     * @param systemName name of system in config
     * @param integrationPoint name of integration point of system
     * @param payload request
     * @return generated response
     * @throws Exception
     */
    private String generate(String systemName, String integrationPoint, String payload) throws Exception {
        String message = dataFileService.getCurrentMessage(systemName, integrationPoint);
        String script = dataFileService.getCurrentScript(systemName, integrationPoint);
        return groovyService.execute(payload, message, script);
    }
}
