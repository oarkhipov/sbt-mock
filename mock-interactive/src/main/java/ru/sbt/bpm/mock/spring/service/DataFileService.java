package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-bochev-as on 26.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
@Service
public class DataFileService {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    MockConfigContainer configContainer;

    @Autowired
    ConfigurationService configurationService;

    private final String pathBase = "/WEB-INF/";
    private final String dataPath = pathBase + "data" + File.separator;
    private final String xsdPath = pathBase + "xsd" + File.separator;

    /**
     * Возвращает ресурс, лежащий в pathBase
     *
     * @param name имя файла, относительно WEB-INF
     * @return ресурс
     */
    public String getPathBaseFilePath(String name) throws IOException {
        Resource resource = appContext.getResource(pathBase);
        File file = resource.getFile();
        return file.getAbsolutePath() + File.separator + name;
    }

    public Resource getDataResource(String name) {
        return appContext.getResource(dataPath + name);
    }

    public Resource getSystemXsdDirectoryResource(String systemName) {
        String relativePath = xsdPath + systemName + File.separator;
        Resource resource = appContext.getResource(relativePath);
        if (!resource.exists()) {
            try {
                String basePath = appContext.getResource("").getFile().getPath();
                new File(basePath + relativePath).mkdirs();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resource;
    }

    public Resource getConfigResource() {
        return appContext.getResource(configContainer.getFilePath());
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
        Resource resource = getDataResource(systemName, integrationPointName, fileName);
        File file = resource.getFile();
        log.debug("Getting file: " + file.getAbsolutePath());
        if (!file.exists()) {
            return "";
        }
        return FileUtils.readFileToString(file, "UTF-8");
    }

    protected void clearData() throws IOException {
        //data clear
        File rootDir = getDataResource("").getFile();
        List<File> files = searchFiles(rootDir, "");
        for (File file : files) {
            FileUtils.deleteQuietly(file);
        }
        List<File> dirs = searchDirs(rootDir);
        for (File dir : dirs) {
            FileUtils.deleteQuietly(dir);
        }

        //xsd clear
        rootDir = getSystemXsdDirectoryResource(".").getFile();
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
     * Возвращает ресурс, лежащий в pathBase
     *
     * @param systemName       name of system
     * @param integrationPoint name of integrationPoint
     * @param fileName         name of File to get
     * @return ресурс xml
     * @throws IOException
     */
    public Resource getDataResource(String systemName, String integrationPoint, String fileName) throws IOException {
        return appContext.getResource(dataPath + systemName + File.separator + integrationPoint + File.separator +
                fileName);
    }

    public Resource getXsdResource(String systemName, String xsdFile) throws IOException {
//        ru.sbt.bpm.mock.config.entities.System system = configContainer.getConfig().getSystems().getSystemByName(systemName);
//        String remoteRootSchema = system.getRemoteRootSchema();
        if (xsdFile.toLowerCase().startsWith("http")) {
            return new UrlResource(xsdFile);
        } else
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
        Resource resource = appContext.getResource(dataPath +
                systemName + File.separator +
                integrationPointName);
        Resource newResource = appContext.getResource(dataPath +
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
        Resource resource = appContext.getResource(dataPath +
                systemName + File.separator +
                integrationPointName);
        FileUtils.deleteDirectory(resource.getFile());
    }

    /**
     * Move data and xsd files to another directory on system rename
     *
     * @param systemName    old system name
     * @param newSystemName new system name
     * @throws IOException
     */
    public void moveSystemDir(String systemName, String newSystemName) throws IOException {
        //Data
        Resource dataResource = appContext.getResource(dataPath + systemName);
        Resource newDataResource = appContext.getResource(dataPath + newSystemName);
        FileUtils.moveDirectory(dataResource.getFile(), newDataResource.getFile());
        //Xsd
        Resource xsdResource = appContext.getResource(xsdPath + systemName);
        Resource newXsdResource = appContext.getResource(xsdPath + newSystemName);
        FileUtils.moveDirectory(xsdResource.getFile(), newXsdResource.getFile());
    }

    /**
     * Delete data and xsd directory on system delete
     *
     * @param systemName name of system, to delete
     * @throws IOException
     */
    public void deleteSystemDir(String systemName) throws IOException {
        Resource xsdResource = appContext.getResource(xsdPath + systemName);
        Resource dataResource = appContext.getResource(dataPath + systemName);
        FileUtils.deleteDirectory(xsdResource.getFile());
        FileUtils.deleteDirectory(dataResource.getFile());
    }

    public void uploadSchema(String systemName, File xsdZipFile) throws IOException {
        Resource xsdDirectoryResource = appContext.getResource(xsdPath + systemName);
        String xsdDirectoryPath = xsdDirectoryResource.getFile().getAbsolutePath();
        File file = new File(xsdDirectoryPath);
        if (file.delete()) {
            file.deleteOnExit();
        }
        configurationService.unzipFile(xsdZipFile, xsdDirectoryPath);
    }
}
