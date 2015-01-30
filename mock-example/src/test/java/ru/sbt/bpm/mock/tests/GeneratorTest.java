package ru.sbt.bpm.mock.tests;

import org.junit.Test;
import ru.sbt.bpm.mock.sigeneator.GenerateMockAppServlet;

import java.io.FileReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
// тест генератора контекста на чтение
public class GeneratorTest {

//    @Test
//    public void testGeneratorSingletonWithOutFile() throws Exception {
//        GenerateMockAppServlet gen1 = GenerateMockAppServlet.getInstance();
//        GenerateMockAppServlet gen2 = GenerateMockAppServlet.getInstance();
//        assertEquals(gen1, gen2);
//        System.out.println(gen1.getaFilePath() + " || " + gen2.getaFilePath());
//        assertEquals(gen1.getaFilePath(), gen2.getaFilePath());
//        System.out.println("================================");
//    }

    @Test
    public void testGeneratorSingletonWithFile() throws Exception {
        final String fileExpected = "/xml/MockConfigFiles/MockConfig1.xml";
        GenerateMockAppServlet gen1 = GenerateMockAppServlet.getInstance("/xml/MockConfigFiles/MockConfig1.xml");
        GenerateMockAppServlet gen2 = GenerateMockAppServlet.getInstance("/xml/MockConfigFiles/MockConfig1.xml");
        assertEquals(gen1, gen2);

        System.out.println(gen1.getaFilePath() + " || " + gen2.getaFilePath());

        assertEquals(fileExpected, gen1.getaFilePath());
        assertEquals(fileExpected, gen2.getaFilePath());
        System.out.println("================================");
    }

    @Test
    public void testGeneratorSingletonWithDiffFiles() throws Exception {
        final String fileExpected = "/xml/MockConfigFiles/MockConfig1.xml";
        GenerateMockAppServlet gen1 = GenerateMockAppServlet.getInstance("/xml/MockConfigFiles/MockConfig1.xml");

        System.out.println(gen1.getaFilePath());

        assertEquals(fileExpected, gen1.getaFilePath());

        GenerateMockAppServlet gen2 = GenerateMockAppServlet.getInstance("/xml/MockConfigFiles/MockConfig2.xml");

        System.out.println(gen2.getaFilePath());
        assertEquals(fileExpected, gen2.getaFilePath());
        System.out.println("================================");
    }

    @Test
    public void testGeneratorMockIsNotNull() throws Exception{
        final String file = this.getClass().getClassLoader().getResource("").getPath() + "\\..\\..\\src\\main\\webapp\\WEB-INF\\MockConfigFiles\\MockConfig1.xml";
        GenerateMockAppServlet gen1 = GenerateMockAppServlet.getInstance(file);
        gen1.setaFilePath(file);
        gen1.init();

        assertNotNull(gen1.getaMockConfig());
    }
}
