package ru.sbt.bpm.mock.spring.service;

import generated.springframework.beans.Beans;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.context.generator.service.SpringContextGeneratorService;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author sbt-bochev-as on 28.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Slf4j
@Service
public class ConfigurationService {

    private final
    ApplicationContext applicationContext;

    private final
    DataFileService dataFileService;

    private final
    MockConfigContainer configContainer;

    private final
    XsdAnalysisService xsdAnalysisService;

    private final
    SpringContextGeneratorService springContextGeneratorService;

    private final
    MockSpringContextGeneratorService mockSpringContextGeneratorService;

    @Autowired
    public ConfigurationService(ApplicationContext applicationContext, MockSpringContextGeneratorService mockSpringContextGeneratorService, MockConfigContainer configContainer, SpringContextGeneratorService springContextGeneratorService, DataFileService dataFileService, XsdAnalysisService xsdAnalysisService) {
        this.applicationContext = applicationContext;
        this.mockSpringContextGeneratorService = mockSpringContextGeneratorService;
        this.configContainer = configContainer;
        this.springContextGeneratorService = springContextGeneratorService;
        this.dataFileService = dataFileService;
        this.xsdAnalysisService = xsdAnalysisService;
    }

    @PostConstruct
    private void init() throws JAXBException, IOException {
        //generate spring context
        String configChecksum = configContainer.getConfig().getMainConfig().getConfigChecksum();
        String generatedChecksum = generateChecksum();
        if (configChecksum == null || !generatedChecksum.equals(configChecksum)) {
            reInitSpringContext();
        }
    }

    public byte[] compressConfiguration() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        File rootDirectory = new File(dataFileService.getContextFilePath(""));
        File dataDirectory = dataFileService.getContextDataFile("");
        File xsdDirectory = dataFileService.getSystemXsdDirectoryFile(".").getParentFile();

        //Add data files
        List<File> fileList = dataFileService.searchFiles(dataDirectory, "");
        for (File file : fileList) {
            addZipEntity(zipOutputStream, file.getPath().replace(rootDirectory.getPath() + File.separator, ""), file);
        }
        //Add xsd files
        fileList = dataFileService.searchFiles(xsdDirectory, "");
        for (File file : fileList) {
            addZipEntity(zipOutputStream, file.getPath().replace(rootDirectory.getPath() + File.separator, ""), file);
        }
        //Add config
        File configFile = dataFileService.getConfigFile();
        addZipEntity(zipOutputStream, configFile.getName(), configFile);

        zipOutputStream.finish();
        return byteArrayOutputStream.toByteArray();
    }

    private void addZipEntity(ZipOutputStream zipOutputStream, String destinationPath, File entityFile) throws IOException {
        ZipEntry entry = new ZipEntry(destinationPath);
        zipOutputStream.putNextEntry(entry);

        FileInputStream fileInputStream = new FileInputStream(entityFile.getPath());
        IOUtils.copy(fileInputStream, zipOutputStream);
        IOUtils.closeQuietly(fileInputStream);
    }

    public void unzipConfiguration(File configZip) throws IOException, SaxonApiException, JAXBException {
        dataFileService.clearData();
        dataFileService.unzipFile(configZip, dataFileService.getContextFilePath(""));
        //re-init configuration
        configContainer.reInit();
        xsdAnalysisService.reInit();
        reInitSpringContext();
    }

    public void saveConfig() throws IOException {
        File configFile = dataFileService.getConfigFile();
        FileUtils.write(configFile, configContainer.toXml());
    }

    public void reInitSpringContext() throws JAXBException, IOException {
        mockSpringContextGeneratorService.reInit();
        Beans beans = mockSpringContextGeneratorService.generateContext();
        String xml = springContextGeneratorService.toXml(beans);
        String servletConfigAbsolutePath = dataFileService.getContextFilePath("mockapp-servlet.xml");
        configContainer.getConfig().getMainConfig().setConfigChecksum(generateChecksum());
        saveConfig();
        FileUtils.writeStringToFile(new File(servletConfigAbsolutePath), xml);
        if (applicationContext instanceof XmlWebApplicationContext) {
            ((XmlWebApplicationContext) applicationContext).refresh();
        } else if (applicationContext instanceof GenericWebApplicationContext) {
            ((GenericWebApplicationContext) applicationContext).refresh();
        } else {
            throw new RuntimeException("Unable to refresh Spring context!");
        }
    }

    private String generateChecksum() {
        StringBuilder stringBuilder = new StringBuilder();
        MockConfig config = configContainer.getConfig();
        Set<System> systems = config.getSystems().getSystems();
        for (System system : systems) {
            if (system.getProtocol().equals(Protocol.JMS)) {
                String queueConnectionFactory = system.getQueueConnectionFactory();
                if (queueConnectionFactory != null) {
                    stringBuilder.append(queueConnectionFactory.toLowerCase());
                }

                String mockIncomeQueue = system.getMockIncomeQueue();
                if (mockIncomeQueue != null) {
                    stringBuilder.append(mockIncomeQueue.toLowerCase());
                }

                String mockOutcomeQueue = system.getMockOutcomeQueue();
                if (mockOutcomeQueue != null) {
                    stringBuilder.append(mockOutcomeQueue.toLowerCase());
                }

                String driverOutcomeQueue = system.getDriverOutcomeQueue();
                if (driverOutcomeQueue != null) {
                    stringBuilder.append(driverOutcomeQueue.toLowerCase());
                }

                String driverIncomeQueue = system.getDriverIncomeQueue();
                if (driverIncomeQueue != null) {
                    stringBuilder.append(driverIncomeQueue.toLowerCase());
                }
            }
        }

        String driverTimeout = config.getMainConfig().getDriverTimeout();
        if (driverTimeout != null) {
            stringBuilder.append(driverTimeout);
        }

        return DigestUtils.md5Hex(stringBuilder.toString());
    }
}
