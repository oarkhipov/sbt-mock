package ru.sbt.bpm.mock.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;


/**
 * Created by sbt-bochev-as on 13.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
public class XmlDataService {
    @Autowired
    private ApplicationContext appContext;


    public static final String pathBase = "/WEB-INF/data/";

    public boolean validate() {
        //TODO add validation


//        try {
//            SchemaFactory factory =
//                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//            StreamSource sources[] = new StreamSource[xsds.length];
//            for (int i = 0; i < xsds.length; i++) {
//                System.out.println("add xsd [" + xsds[i] + "] to test schema");
//                sources[i] = new StreamSource(ClassLoader.getSystemResourceAsStream(xsds[i]));
//            }
//            Schema schema = factory.newSchema(sources);
//            Validator validator = schema.newValidator();
//            System.out.println("test xml [" + xml + "]");
//            validator.validate(new StreamSource(ClassLoader.getSystemResourceAsStream(xml)));
//        } catch (SAXException e) {
//            e.printStackTrace();
//            return false;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return true;

    }

    public String getXml(String name) throws IOException {
        String[] nameParts = name.split("_");
        Resource resource = appContext.getResource(pathBase + nameParts[0] + File.separator + "xml" + File.separator + nameParts[1] + "Data.xml");
        return FileUtils.readFileToString(resource.getFile());
    }

    public Resource getResource(String path) {
        return appContext.getResource(path);
    }

}
