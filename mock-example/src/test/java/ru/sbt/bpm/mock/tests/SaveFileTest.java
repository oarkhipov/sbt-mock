package ru.sbt.bpm.mock.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.utils.SaveFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SBT-Vostrikov-MI on 16.12.2014.
 */
public class SaveFileTest {

    protected SaveFile saveFile;

    @BeforeClass
    public void init() {
        saveFile = SaveFile.getInstance();
    }

    @Test
    public void testPaths() {
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

        //проверка что в resources содерждатся те папки, что мы и ожидаем
        getpath = saveFile.getResourcesPath();

        folder = new File(getpath);
        listOfFiles = folder.listFiles();
        listOfFileNames = new ArrayList(listOfFiles.length);
        for(File f : listOfFiles) {
            listOfFileNames.add(f.getName());
        }

        assert listOfFileNames.size()>0;
        assert listOfFileNames.contains("xml");
        assert listOfFileNames.contains("xmlAssertion");
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
        File file = saveFile.restoreNextBackUpedDataFile("AMRLiRT\\xml\\SrvCalcDebtCapacityData.xml");
        System.out.println(file.getName());
        file = saveFile.restoreNextBackUpedDataFile("AMRLiRT\\xml\\SrvCalcDebtCapacityData.xml");
        System.out.println(file.getName());
        file = saveFile.restoreNextBackUpedDataFile("AMRLiRT\\xml\\SrvCalcDebtCapacityData.xml");
        System.out.println(file.getName());
        file = saveFile.restoreNextBackUpedDataFile("AMRLiRT\\xml\\SrvCalcDebtCapacityData.xml");
        System.out.println(file.getName());
    }
}
