package ru.sbt.bpm.mock.tests;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.sbt.bpm.mock.spring.utils.SaveFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SBT-Vostrikov-MI on 16.12.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class SaveFileTest {


    @Autowired
    public ApplicationContext appContext;

    public SaveFile saveFile;

    @Test
    @Ignore
    public void testPaths() throws IOException {
        saveFile = SaveFile.getInstance(appContext);
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
    @Ignore
    public void checkBackUp() throws Exception {
        checkBackUpChangedFile("\\backup\\AMRLiRT\\xml", "AMRLiRT\\xml\\CalculateDebtCapacityData.xml");
        checkBackUpChangedFile("\\backup\\CRM\\xml", "CRM\\xml\\ForceSignalData.xml");
        checkBackUpChangedFile("\\backup\\FinRep\\xml", "FinRep\\xml\\FinAnalysisImportData.xml");
    }

    private File checkBackUpChangedFile(String backSupFolder, String fileToBackUp) throws Exception  {
        File f = checkBackUp(backSupFolder, fileToBackUp);
        long cheksumB = FileUtils.checksumCRC32(f);
        System.out.println(cheksumB);
        String str = saveFile.getFileString(f);
        String strstr=str.replace("string","string1");
        FileUtils.writeStringToFile(f, strstr);
        sleep(1000);
        long cheksum2 = FileUtils.checksumCRC32(f);
        System.out.println(cheksum2);
        File f2 = checkBackUp(backSupFolder, fileToBackUp);

        assert f.getAbsolutePath().equals(f2.getAbsolutePath())
                : "Different files; ["+f.getAbsolutePath()+"] and ["+f2.getAbsolutePath()+"]";

        String strstr2=str.replace("string","string2");
        FileUtils.writeStringToFile(f, strstr2);
        sleep(1000);
        long cheksum3 = FileUtils.checksumCRC32(f);
        System.out.println(cheksum3);
        File f3 = checkBackUp(backSupFolder, fileToBackUp);
        sleep(1000);

        assert f.getAbsolutePath().equals(f3.getAbsolutePath())
                : "Different files; ["+f.getAbsolutePath()+"] and ["+f3.getAbsolutePath()+"]";

        File f4 = restorBackUp(backSupFolder, fileToBackUp);
        sleep(1000);
        long cheksumA = FileUtils.checksumCRC32(f4);

        saveFile = SaveFile.getInstance(appContext);
        File file = saveFile.getNextBackUpedDataFile(fileToBackUp);
        System.out.println(file.getName());
        File file2 = saveFile.getNextBackUpedDataFile(fileToBackUp);
        System.out.println(file2.getName());
        File file3 = saveFile.getNextBackUpedDataFile(fileToBackUp);
        System.out.println(file3.getName());
        File file4 = saveFile.getNextBackUpedDataFile(fileToBackUp);
        System.out.println(file4.getName());

        assert !file.getAbsolutePath().equals(file2.getAbsolutePath());
        assert !file2.getAbsolutePath().equals(file3.getAbsolutePath());
        assert !file3.getAbsolutePath().equals(file4.getAbsolutePath());

        assert cheksumA == cheksumB;

        return f;
    }

    private File checkBackUp(String backSupFolder, String fileToBackUp) throws Exception  {
        saveFile = SaveFile.getInstance(appContext);
        String subpath = saveFile.getWebInfPath();
        File backFolder = new File(subpath + backSupFolder);
        File[] backupsBefore = backFolder.listFiles();
        if (backupsBefore == null) backupsBefore = new File[0];
        File file = saveFile.getBackUpedDataFile(fileToBackUp);
        System.out.println(file.getCanonicalPath());

        assert file.exists() : "файл не существует";
        File[] backupsAfter = backFolder.listFiles();
        assert backupsBefore.length<= backupsAfter.length
                || ( backupsBefore.length==backupsAfter.length && saveFile.logSize==backupsAfter.length )
                : "Количество бэкапов изменилось backupsBefore.length=["+backupsBefore.length+"], backupsAfter.length=["+backupsAfter.length+"], saveFile.logSize=["+saveFile.logSize+"],";
        int countSameBefore = 0;
        long fhash = FileUtils.checksumCRC32(file);
        for (File bf : backupsBefore) {
            long bhash = FileUtils.checksumCRC32(bf);
            if (bhash == fhash) countSameBefore++;
        }

        int countSame = 0;
        for (File bf : backupsAfter) {
            long bhash = FileUtils.checksumCRC32(bf);
            if (bhash == fhash) countSame++;
        }
        assert countSame>0 : "бэкапов нет";
        assert countSame==1 || countSame==countSameBefore : "бэкапов больше одного countSame:"+countSame+", countSameBefore"+countSameBefore;

        int countSameBacks = 0;
        for (File ba : backupsAfter) {
            long ahash = FileUtils.checksumCRC32(ba);
            for (File bb : backupsBefore) {
                long bhash = FileUtils.checksumCRC32(bb);
                if (ahash == bhash) countSameBacks++;
            }
        }
        if (backupsBefore.length==backupsAfter.length && saveFile.logSize==backupsAfter.length) {
            assert countSame == saveFile.logSize - 1;
        } else {
            assert countSameBacks+countSame-countSameBefore == backupsAfter.length :"not equals:"+countSameBacks+"+"+countSame+"-"+countSameBefore+" == "+backupsAfter.length;
        }
        return file;
    }

    private File restorBackUp(String backSupFolder, String fileToBackUp) throws Exception  {
        saveFile = SaveFile.getInstance(appContext);
        String subpath = saveFile.getWebInfPath();
        File backFolder = new File(subpath + backSupFolder);
        File[] backupsBefore = backFolder.listFiles();
        if (backupsBefore == null) backupsBefore = new File[0];
        File file = saveFile.restoreBackUpedDataFile(fileToBackUp);
        System.out.println(file.getCanonicalPath());

        assert file.exists() : "файл не существует";
        File[] backupsAfter = backFolder.listFiles();
        assert backupsBefore.length == backupsAfter.length || backupsBefore.length + 1 == backupsAfter.length
                : "Количество бэкапов изменилось backupsBefore.length=["+backupsBefore.length+"], backupsAfter.length=["+backupsAfter.length+"], saveFile.logSize=["+saveFile.logSize+"],";

        int countSameBefore = 0;
        long fhash = FileUtils.checksumCRC32(file);
        for (File bf : backupsBefore) {
            long bhash = FileUtils.checksumCRC32(bf);
            if (bhash == fhash) countSameBefore++;
        }

        int countSame = 0;
        for (File bf : backupsAfter) {
            long bhash = FileUtils.checksumCRC32(bf);
            if (bhash == fhash) countSame++;
        }
        assert countSame>0 : "бэкапов нет";
        assert countSame==1 || countSame==countSameBefore : "бэкапов больше одного countSame:"+countSame+", countSameBefore"+countSameBefore;

        int countSameBacks = 0;
        for (File ba : backupsAfter) {
            long ahash = FileUtils.checksumCRC32(ba);
            for (File bb : backupsBefore) {
                long bhash = FileUtils.checksumCRC32(bb);
                if (ahash == bhash) countSameBacks++;
            }
        }
        if (backupsBefore.length==backupsAfter.length && saveFile.logSize==backupsAfter.length) {
            assert countSame == saveFile.logSize - 1;
        } else {
            assert countSameBacks+countSame-countSameBefore == backupsAfter.length :"not equals:"+countSameBacks+"+"+countSame+"-"+countSameBefore+" == "+backupsAfter.length;
        }
        return file;
    }


    private void sleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e)
        {

        }
    }

}
