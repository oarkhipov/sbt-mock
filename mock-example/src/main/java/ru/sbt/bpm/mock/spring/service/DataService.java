package ru.sbt.bpm.mock.spring.service;

import lombok.extern.log4j.Log4j;
import net.sf.saxon.s9api.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.spring.utils.ResourceResolver;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by sbt-bochev-as on 13.12.2014.
 * @author sbt-vostrikov-mi
 * <p/>
 * Company: SBT - Moscow
 */
@Log4j
@Service
public class DataService {
    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private Map<String, String> sbrfNamespaceMap;

    @Autowired
    private MockConfigContainer configContainer;

    private String pathBase = "/WEB-INF/";

    private Map<String, Validator> validator;

    private ArrayList<File> allXsdFiles = null; //используется только для статистики

    private SchemaFactory factory;

    @PostConstruct
    protected void init() throws IOException, SAXException {
        Resource resource = appContext.getResource("/WEB-INF/web.xml");

        File xsdDir = new File(resource.getFile().getParent() + File.separator + "xsd");

        factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        allXsdFiles = new ArrayList<File>();
        /**
         * ResourceResolver добавлен для корректной работы с XSD с одиннаковыми именами, но в разных директориях
         */
        factory.setResourceResolver(new ResourceResolver());
        validator = new HashMap<String, Validator>();

        //TODO в валидатор не будут добавлены xsd из корня папок xsd и data. Пока это и не нужно - в них пока содердится только configFile.xsd, который пока не за чем валидировать. Если понадобится валидировать и его - нужно будет сделать отдельный валидатор и вызов для него.
        Map<String,List<File>> systemMap = new HashMap<String, List<File>>();
        //получаем мапу <имя системы, список файлов относящихся к ней>
        getSystemDirs(systemMap, xsdDir);

        for (Map.Entry<String,List<File>> system : systemMap.entrySet()) {
            List<File> xsdFiles = new ArrayList<File>();
            //ищем все xsd относящиеся к данной системе. В список попадут как xsd из xsd папки так и и из дата папки
            for (File dir : system.getValue()) {
                xsdFiles.addAll(searchFiles(dir, ".xsd"));
            }
            allXsdFiles.addAll(xsdFiles); //в целях логирования сохраняем еще общий список всех xsd
            initSystemValidator(system.getKey(), xsdFiles); //по полученным файлам создаем валидатор и добавляем его к карте валидаторов
        }
    }


    /**
     * разбор директории по системам. Получает директорию, разбивает все входящие в неё поддиректории в мап.
     * например, на вход получаем директорию дата, на выходе мап со всеми директориями разубитые по системам.
     * при повторнмо вызове добавляет файлы к той же мапе - не плодит системы.
     * @param map входной/выходной мап
     * @param Dir директория, которую надо разбить
     */
    private void getSystemDirs(Map<String,List<File>> map, File Dir) {
        for (File systemDir : searchDirs(Dir)) {
            String system = systemDir.getName();
            if (map.containsKey(system)) {
                map.get(system).add(systemDir);
            } else {
                ArrayList<File> newList = new ArrayList<File>();
                newList.add(systemDir);
                map.put(system, newList);
            }
        }
    }

    /**
     * заведение валидатора для списска xsd файлов и добавление его в карту валидаторов
     * @param system имя валидатора - соответсвует имени системы и папке в xsd и data директориях
     * @param xsdFiles список xsd-файлов
     */
    private void initSystemValidator(String system, List<File> xsdFiles) throws SAXException {
        StreamSource sources[] = new StreamSource[xsdFiles.size()];

//            Add Xsd files to source
        for (int i = 0; i < xsdFiles.size(); i++) {
//            System.out.println(xsdFiles.get(i).getAbsolutePath());
            sources[i] = new StreamSource(xsdFiles.get(i));
        }

        Schema schema = factory.newSchema(sources);
        validator.put(system, schema.newValidator());
    }

    public void setPathBase(String pathBase) {
        this.pathBase = pathBase;
    }


    /**
     * Возвращает файл по имени, относительно pathBase
     *
     * @param name путь до файла
     * @return содержимое файла
     * @throws IOException
     */
    public String getFile(String name) throws IOException {
        return FileUtils.readFileToString(getResource(name).getFile(), Charset.forName("UTF-8"));
    }

