package ru.sbt.bpm.mock.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.xml.sax.SAXException;


import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sbt-bochev-as on 13.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/mockapp-servlet-test.xml"})
public class XsdValidator {

    @Autowired
    ApplicationContext applicationContext;


    @Test
    public void test1() throws IOException {

        Resource resource = applicationContext.getResource("/WEB-INF/web.xml");

        File dir = new File(resource.getFile().getParent());

        final ArrayList<File> xsdFiles = new ArrayList<>();

        searchFiles(dir, xsdFiles, ".xsd");



        System.out.println(xsdFiles.size());
        for (File xsdFile : xsdFiles) {
            System.out.println("xsd: " + xsdFile.getName());
        }


        System.out.println("-----------------------------");


    }

    private boolean isValid(String xml, String... xsds) {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource sources[] = new StreamSource[xsds.length];
            for (int i = 0; i < xsds.length; i++) {
                System.out.println("add xsd [" + xsds[i] + "] to test schema");
                sources[i] = new StreamSource(ClassLoader.getSystemResourceAsStream(xsds[i]));
            }
            Schema schema = factory.newSchema(sources);
            Validator validator = schema.newValidator();
            System.out.println("test xml [" + xml + "]");
            validator.validate(new StreamSource(ClassLoader.getSystemResourceAsStream(xml)));
        } catch (SAXException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void searchFiles(File rootDir, ArrayList<File> files, String ext) {
        File[] listFiles = rootDir.listFiles();
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
