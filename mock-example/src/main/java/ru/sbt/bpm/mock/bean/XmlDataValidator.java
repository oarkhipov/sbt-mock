package ru.sbt.bpm.mock.bean;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;

/**
 * Created by sbt-bochev-as on 05.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
public class XmlDataValidator {

    private final String webInfDir = "C:\\work\\IdeaProjects\\GitProjects\\Mock\\mock-example\\src\\main\\webapp\\WEB-INF";

    public static boolean validate(String xml, String... xsds) {
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

    public static boolean validate(String xml) {


//        String[] xsds = dataXsdList.xsd.toArray(new String[dataXsdList.xsd.size()]);
//
//        try {
//            SchemaFactory factory =
//                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//
//            SAXSource sources[] = new SAXSource[xsds.length];
//            for (int i = 0; i < xsds.length; i++) {
//                System.out.println("add xsd [" + xsds[i] + "] to test schema");
//                sources[i] = new SAXSource(new InputSource(ClassLoader.getSystemResourceAsStream(xsds[i])));
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
}
