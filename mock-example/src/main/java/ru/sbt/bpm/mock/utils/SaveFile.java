package ru.sbt.bpm.mock.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SBT-Vostrikov-MI on 16.12.2014.
 */
public class SaveFile {
    private static SaveFile ourInstance = new SaveFile();

    public static SaveFile getInstance() {
        return ourInstance;
    }

    private SaveFile() {
        slash = File.separator;
        currentChosenBackUp = new HashMap<>();
    }

    public int logSize = 5;//TODO засунуть в конфиг

    private Map<String, Integer> currentChosenBackUp;

    protected String slash;

    /**
     * путь к дирректории WEB-INF
     */
    private String webInfPath;

    /**
     * путь к дирректории resources
     */
    private String resourcesPath;

    /**
     * путь к дирректории out
     */
    private String outPath;

    public String getWebInfPath() {
        if (webInfPath!=null) return webInfPath;
        webInfPath = System.getProperty("user.dir") + slash +"src"+slash+"main"+slash+"webapp"+slash+"WEB-INF";
        return webInfPath;
    }

    public String getResourcesPath() {
        if (resourcesPath!=null) return resourcesPath;
        resourcesPath = System.getProperty("user.dir") +slash+"src"+slash+"test"+slash+"resources";
        return resourcesPath;
    }

    public String getOutPath() {
        if (outPath!=null) return outPath;
        outPath = System.getProperty("user.dir") +slash+"out"+slash+"back";
        return outPath;
    }

    /**
     * Возвращает файл из папки WEB-INF/data
     * @param path путь формата "AMRLiRT/xml/SrvCalcDebtCapacityData.xml"
     * @return Java.io.File
     */
    protected File getDataFile(String path) throws FileNotFoundException{
        File f = new File(getWebInfPath() +slash+"data"+slash+path);
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
        return f;
    }

    /**
     * Возвращает файл из папки WEB-INF/data, предварительно сохранив бэкап
     * @param path путь формата "AMRLiRT/xml/SrvCalcDebtCapacityData.xml"
     * @return Java.io.File
     */
    public File getBackUpedDataFile(String path) throws IOException{
        assert !path.contains(".."+slash) : "other directories are blocked";
        File f = getDataFile(path);
        File back = isFileNeedBackUp(path, f);
        if (back != null) {
            copyFile(f, back);
        }
        return f;
    }

    /**
     * Возвращает файл из бэкапа WEB-INF/data
     * @param path путь формата "AMRLiRT/xml/SrvCalcDebtCapacityData.xml"
     * @return Java.io.File
     */
    public File restoreBackUpedDataFile(String path) throws IOException {
        return restoreBackUpedDataFile(path, 1);
    }
    public File restoreBackUpedDataFile(String path, int num) throws IOException {
        assert !path.contains(".."+slash) : "other directories are blocked";
        assert num>=1 : "cant restore ["+num+"] backupfile";
        //создаем бэекап текущего, но только если уже нет такого же
        File f = getBackUpedDataFile(path);
        List<File> backUps = getBackUpFilesList(path, f);
        assert num<backUps.size()+1 : "cant restore ["+num+"] backupfile. there is only ["+backUps.size()+"] backups";

        //copyFile(backUps.get(num-1), f);
        return backUps.get(num-1);
    }

    /**
     * Возвращает файл из папки WEB-INF/data, предварительно вернув его из бэкапа
     * Каждый следующий вызов возвращает новую запись из бэкапа.
     * @param path путь формата "AMRLiRT/xml/SrvCalcDebtCapacityData.xml"
     * @return Java.io.File
     */
    public File restoreNextBackUpedDataFile(String path) throws IOException {
        assert !path.contains(".."+slash) : "other directories are blocked";
        //создаем бэекап текущего, но только если уже нет такого же
        File f = getBackUpedDataFile(path);
        List<File> backUps = getBackUpFilesList(path, f);

        int num=-1;
        if (currentChosenBackUp.containsKey(path)) {
            num = currentChosenBackUp.get(path);
        } else {
            //ищем элемент, который совпадает с нашим.
            try {
                long fhash = FileUtils.checksumCRC32(f);
                for (int i = backUps.size() - 1; num < 0 && i >= 0; i--) {
                    long bhash = FileUtils.checksumCRC32(backUps.get(i));
                    if (bhash == fhash) {
                        num = i;
                    }
                }
            } catch (IOException e) {
                num = backUps.size() - 1;
            }
        }

        // увеличиваем на один. Если перевалили за размер - начинаем с начала.
        num--;
        if (num <0) num=backUps.size()-1;

        //запоминаем номер,Ю на котором остановились, чтобы потом начать с него
        currentChosenBackUp.put(path, num);

        //copyFile(backUps.get(num), f);
        return backUps.get(num);
    }

