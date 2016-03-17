package ru.sbt.bpm.mock.config;

import org.junit.Test;
import ru.sbt.bpm.mock.config.entities.ElementSelector;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;

import static org.junit.Assert.assertTrue;

/**
 * Created by sbt-hodakovskiy-da on 02.04.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
// Тест для проверки создание Spring Context File
public class MockConfigTest {

    @Test
    public void testGeneratorSingletonWithFile() throws Exception {

        MockConfigContainer configContainer = MockConfigContainer.getInstance("src/main/webapp/WEB-INF/MockConfig.xml");
        configContainer.init();
        assertTrue(configContainer.getConfig().getSystems() != null);
        final IntegrationPoint firstIntegrationPoint = configContainer.getConfig().getSystems().getSystems().get(0).getIntegrationPoints().getIntegrationPoints().get(0);
        final ElementSelector firstElementSelector = firstIntegrationPoint.getRequestXpathValidatorSelector().getElementSelectors().get(0);
        assertTrue(firstElementSelector.getElement() != null);
        assertTrue(firstElementSelector.getNamespace() != null);
    }
}
