package ru.sbt.bpm.mock.generator.xsd;

import net.sf.saxon.lib.NamespaceConstant;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import ru.sbt.bpm.mock.generator.localPaths;
import ru.sbt.bpm.mock.config.entities.*;
import ru.sbt.bpm.mock.spring.utils.SaveFile;
import ru.sbt.bpm.mock.spring.utils.Xsl20Transformer;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.*;

/**
 * Класс для импорта новой xsd и замены его на старые
 * Created by sbt-vostrikov-mi on 14.01.2015.
 */
public class importXSD {

    private static importXSD ourInstance = new importXSD();

    public static importXSD getInstance() {
        return ourInstance;
    }

    private importXSD() {
    }

    private Validator validator;

    /**
     * копирует входящие xsd-файлы с зависимостями
     * @param system результат парсинга конфига
     * @param point результат парсинга конфига
     * @throws Exception
     */
    public void copyXSDFiles(SystemTag system, IntegrationPoint point) throws Exception {
        File baseDir = findFolder(system);
        String systemName = system.getaSystemName();
        System.out.println("Используется дирректория {"+baseDir.getAbsolutePath()+"}" );
        importFile(baseDir.getAbsolutePath() + File.separator + system.getaRootXSD(), formSubPath(system.getaRootXSD(), systemName));
        importFile(baseDir.getAbsolutePath() + File.separator + point.getaXsdFile(), formSubPath(point.getaXsdFile(), systemName));
        for (Dependency dependency : point.getaDependencies().getaDependency()) {
            importFile(baseDir.getAbsolutePath() + File.separator + dependency.getaXsdFile(), formSubPath(dependency.getaXsdFile(), systemName));
        }
    }

    /**
     * вырезаем подпуть к директории. Например, если у нас есть строка "commonTypes/commonTypes.xsd", то вернуть нам нужно только commonTypes/
     * @param pathToAdd относительный путь файла
     * @param subfolder то, куда надо приписать этот подпуть
     * @return часть относительного пути без самого файла
     */
    private String formSubPath(String pathToAdd, String subfolder) {
        if (pathToAdd.contains("\\") || pathToAdd.contains("/") ) {
            int i = pathToAdd.lastIndexOf("/");
            int j = pathToAdd.lastIndexOf("\\");
            int k = i>j?i:j;
            subfolder += File.separator + pathToAdd.substring(0, k);
        }
        return subfolder;
    }



    /**
     * Получает путь к папке и проверяет разные возможные пути, чтобы найти нужную нам папку
     * @param system результат парсинга конфига
     * @throws Exception
     */
    private File findFolder(SystemTag system) throws FileNotFoundException {
        List<String> possiblePaths = new ArrayList<String>(); // записываем в список всякие разные пути, по которым можна найти папку
        possiblePaths.add(localPaths.getPath()); //основной путь
        possiblePaths.add(localPaths.getWebInfPath()); //WebInf папку
        possiblePaths.add(localPaths.getExamplesPath()); //папку с примерами
        possiblePaths.add(""); //корень для абсолютных путей и рабочей дирректории
        for (String subPath : possiblePaths ) { //и по очереди проверям эти пути
            try {
                File baseDir = new File(subPath + system.getaPathToXSD());
                if (!baseDir.exists()) throw new FileNotFoundException(); //папка есть?
                if (!baseDir.isDirectory()) throw new FileNotFoundException(); //это папка?
                File baseXSD = new File(subPath + system.getaPathToXSD() + File.separator + system.getaRootXSD() ); //также проверим что в этой папке есть xsd
                if (!baseXSD.exists()) throw new FileNotFoundException(); //xsd есть?
                if (baseXSD.isDirectory()) throw new FileNotFoundException(); //xsd это не папка?
                if (baseXSD.getTotalSpace() == 0) throw new FileNotFoundException(); //xsd не пустой?
                return baseDir; //нашли нужный нам файл
            } catch (Exception e) {
                //не нашли по этому пути, посмотрим в следующем
            }
        }
        throw new FileNotFoundException(system.getaPathToXSD() + "\\" + system.getaRootXSD());
    }

    /**
     * копирование одного конкретного xsd-файла в дирректроию WEB-INF\xsd\{subFolder}
     * Проверяет хэш файла, и если такой уже есть, то не копирует
     * @param filePathToImport файл, который надо скопировать
     * @param subFolder подпапка в WEB-INF\xsd куда будет помешен файл
     */
    private void importFile(String filePathToImport, String subFolder) throws Exception {
        File fileToImport = new File(filePathToImport);
        File copyTo = new File(localPaths.getWebInfPath()+ File.separator + "xsd" + File.separator + subFolder + File.separator + fileToImport.getName());
        if (copyTo.exists()) {
            long cheksumFrom = FileUtils.checksumCRC32(fileToImport);
            long cheksumTo = FileUtils.checksumCRC32(copyTo);
            if (cheksumTo == cheksumFrom) {
                System.out.println("Файл {"+fileToImport.getName()+"} не нуждается в обновлении");
                return; //файлы одинаковые, копировать не нужно
            }
            FileUtils.copyFile(fileToImport, copyTo);
            System.out.println("Файл {" + fileToImport.getName() + "} обновлен");
            return;
        }
        FileUtils.copyFile(fileToImport, copyTo);
        System.out.println("Файл {" + fileToImport.getName() + "} скопирован");
        return;
    }



}
