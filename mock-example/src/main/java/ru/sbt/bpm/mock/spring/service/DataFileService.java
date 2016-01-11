package ru.sbt.bpm.mock.spring.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public Resource getDataResource(String name) {
        return appContext.getResource(dataPath + name);
    }

    public Resource getSystemXsdDirectoryResource(String systemName) {
        return appContext.getResource(xsdPath + systemName + File.separator);
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
        ru.sbt.bpm.mock.config.entities.System system = configContainer.getConfig().getSystems().getSystemByName(systemName);
        String rootXsd = system.getRootXSD();
        if (rootXsd.toLowerCase().startsWith("http")) {
            return new UrlResource(rootXsd);
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


}
