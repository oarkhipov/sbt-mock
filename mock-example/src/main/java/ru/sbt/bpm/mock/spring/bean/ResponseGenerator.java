package ru.sbt.bpm.mock.spring.bean;

import lombok.extern.log4j.Log4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.service.DataFileService;
import ru.sbt.bpm.mock.spring.service.DataService;
import ru.sbt.bpm.mock.spring.service.GroovyService;

import javax.xml.xpath.XPathExpressionException;

/**
 * @author sbt-bochev-as on 23.11.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Log4j
@Component
public class ResponseGenerator {

    @Autowired
    String operationSelector;

    @Autowired
    GroovyService groovyService;

    @Autowired
    DataService dataService;

    @Autowired
    DataFileService dataFileService;

    @Autowired
    MockConfigContainer configContainer;

    public String routeAndGenerate(String systemName, String payload) throws Exception {
        try {
            String integrationPointName = getIntegrationPointName(payload);
            log.debug(integrationPointName);

            Boolean answerRequired = configContainer.getConfig()
                    .getSystems().getSystemByName(systemName)
                    .getIntegrationPoints().getIntegrationPointByName(integrationPointName)
                    .getAnswerRequired();
            if (answerRequired) {
                return generate(systemName, integrationPointName, payload);
            } else {
                return null;
            }
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    private String getIntegrationPointName(String payload) throws XPathExpressionException, SaxonApiException {
        XdmValue value = dataService.evaluateXpath(payload, operationSelector);
        return ((XdmNode) value).getNodeName().getLocalName();
    }

    private String generate(String systemName, String integrationPoint, String payload) throws Exception {
        String message = dataFileService.getCurrentMessage(systemName, integrationPoint);
        String script = dataFileService.getCurrentScript(systemName, integrationPoint);
        return groovyService.compile(payload, message, script);
    }
}
