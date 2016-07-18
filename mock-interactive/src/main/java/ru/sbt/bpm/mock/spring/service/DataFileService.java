package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author sbt-bochev-as on 26.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
@Service
public class DataFileService {

    private final String contextDir = "/WEB-INF/";
    private final String dataPath = "data" + File.separator;
    private final String xsdPath = "xsd" + File.separator;

    @Autowired
    MockConfigContainer configContainer;

    /**
     * Возвращает ресурс, лежащий в contextDir
     *
     * @param relativePath имя файла, относительно WEB-INF
     * @return ресурс
     */
    public String getContextFilePath(String relativePath) throws IOException {
        return configContainer.getBasePath() + contextDir + relativePath;
    }

    public File getContextFile(String relativePath) {
        try {
            return new File(getContextFilePath(relativePath));
        } catch (IOException e) {
            throw new RuntimeException("Context file not found!", e);
        }
    }

    public File getContextDataFile(String relativePath) {
        return getContextFile(dataPath + relativePath);
    }

    public File getSystemXsdDirectoryFile(String systemName) {
        String relativePath = xsdPath + systemName + File.separator;
        File file = getContextFile(relativePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getConfigFile() {
        return new File(configContainer.getBasePath() + configContainer.getFilePath());
    }

    /**
     * Рекурсивный поиск файлов с определённым расширением
     *
     * @param rootDir корневая директория поиска
     * @param ext     расширение файла
     */
    public List<File> searchFiles(File rootDir, String ext) {
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
    public List<File> searchDirs(File rootDir) {
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
        File file = getContextDataFile(systemName, integrationPointName, fileName);
        log.debug("Getting file: " + file.getAbsolutePath());
        if (!file.exists()) {
            return "";
        }
        return FileUtils.readFileToString(file, "UTF-8");
    }

    protected void clearData() throws IOException {
        //data clear
        File rootDir = getContextDataFile("");
        List<File> files = searchFiles(rootDir, "");
        for (File file : files) {
            FileUtils.deleteQuietly(file);
        }
        List<File> dirs = searchDirs(rootDir);
        for (File dir : dirs) {
            FileUtils.deleteQuietly(dir);
        }

        //xsd clear
        rootDir = getSystemXsdDirectoryFile(".");
        files = searchFiles(rootDir, "");
        for (File file : files) {
            FileUtils.deleteQuietly(file);
        }
        dirs = searchDirs(rootDir);
        for (File dir : dirs) {
            FileUtils.deleteQuietly(dir);
        }
    }

    /**
     * Возвращает ресурс, лежащий в contextDir
     *
     * @param systemName       relativePath of system
     * @param integrationPoint relativePath of integrationPoint
     * @param fileName         relativePath of File to get
     * @return ресурс xml
     * @throws IOException
     */
    public File getContextDataFile(String systemName, String integrationPoint, String fileName) throws IOException {
        return getContextDataFile(systemName + File.separator + integrationPoint + File.separator +
                fileName);
    }

    public File getXsdFile(String systemName, String xsdFile) throws IOException {
        if (xsdFile.toLowerCase().startsWith("http")) {
            //TODO test file from url
            return new File(xsdFile);
        } else
            return getContextFile(xsdPath + systemName + File.separator + xsdFile);
    }

    /**
     * Move data files to another directory on integration point rename
     *
     * @param systemName              relativePath of system, which integration point belongs
     * @param integrationPointName    old integration point relativePath
     * @param newIntegrationPointName new integration point relativePath
     * @throws IOException
     */
    public void moveDataFiles(String systemName, String integrationPointName, String newIntegrationPointName) throws IOException {
        File oldDirectory = getContextDataFile(
                systemName + File.separator +
                        integrationPointName);
        File newDirectory = getContextDataFile(
                systemName + File.separator +
                        newIntegrationPointName);
        FileUtils.moveDirectory(oldDirectory, newDirectory);
    }

    /**
     * Delete data directory on integration point delete
     *
     * @param systemName           relativePath of system, which integration point belongs
     * @param integrationPointName relativePath of integration point to delete
     * @throws IOException
     */
    public void deleteDataFiles(String systemName, String integrationPointName) throws IOException {
        File dataDirectory = getContextDataFile(
                systemName + File.separator +
                        integrationPointName);
        FileUtils.deleteDirectory(dataDirectory);
    }

    /**
     * Move data and xsd files to another directory on system rename
     *
     * @param systemName    old system relativePath
     * @param newSystemName new system relativePath
     * @throws IOException
     */
    public void moveSystemDir(String systemName, String newSystemName) throws IOException {
        //Data
        File systemDirectory = getContextDataFile(systemName);
        File newSystemDirectory = getContextDataFile(newSystemName);
        FileUtils.moveDirectory(systemDirectory, newSystemDirectory);
        //Xsd
        File xsdResource = getContextFile(xsdPath + systemName);
        File newXsdResource = getContextFile(xsdPath + newSystemName);
        FileUtils.moveDirectory(xsdResource, newXsdResource);
    }

    /**
     * Delete data and xsd directory on system delete
     *
     * @param systemName relativePath of system, to delete
     * @throws IOException
     */
    public void deleteSystemDir(String systemName) throws IOException {
        File xsdDirectory = getContextFile(xsdPath + systemName);
        File dataDirectory = getContextDataFile(systemName);
        FileUtils.deleteDirectory(xsdDirectory);
        FileUtils.deleteDirectory(dataDirectory);
    }

    public void uploadSchema(String systemName, File xsdZipFile) throws IOException {
        File xsdDirectoryFile = getContextFile(xsdPath + systemName);
        if (xsdDirectoryFile.delete()) {
            xsdDirectoryFile.deleteOnExit();
        }
        unzipFile(xsdZipFile, xsdDirectoryFile.getAbsolutePath());
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
}
