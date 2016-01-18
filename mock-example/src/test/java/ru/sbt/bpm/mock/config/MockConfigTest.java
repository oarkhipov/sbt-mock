package ru.sbt.bpm.mock.config;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by sbt-hodakovskiy-da on 02.04.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
// Тест для проверки создание Spring Context File
public class MockConfigTest {

    @Test
    @Ignore
    public void testGeneratorSingletonWithFile() throws Exception {
        
        MockConfigContainer gen1 = MockConfigContainer.getInstance("src/main/webapp/resources/MockConfigFiles/MockConfig.xml");
        gen1.init();
        assertNotNull(gen1.getConfig().getSystems());
    }
}
