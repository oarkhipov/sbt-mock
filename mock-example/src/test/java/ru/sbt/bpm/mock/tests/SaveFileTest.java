package ru.sbt.bpm.mock.tests;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbt.bpm.mock.utils.SaveFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SBT-Vostrikov-MI on 16.12.2014.
 */
public class SaveFileTest {

    static protected SaveFile saveFile;

    @BeforeClass
    static public void init() throws IOException{
        saveFile = SaveFile.getInstance("C:\\Users\\sbt-vostrikov-mi\\Java\\Idea\\XSDMockService\\mock\\mock-example\\target\\ncpdb-interactive\\WEB-INF");
    }

    @Test
    public void testPaths() throws IOException {
        //проверка что в getWebInfPath содерждатся те папки, что мы и ожидаем
        String getpath = saveFile.getWebInfPath();

        File folder = new File(getpath);
        File[] listOfFiles = folder.listFiles();
        List<String> listOfFileNames = new ArrayList(listOfFiles.length);
        for(File f : listOfFiles) {
            listOfFileNames.add(f.getName());
        }

        assert listOfFileNames.size()>0;
        assert listOfFileNames.contains("data");
        assert listOfFileNames.contains("jsp");
        assert listOfFileNames.contains("xsd");
        assert listOfFileNames.contains("xsl");
    }

    /**
     * сохраняем бэкап. Лучше всегда брать файл через этот вызов.
     * Возвращает файл, укбедившись что у него есть бэкап.
     */
    @Test
    public void checkBackUp() throws Exception {
        File file = saveFile.getBackUpedDataFile("AMRLiRT\\xml\\SrvCalcDebtCapacityData.xml");
        System.out.println(file.getCanonicalPath());
    }

    /**
     * возвращаемся к самому раннему бэкапу.
     * Текущая xml также оказывается в бэкапе, если еще не была
     */
    @Test
    public void checkRestoreBackUp() throws Exception {
        File file = saveFile.restoreBackUpedDataFile("AMRLiRT\\xml\\SrvCalcDebtCapacityData.xml");
        System.out.println(file.getName());
    }

    /**
     * гуляеем по бэкАпам.
     * restoreNextBackUpedDataFile возвращает следующий бэкап.
     * Каждый вызов функции будет возвращать следующий файл.
     * Текущая xml также оказывается в бэкапе, если еще не была.
     */
    @Test
    public void checkNextRestoreBackUp() throws Exception {
        File file = saveFile.getNextBackUpedDataFile("AMRLiRT\\xml\\SrvCalcDebtCapacityData.xml");
        System.out.println(file.getName());
        file = saveFile.getNextBackUpedDataFile("AMRLiRT\\xml\\SrvCalcDebtCapacityData.xml");
        System.out.println(file.getName());
        file = saveFile.getNextBackUpedDataFile("AMRLiRT\\xml\\SrvCalcDebtCapacityData.xml");
        System.out.println(file.getName());
        file = saveFile.getNextBackUpedDataFile("AMRLiRT\\xml\\SrvCalcDebtCapacityData.xml");
        System.out.println(file.getName());
    }
}
