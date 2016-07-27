package ru.sbt.bpm.mock.config;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.entities.ElementSelector;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;

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
        final IntegrationPoint firstIntegrationPoint = configContainer.getConfig().getSystems().getSystems().get(0).getIntegrationPoints().getIntegrationPoints().get(0);
        final ElementSelector firstElementSelector = firstIntegrationPoint.getXpathValidatorSelector().getElementSelectors().get(0);
        assertTrue(firstElementSelector.getElement() != null);
        assertTrue(firstElementSelector.getNamespace() != null);
    }

    @Test
    public void testCdata() throws Exception {
        configContainer.getConfig().getSystems().getSystems().get(0).getIntegrationPoints().getIntegrationPoints().get(0).setDispatcherExpression("SomeValue");
        String xml = configContainer.toXml();
        assertTrue(xml.contains("<![CDATA["));
    }

    @BeforeClass
    public void init() throws Exception{
        configContainer = MockConfigContainer.getInstance("src/test/resources/env/MockConfig.xml");
        configContainer.init();
    }
}
