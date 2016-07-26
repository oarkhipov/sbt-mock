package ru.sbt.bpm.mock.spring.service;

import generated.springframework.beans.Beans;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.XmlWebApplicationContext;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.context.generator.service.SpringContextGeneratorService;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author sbt-bochev-as on 28.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Service
public class ConfigurationService {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    DataFileService dataFileService;

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    XsdAnalysisService xsdAnalysisService;

    @Autowired
    SpringContextGeneratorService springContextGeneratorService;

    @Autowired
    MockSpringContextGeneratorService mockSpringContextGeneratorService;

    @PostConstruct
    private void init() throws JAXBException, IOException {
        //generate spring context
//        reInitSpringContext();
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
        configContainer.sortConfig();
        File configFile = dataFileService.getConfigFile();
        FileUtils.write(configFile, configContainer.toXml());
    }

    public void reInitSpringContext() throws JAXBException, IOException {
        mockSpringContextGeneratorService.reInit();
        Beans beans = mockSpringContextGeneratorService.generateContext();
        String xml = springContextGeneratorService.toXml(beans);
        String servletConfigAbsolutePath = dataFileService.getContextFilePath("mockapp-servlet.xml");
        FileUtils.writeStringToFile(new File(servletConfigAbsolutePath), xml);
        ((XmlWebApplicationContext) applicationContext).refresh();
    }
}
