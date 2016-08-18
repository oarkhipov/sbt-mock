package ru.sbt.bpm.mock.config;

import ru.sbt.bpm.mock.config.MockConfigContainer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.entities.ElementSelector;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.MessageTemplate;
import ru.sbt.bpm.mock.config.entities.MessageTemplates;

import static org.testng.Assert.assertTrue;


/**
 * Created by sbt-hodakovskiy-da on 02.04.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
// Тест для проверки создание Spring Context File
public class MockConfigTest {

    private MockConfigContainer configContainer;

    @Test
    public void testGeneratorSingletonWithFile() throws Exception {
        assertTrue(configContainer.getConfig().getSystems() != null);
        final IntegrationPoint firstIntegrationPoint = configContainer.getConfig().getSystems().getSystems().iterator().next()
                .getIntegrationPoints().getIntegrationPoints().iterator().next();
        final ElementSelector firstElementSelector = firstIntegrationPoint.getXpathValidatorSelector().getElementSelectors().get(0);
        assertTrue(firstElementSelector.getElement() != null);
        assertTrue(firstElementSelector.getNamespace() != null);
    }

    @Test
    public void testCdata() throws Exception {
        MessageTemplates messageTemplates = configContainer.getConfig().getSystems().getSystems().iterator().next()
                .getIntegrationPoints().getIntegrationPoints().iterator().next()
                .getMessageTemplates();
        messageTemplates.getMessageTemplateList().add(new MessageTemplate());
        messageTemplates.getMessageTemplateList().get(0).setDispatcherExpression("SomeValue");
        String xml = configContainer.toXml();
        assertTrue(xml.contains("<![CDATA["));
    }

    @BeforeClass
    public void init() throws Exception {
        configContainer = MockConfigContainer.getInstance("src/test/resources/env/MockConfig.xml");
        configContainer.init();
    }
}