    /**
     * Возвращает ресурс, лежащий в pathBase
     *
     * @param name имя systemName_integrationPointName_fileName.ext
     * @return ресурс xml
     * @throws IOException
     */
    public Resource getDataResource(String name) throws IOException {
        String[] nameParts = name.split("_");
        return appContext.getResource(pathBase + File.separator +
                "data" + File.separator +
                nameParts[0] + File.separator +
                nameParts[1] + File.separator +
                nameParts[2]);
    }

    public Resource getXsdResource(String systemName, String xsdFile) throws IOException {
        return appContext.getResource(pathBase + File.separator +
                "xsd" + File.separator +
                systemName + File.separator +
                xsdFile);
    }

    public String getDataResourceContent(String name) throws IOException {
        File file = getDataResource(name).getFile();
        if (!file.exists()) {
            return "";
        }
        return FileUtils.readFileToString(file);
    }

    /**
     * Возвращает ресурс, лежащий в pathBase
     *
     * @param name имя файла, относительно WEB-INF
     * @return ресурс
     * @throws IOException
     */
    public Resource getResource(String name) throws IOException {
        return appContext.getResource(pathBase + name);
    }

    /**
     * Валидирует xml на соответствие схем
     *
     * @param xml спец имя xml
     * @param System подпапка из директорий xsd и data, по которым будет производится ваидация
     * @return признак валидности
     * @throws Exception
     */
    public boolean validate(String xml, String System) throws Exception {
        InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        try {
            validator.get(System).validate(new StreamSource(stream));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }

    /**
     * Валидирует xml на соответствие схем, не бросает исключений
     *
     * @param xmlData спец имя xml
     * @return признак валидности
     */
    public boolean assertableValidate(String xmlData, String System) {
        try {
            return validate(xmlData, System);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Рекурсивный поиск файлов с определённым расширением
     *
     * @param rootDir корневая директория поиска
     * @param ext расширение файла
     */
    private List<File> searchFiles(File rootDir, String ext) {
        List<File> files = new ArrayList<File>();
        File[] listFiles = rootDir.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    files.addAll(searchFiles(file, ext));
                } else if (file.getName().toLowerCase().endsWith(ext)) {
                    log.debug("Added xsd [" + file.getName() + "] to Validator");
                    files.add(file);
                }
            }
        }
        return files;
    }
    /**
     * Нерекурсивный поиск файлов с определённым расширением
     * не заходит в подпапки
     * @param rootDir корневая директория поиска
     * @param ext расширение файла
     */
    private List<File> searchFilesNonRecursve(File rootDir, String ext) {
        List<File> files = new ArrayList<File>();
        File[] listFiles = rootDir.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                } else if (file.getName().toLowerCase().endsWith(ext)) {
                    log.debug("Added xsd [" + file.getName() + "] to Validator");
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
    private List<File> searchDirs(File rootDir) {
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

    public void showConfig() {
        log.info("Validator have " + allXsdFiles.size() + " file(s) in it's database");
        for (File xsdFile : allXsdFiles) {
            log.info("Xsd file [" + xsdFile.getName() + "] was added to Validator list");
        }
    }

    public boolean assertXpath(String xml, String systemName, String integrationPointName) throws XPathExpressionException, SaxonApiException {

        String xpathWithFullNamespaceString = configContainer.getConfig()
                .getSystems().getSystemByName(systemName)
                .getIntegrationPoints().getIntegrationPointByName(integrationPointName)
                .getXpathWithFullNamespaceString();
        log.debug("assert xpath: " + xpathWithFullNamespaceString);
        Processor processor = new Processor(false);
        XPathCompiler xPathCompiler = processor.newXPathCompiler();
        DocumentBuilder builder = processor.newDocumentBuilder();

        //build doc
        StringReader reader = new StringReader(xml);
        XdmNode xdmNode = builder.build(new StreamSource(reader));

        //load xpath
        XPathSelector selector = xPathCompiler.compile(xpathWithFullNamespaceString).load();
        selector.setContextItem(xdmNode);
        return selector.evaluate().size()!=0;
    }

    public String getCurrentMessage(String systemName, String integrationPointName) throws IOException {
        return getAbstractDataFileContent(systemName, integrationPointName, "message.xml");
    }

    public String getCurrentScript(String systemName, String integrationPointName) throws IOException {
        return getAbstractDataFileContent(systemName, integrationPointName, "script.groovy");
    }

    private String getAbstractDataFileContent(String systemName, String integrationPointName, String fileName) throws IOException {
        Resource resource = appContext.getResource(pathBase + File.separator +
                "data" + File.separator +
                systemName + File.separator +
                integrationPointName + File.separator +
                fileName);
        return FileUtils.readFileToString(resource.getFile(),"UTF-8");
    }
}
