package ru.sbt.bpm.mock.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by sbt-bochev-as on 13.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
public class XmlDataService {
    @Autowired
    private ApplicationContext appContext;

    private String pathBase = "/WEB-INF/data/";
    private String xslPathBase = "/WEB-INF/xsl/";

    private Validator validator;

    @PostConstruct
    private void init() throws IOException, SAXException {
        Resource resource = appContext.getResource("/WEB-INF/web.xml");

        File dir = new File(resource.getFile().getParent());

        ArrayList<File> xsdFiles = new ArrayList<File>();
        searchFiles(dir, xsdFiles, ".xsd");

        SchemaFactory factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
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



    public String getXml(String name) throws IOException {
        return FileUtils.readFileToString(getXmlResource(name).getFile());
    }

    public Resource getXmlResource(String name) throws IOException {
        String[] nameParts = name.split("_");
        return appContext.getResource(pathBase + nameParts[0] + File.separator + "xml" + File.separator + nameParts[1] + "Data.xml");
    }
    public Resource getXslResource(String name) throws IOException {
        String[] nameParts = name.split("_");
        return appContext.getResource(xslPathBase + nameParts[0] + File.separator + File.separator + nameParts[1] + ".xsl");
    }

    public boolean validate(String xmlData) throws SAXException, IOException {
        InputStream stream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));
        validator.validate(new StreamSource(stream));
        return true;
    }

    private void searchFiles(File rootDir, ArrayList<File> files, String ext) {
        File[] listFiles = rootDir.listFiles();
        assert listFiles != null;
        for (File file : listFiles) {
            if(file.isDirectory()) {
                searchFiles(file, files, ext);
            }
            else if(file.getName().toLowerCase().endsWith(ext)){
                files.add(file);
            }
        }
    }
}
