package ru.sbt.bpm.mock.spring.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.File;

/**
 * @author sbt-bochev-as on 26.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class ConfigurationServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    ConfigurationService configurationService;

    @Test
    public void testCompressConfiguration() throws Exception {

        FileUtils.writeByteArrayToFile(new File("target/mockZip.zip"), configurationService.compressConfiguration());
    }

    @Test
    public void testUnzipConfiguration() throws Exception {
        File configFile = new File("target/mockZip.zip");
        configurationService.unzipConfiguration(configFile);
    }
}