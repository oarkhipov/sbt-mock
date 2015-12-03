package ru.sbt.bpm.mock.spring.bean;

import lombok.extern.log4j.Log4j;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.sbt.bpm.mock.spring.service.DataService;
import ru.sbt.bpm.mock.spring.service.GroovyService;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.*;
import java.util.Map;

/**
 * @author sbt-bochev-as on 23.11.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Log4j
@Component
public class ResponseGenerator {

    @Autowired
    Map<String, String> sbrfNamespaceMap;

    @Autowired
    String operationSelector;

    @Autowired
    GroovyService groovyService;

    @Autowired
    DataService dataService;

    public String routeAndGenerate(String systemName, @Payload String payload) throws Exception {
        String integrationPointName = getIntegrationPointName(payload);
        log.debug(integrationPointName);
        return generate(systemName, integrationPointName, payload);
    }

    private String getIntegrationPointName(String payload) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        NamespaceContext context = (NamespaceContext) new SimpleNamespaceContext(sbrfNamespaceMap);
        xPath.setNamespaceContext(context);
        XPathExpression expression = xPath.compile(operationSelector);
        return (String) expression.evaluate(payload, XPathConstants.STRING);
    }

    private String generate(String systemName, String integrationPoint, String payload) throws Exception {
        String message = dataService.getCurrentMessage(systemName, integrationPoint);
        String script = dataService.getCurrentScript(systemName, integrationPoint);
        return groovyService.compile(payload, message, script);
    }

}
