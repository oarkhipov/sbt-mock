package ru.sbt.bpm.mock.generator.util;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class SimpleValidator {
    private static SimpleValidator ourInstance = new SimpleValidator();

    public static SimpleValidator getInstance() {
        return ourInstance;
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

    private SimpleValidator() {
    }

    /**
     * пробуем распарсить XML java-методоми
     * @param xml
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public boolean validateXML(String xml) throws IOException, SAXException, ParserConfigurationException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(xml.getBytes());
        builder.parse(stream);

        return true;
    }


}
