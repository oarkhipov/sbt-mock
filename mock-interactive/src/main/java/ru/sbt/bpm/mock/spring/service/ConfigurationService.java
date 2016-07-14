package ru.sbt.bpm.mock.spring.service;

import generated.springframework.beans.Beans;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.context.generator.service.SpringContextGeneratorService;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
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


    public byte[] compressConfiguration() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        File rootDirectory = new File(dataFileService.getPathBaseFilePath(""));
        File dataDirectory = dataFileService.getDataResource("").getFile();
        File xsdDirectory = dataFileService.getSystemXsdDirectoryResource(".").getFile();

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
        File configFile = dataFileService.getConfigResource().getFile();
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

    public void unzipConfiguration(File configZip) throws IOException, SaxonApiException {
        dataFileService.clearData();
        unzipFile(configZip, dataFileService.getPathBaseFilePath(""));
        //re-init configuration
        configContainer.reInit();
        xsdAnalysisService.reInit();

    }

    public void unzipFile(File zipFile, String placeToUnzip) throws IOException {
        ZipFile zipFileArch = new ZipFile(zipFile);
        Enumeration<?> enumeration = zipFileArch.entries();

        while (enumeration.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();

            String name = zipEntry.getName();

            File file = new File(placeToUnzip + File.separator + name);

            File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            if (file.isDirectory()) {
                file.mkdir();
            } else {
                //Extract file
                InputStream inputStream = zipFileArch.getInputStream(zipEntry);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = inputStream.read(bytes)) >= 0) {
                    fileOutputStream.write(bytes, 0, length);
                }
                inputStream.close();
                fileOutputStream.close();
            }
        }
        zipFileArch.close();
    }

    public void saveConfig() throws IOException {
        File configFile = dataFileService.getConfigResource().getFile();
        FileUtils.write(configFile, configContainer.toXml());
    }

    public void reInitSpringContext() throws JAXBException, IOException {
        mockSpringContextGeneratorService.reInit();
        Beans beans = mockSpringContextGeneratorService.generateContext();
        String xml = springContextGeneratorService.toXml(beans);
        //TODO fix dangerous situation. when servlet config generated broken
        String servletConfigAbsolutePath = dataFileService.getPathBaseFilePath("mockapp-servlet.xml");
        FileUtils.writeStringToFile(new File(servletConfigAbsolutePath), xml);
        applicationContext = new FileSystemXmlApplicationContext(servletConfigAbsolutePath);
    }
}
