package ru.sbt.bpm.mock.spring.utils;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class Validator {
    private static Validator ourInstance = new Validator();

    public static Validator getInstance() {
        return ourInstance;
    }

    /**
     * инициализация валидатора
     * @param dir путь к xsd
     */
    public void initValidator(File dir) throws Exception {

//        ArrayList<File> xsdFiles = new ArrayList<File>();
//        searchFiles(dir, xsdFiles, ".xsd");
//
//        SchemaFactory factory =
//                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//        //StreamSource sources[] = new StreamSource[xsdFiles.size()]; //TODO это главная проблема валидатора - он пытается взять все xsd. возможно какие-то не надо.
//        StreamSource sources[] = new StreamSource[0];
//
////            Add Xsd files to source
//        /*for (int i = 0; i < xsdFiles.size(); i++) {
//            sources[i] = new StreamSource(xsdFiles.get(i));
//        }*/
//        Schema schema = factory.newSchema(sources);
//        validator = schema.newValidator();
    }

    /**
     * поиск файлов по маске
     * @param rootDir дирректория
     * @param files исходящий массив
     * @param ext разрешение
     */
    private void searchFiles(File rootDir, ArrayList<File> files, String ext) throws Exception {
        File[] listFiles = rootDir.listFiles();
        if (listFiles == null) throw new FileNotFoundException(rootDir.getAbsolutePath().toString() + " is empty");
        for (File file : listFiles) {
            if(file.isDirectory()) {
                searchFiles(file, files, ext);
            }
            else if(file.getName().toLowerCase().endsWith(ext)){
                files.add(file);
            }
        }
    }

    private Validator() {
    }

    public boolean validateXML(String xml) throws IOException, SAXException, ParserConfigurationException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(xml.getBytes());
        builder.parse(stream);

        //TODO Эта валидация гораздо круче, но она не пашет. Надо разобратьтся почему.
//        if (validator == null) throw new NullPointerException();
//        Source xmlReader = new StreamSource(new StringReader(xml));
//        validator.validate(xmlReader);
        return true;
    }


}