    /**
     * проверяет, нуждается ли файл в бэкапе.
     * Должен проверить есть ли такой файл в папке с бэкапапми, если есть - не похож ли последний на этот
     * @param f файл для бэкапа
     * @return File если нужно. Null если бэк не нужен
     */
    protected File isFileNeedBackUp(String path, File f) {
        String subPath = path.replace(f.getName(), "");
        File backDir = getBackUpDestinationDir(subPath, f);
        try {
            assert backDir.exists();//дирректории нет - нуужно делать бэкап

            //список бэкапов с похожими намерами
            List<File> backupFiles = getBackUpFilesList(path, f);

            //просматриваем список бэкапов - вдруг такой уже есть
            long fhash = FileUtils.checksumCRC32(f);
            boolean noBackUpFile = true;
            for (File bf : backupFiles) {
                if (noBackUpFile) {
                    long bhash = FileUtils.checksumCRC32(bf);
                    noBackUpFile = fhash != bhash;
                }
            }
            assert !noBackUpFile;

            return null;

        } catch (Exception | AssertionError e){
            return new File(backDir.getPath() + slash + composeBackUpFilename(f.getName()));
        }

    }

    /**
     * Возвращает директорию, куда надо делать бэкап
     * не проверяет наличие дирректории
     */
    private File getBackUpDestinationDir(String path, File f) {
        String subPath = path.replace(f.getName(), "");
        File dir = new File(getOutPath() +slash+ subPath);
        return dir;
    }

    /**
     * если в списске бэкапов уже есть такой файл - не делает бэкап, возвращает null
     * если нет такого файла - выдает новое имя.
     */
    private String composeBackUpFilename(File f, List<File> backUpFilesList) throws IllegalArgumentException {
        try {
            long fhash = FileUtils.checksumCRC32(f);
            for (File bf : backUpFilesList) {
                long bhash = FileUtils.checksumCRC32(bf);
                if (bhash == fhash) {
                    return null;
                }
            }
        }
        catch (IOException e){

        }
        return composeBackUpFilename(f.getName());
    }

    /**
     * првращает строку типа filename.xml в filename.20141216_0941.xml
     */
    private String composeBackUpFilename(String filename) throws IllegalArgumentException {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date dateTime = new Date();
        String date = dateFormat.format(dateTime);

        Pattern p = Pattern.compile("^([\\w,\\s-]+)\\.([\\w]{3})$");
        Matcher m = p.matcher(filename);
        if (m.find()) {
            return m.replaceFirst("$1."+date+".$2");
        }
        throw new IllegalArgumentException("wrong filename : ["+filename+"]");
    }

    /**
     * проверяет, нужно является ли данный файл бэкапом второго файла
     * Также евляется ли он более новым бэкапом для третьего файла
     */
    private boolean isFileValidBackUp(File b, File f, File bold) {
        String bname = b.getName();
        String fname = f.getName();
        String fbasename = FilenameUtils.getBaseName(fname);
        String fextname = FilenameUtils.getExtension(fname);
        if ( !bname.contains(fbasename) ) return false;
        if ( !bname.contains(fextname) ) return false;

        Pattern p = Pattern.compile("^[\\w,\\s-]+\\.(\\d{8}_\\d{6})\\.[\\w]{3}$");
        Matcher m = p.matcher(bname);
        if (!m.find()) return false; //это какой-то не нами созданный файл. или создан позднее 9999 года - тогда программа будет работать странно.

        if (bold == null) { //если на этом этапе файл подходит - он может быть бэкапом
            return true;
        }
        String dateA = m.group(1);
        Matcher mB = p.matcher(bold.getName());
        mB.find();//по идее если файл оказался как bold, то мы его уже проверили регуляркой
        String dateB = mB.group(1);
        return dateA.compareTo(dateB)>0; //сравниваем строково. Даты одинакового формата, дата по убыванию. Должна вернуть более позднюю.
    }

    protected void copyFile(File f, File back) throws IOException {
        FileUtils.copyFile(f,back);
    }

    protected List<File> getBackUpFilesList(String path, File f) throws IOException {
        String fname = f.getName();
        String fbasename = FilenameUtils.getBaseName(fname);
        String fextname = FilenameUtils.getExtension(fname);
        File backDir =  getBackUpDestinationDir(path, f);
        List files = new ArrayList();


        Pattern p = Pattern.compile("^" + fbasename + "\\.(\\d{8}_\\d{6})\\." + fextname + "$");

        for (File bf : backDir.listFiles()) {
            Matcher m = p.matcher(bf.getName());
            if (m.find()) {
                files.add(bf);
            }
        }
        return files;
    }


}
