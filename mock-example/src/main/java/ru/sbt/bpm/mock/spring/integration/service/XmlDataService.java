package ru.sbt.bpm.mock.spring.integration.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import ru.sbt.bpm.mock.spring.utils.ResourceResolver;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * Created by sbt-bochev-as on 13.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
@Service
public class XmlDataService {
    @Autowired
    private ApplicationContext appContext;

    private String pathBase = "/WEB-INF/";
    private String xslPathBase = "/WEB-INF/xsl/";

    private Validator validator;

    private ArrayList<File> xsdFiles = null;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    protected void init() throws IOException, SAXException {
        Resource resource = appContext.getResource("/WEB-INF/web.xml");

        File xsdDir = new File(resource.getFile().getParent() + File.separator + "xsd");
        File xsdDataDir = new File(resource.getFile().getParent() + File.separator + "data");

        xsdFiles = new ArrayList<File>();
        searchFiles(xsdDir, xsdFiles, ".xsd");
        searchFiles(xsdDataDir, xsdFiles, ".xsd");

        SchemaFactory factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        /**
         * ResourceResolver добавлен для корректной работы с XSD с одиннаковыми именами, но в разных директориях
         */
        factory.setResourceResolver(new ResourceResolver());
        StreamSource sources[] = new StreamSource[xsdFiles.size()];

//            Add Xsd files to source
        for (int i = 0; i < xsdFiles.size(); i++) {
            sources[i] = new StreamSource(xsdFiles.get(i));
        }

            Schema schema = factory.newSchema(sources);
            validator = schema.newValidator();
    }

    public void setPathBase(String pathBase) {
        this.pathBase = pathBase;
    }


    /**
     * Возвращает xml по имени, относительно pathBase
     *
     * @param name путь до файла
     * @return содержимое файла
     * @throws IOException
     */
    public String getXml(String name) throws IOException {
        return FileUtils.readFileToString(getResource(name).getFile(), Charset.forName("UTF-8"));
    }

    /**
     * Возвращает xml по спец имени, относительно pathBase
     *
     * @param name путь до файла
     * @return содержимое файла
     * @throws IOException
     */
    public String getDataXml(String name) throws IOException {
        return FileUtils.readFileToString(getXmlDataResource(name).getFile(), Charset.forName("UTF-8"));
    }

    /**
     * Возвращает ресурс, лежащий в pathBase
     * 
     * @param name имя xmlData
     * @return ресурс xml
     * @throws IOException
     */
    public Resource getXmlDataResource(String name) throws IOException {
        String[] nameParts = name.split("_");
        return appContext.getResource(pathBase + File.separator +
                "data" + File.separator +
                nameParts[0] + File.separator +
                "xml" + File.separator +
                nameParts[1] + "Data.xml");
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
     * Возвращает ресурс, лежащий в xslPathBase
     *
     * @param name спец имя xsl
     * @return ресурс xsl
     * @throws IOException
     */
    public Resource getXslResource(String name) throws IOException {
        String[] nameParts = name.split("_");
        return appContext.getResource(xslPathBase + nameParts[0] + File.separator + File.separator + nameParts[1] + ".xsl");
    }

    /**
     * Валидирует xmlData на соответствие схем
     * 
     * @param xmlData спец имя xmlData
     * @return признак валидности
     * @throws Exception
     */
    public boolean validate(String xmlData) throws Exception {
        InputStream stream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));
        try {
            validator.validate(new StreamSource(stream));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }

    /**
     * Валидирует xmlData на соответствие схем, не бросает исключений
     *
     * @param xmlData спец имя xmlData
     * @return признак валидности
     */
    public boolean assertableValidate(String xmlData) {
        try {
            return validate(xmlData);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Рекурсивный поиск файлов с определённым расширением
     *
     * @param rootDir корневая директория поиска
     * @param files список файлов, который формуирует функция при поиске
     * @param ext расширение файла
     */
    private void searchFiles(File rootDir, ArrayList<File> files, String ext) {
        File[] listFiles = rootDir.listFiles();
        assert listFiles != null;
        for (File file : listFiles) {
            if(file.isDirectory()) {
                searchFiles(file, files, ext);
            }
            else if(file.getName().toLowerCase().endsWith(ext)){
                log.debug("Added xsd [" + file.getName() + "] to Validator");
                files.add(file);
            }
        }
    }

    public void showConfig() {
        log.info("Validator have " + xsdFiles.size() + " file(s) in it's database");
        for (File xsdFile : xsdFiles) {
            log.info("Xsd file [" + xsdFile.getName() + "] was added to Validator list");
        }
    }
}
