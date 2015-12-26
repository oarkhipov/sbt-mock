package ru.sbt.bpm.mock.spring.service;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sbt.bpm.mock.config.MockConfigContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author sbt-bochev-as on 26.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Service
public class DataFileService {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    MockConfigContainer configContainer;

    private final String pathBase = "/WEB-INF/";
    private final String dataPath = pathBase + File.separator + "data" + File.separator;
    private final String xsdPath = pathBase + File.separator + "xsd" + File.separator;

    /**
     * Возвращает ресурс, лежащий в pathBase
     *
     * @param name имя файла, относительно WEB-INF
     * @return ресурс
     */
    public Resource getPathBaseResource(String name) {
        return appContext.getResource(pathBase + name);
    }

    public Resource getDataResourse(String name) {
        return appContext.getResource(dataPath + name);
    }

    public Resource getSystemXsdDirectoryResource(String systemName) {
        return appContext.getResource(xsdPath + systemName + File.separator);
    }

    /**
     * Рекурсивный поиск файлов с определённым расширением
     *
     * @param rootDir корневая директория поиска
     * @param ext     расширение файла
     */
    public static List<File> searchFiles(File rootDir, String ext) {
        List<File> files = new ArrayList<File>();
        File[] listFiles = rootDir.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    files.addAll(searchFiles(file, ext));
                } else if (file.getName().toLowerCase().endsWith(ext)) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    /**
     * Нерекурсивный поиск папок в директории
     *
     * @param rootDir корневая директория поиска
     */
    public static List<File> searchDirs(File rootDir) {
        List<File> files = new ArrayList<File>();
        File[] listFiles = rootDir.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    public String getCurrentMessage(String systemName, String integrationPointName) throws IOException {
        return getDataFileContent(systemName, integrationPointName, "message.xml");
    }

    public String getCurrentScript(String systemName, String integrationPointName) throws IOException {
        return getDataFileContent(systemName, integrationPointName, "script.groovy");
    }

    public String getCurrentTest(String systemName, String integrationPointName) throws IOException {
        return getDataFileContent(systemName, integrationPointName, "test.xml");
    }

    public String getDataFileContent(String systemName, String integrationPointName, String fileName) throws IOException {
        Resource resource = getDataResource(systemName, integrationPointName, fileName);
        File file = resource.getFile();
        if (!file.exists()) {
            return "";
        }
        return FileUtils.readFileToString(file, "UTF-8");
    }

    /**
     * Возвращает ресурс, лежащий в pathBase
     *
     * @param systemName       name of system
     * @param integrationPoint name of integraionPoint
     * @param fileName         name of File to get
     * @return ресурс xml
     * @throws IOException
     */
    public Resource getDataResource(String systemName, String integrationPoint, String fileName) throws IOException {
        return appContext.getResource(dataPath + systemName + File.separator + integrationPoint + File.separator +
                fileName);
    }

    public Resource getXsdResource(String systemName, String xsdFile) throws IOException {
        return appContext.getResource(xsdPath + systemName + File.separator + xsdFile);
    }

    /**
     * Move data files to another directory on integration point rename
     *
     * @param systemName              name of system, which integration point belongs
     * @param integrationPointName    old integration point name
     * @param newIntegrationPointName new integration point name
     * @throws IOException
     */
    public void moveDataFiles(String systemName, String integrationPointName, String newIntegrationPointName) throws IOException {
        Resource resource = appContext.getResource(dataPath + File.separator +
                systemName + File.separator +
                integrationPointName);
        Resource newResource = appContext.getResource(dataPath + File.separator +
                systemName + File.separator +
                newIntegrationPointName);
        FileUtils.moveDirectory(resource.getFile(), newResource.getFile());
    }

    /**
     * Delete data directory on integration point delete
     *
     * @param systemName           name of system, which integration point belongs
     * @param integrationPointName name of integration point to delete
     * @throws IOException
     */
    public void deleteDataFiles(String systemName, String integrationPointName) throws IOException {
        Resource resource = appContext.getResource(dataPath + File.separator +
                systemName + File.separator +
                integrationPointName);
        FileUtils.deleteDirectory(resource.getFile());
    }

    public byte[] compressConfiguration() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        File rootDirectory = appContext.getResource(pathBase).getFile();
        File dataDirectory = appContext.getResource(dataPath).getFile();

        //Add data files
        List<File> fileList = searchFiles(dataDirectory, "");
        for (File file : fileList) {
            addZipEntity(zipOutputStream, file.getPath().replace(rootDirectory.getPath() + File.separator, ""), file);
        }
        //Add config
        File configFile = appContext.getResource(configContainer.getFilePath()).getFile();
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

    public void unzipConfiguration(MultipartFile configZip) throws IOException {
        String originalFilename = configZip.getOriginalFilename();
        ZipFile zipFile = new ZipFile(originalFilename);
        Enumeration<?> enumeration = zipFile.entries();
        //TODO check content
        //TODO clear data folder
        while (enumeration.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();

            String name = zipEntry.getName();
            //TODO map folders
            //Directories
            File file = new File(name);
            if (name.endsWith("/")) {
                file.mkdirs();
                continue;
            }

            File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }

            //Extract file
            InputStream inputStream = zipFile.getInputStream(zipEntry);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) >= 0) {
                fileOutputStream.write(bytes, 0, length);
            }
            inputStream.close();
            fileOutputStream.close();

        }
        zipFile.close();
    }
}
