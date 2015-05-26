package ru.sbt.bpm.mock.config;

import org.junit.Test;
import ru.sbt.bpm.mock.generator.spring.integration.GatewayContextGenerator;

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
    public void testGeneratorSingletonWithFile() throws Exception {
        final String fileExpected = "src/test/resources/xml/MockConfigFiles/MockConfig.xml";
        MockConfigContainer gen1 = MockConfigContainer.getInstance("src/test/resources/xml/MockConfigFiles/MockConfig.xml");
        MockConfigContainer gen2 = MockConfigContainer.getInstance("src/test/resources/xml/MockConfigFiles/MockConfig.xml");
        assertEquals(gen1, gen2);


        assertEquals(fileExpected, gen1.getFilePath());
        assertEquals(fileExpected, gen2.getFilePath());
    }

    @Test
    public void testGeneratorSingletonWithDiffFiles() throws Exception {
        final String fileExpected = "src/test/resources/xml/MockConfigFiles/MockConfig.xml";
        MockConfigContainer gen1 = MockConfigContainer.getInstance("src/test/resources/xml/MockConfigFiles/MockConfig.xml");


        assertEquals(fileExpected, gen1.getFilePath());
    }

    @Test
    public void testGeneratorMockIsNotNull() throws Exception{
        MockConfigContainer configContainer = MockConfigContainer.getInstance("src/test/resources/xml/MockConfigFiles/MockConfig.xml");
        configContainer.init();

        assertNotNull(configContainer.getConfig());
    }

}
