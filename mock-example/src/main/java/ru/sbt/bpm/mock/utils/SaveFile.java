package ru.sbt.bpm.mock.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.ApplicationContext;

import java.io.*;
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

    /**
     * получает путь через ApplicationContext
     * @param AppContext
     * @return
     */
    public static SaveFile getInstance(ApplicationContext AppContext) throws IOException {
        rootpath = AppContext.getResource("/WEB-INF").getFile().getCanonicalPath();
        return ourInstance;
    }

    /**
     * получает путь напрямую
     * @return
     * @throws IOException
     */
    public static SaveFile getInstance(String RootPath) throws IOException {
        rootpath = RootPath;
        return ourInstance;
    }

    private SaveFile() {
        slash = File.separator;
        currentChosenBackUp = new HashMap<String, Integer>();
    }

    public int logSize = 5;//TODO засунуть в конфиг

    private Map<String, Integer> currentChosenBackUp;


    static String rootpath;

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

    public String getWebInfPath() throws IOException {
        if (webInfPath!=null) return webInfPath;
        webInfPath = rootpath;
        return webInfPath;
    }

    /*public String getResourcesPath() {
        if (resourcesPath!=null) return resourcesPath;
        resourcesPath = System.getProperty("user.dir") +slash+"src"+slash+"test"+slash+"resources";
        return resourcesPath;
    }*/

    public String getOutPath() throws IOException {
        if (outPath!=null) return outPath;
        outPath = getWebInfPath() +slash+"backup";
        return outPath;
    }

    /**
     * Возвращает файл из папки WEB-INF/data
     * @param path путь формата "AMRLiRT/xml/CalculateDebtCapacityData.xml"
     * @return Java.io.File
     */
    protected File getDataFile(String path) throws IOException {
        File f = new File(getWebInfPath() +slash+"data"+slash+path);
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
        return f;
    }

    /**
     * Возвращает файл из папки WEB-INF/data, предварительно сохранив бэкап
     * @param path путь формата "AMRLiRT/xml/CalculateDebtCapacityData.xml"
     * @return Java.io.File
     */
    public File getBackUpedDataFile(String path) throws IOException{
        if (path.contains(".."+slash)) throw new FileNotFoundException("other directories are blocked");
        File f = getDataFile(path);
        File back = isFileNeedBackUp(path, f);
        if (back != null) {
            List<File> backUps = getBackUpFilesList(path, f);
            while (backUps.size()>logSize-1) {
                FileUtils.forceDelete(backUps.get(1));//получаем второй файл с конца, не первый. Это важно - нельзя удалять самый старый
                backUps = getBackUpFilesList(path, f);
            }
            copyFile(f, back);
        }
        return f;
    }

    /**
     * Возвращает файл из бэкапа WEB-INF/data
     * @param path путь формата "AMRLiRT/xml/CalculateDebtCapacityData.xml"
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

        copyFile(backUps.get(num-1), f);
        return f;
    }

    /**
     * Возвращает файл из папки WEB-INF/data, предварительно вернув его из бэкапа
     * Каждый следующий вызов возвращает новую запись из бэкапа.
     * @param path путь формата "AMRLiRT/xml/CalculateDebtCapacityData.xml"
     * @return Java.io.File
     */
    public File getNextBackUpedDataFile(String path) throws IOException, IndexOutOfBoundsException {
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

        //запоминаем номер, на котором остановились, чтобы потом начать с него
        currentChosenBackUp.put(path, num);

        //copyFile(backUps.get(num), f);
        return backUps.get(num);
    }


    /**
     * Возвращает файл из папки WEB-INF/data, предварительно вернув его из бэкапа
     * Каждый следующий вызов возвращает новую запись из бэкапа.
     * @param path путь формата "AMRLiRT/xml/CalculateDebtCapacityData.xml"
     * @return Java.io.File
     */
    public File rollbackNextBackUpedDataFile(String path) throws IOException {
        assert !path.contains(".."+slash) : "other directories are blocked";
        //создаем бэекап текущего, но только если уже нет такого же
        File f = getBackUpedDataFile(path);
        List<File> backUps = getBackUpFilesList(path, f);

        int num=-1;
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
        // увеличиваем на один. Если перевалили за размер - начинаем с начала.
        num--;
        if (num <0) {
            currentChosenBackUp.put(path, 0);
            throw new IndexOutOfBoundsException("Element is last one in list");
        }

        //запоминаем номер, на котором остановились, чтобы потом начать с него
        currentChosenBackUp.put(path, num);

        copyFile(backUps.get(num), f);
        return f;
    }

    /**
     * Возвращает файл из папки WEB-INF/data, предварительно вернув его из бэкапа
     * Каждый следующий вызов возвращает новую запись из бэкапа.
     * @param path путь формата "AMRLiRT/xml/CalculateDebtCapacityData.xml"
     * @return Java.io.File
     */
    public File rollbackPervBackUpedDataFile(String path) throws IOException {
        assert !path.contains(".."+slash) : "other directories are blocked";
        //создаем бэекап текущего, но только если уже нет такого же
        File f = getBackUpedDataFile(path);
        List<File> backUps = getBackUpFilesList(path, f);

        int num=backUps.size();
        try {
                long fhash = FileUtils.checksumCRC32(f);
                for (int i = 0; num >=backUps.size() && i< backUps.size(); i++) {
                    long bhash = FileUtils.checksumCRC32(backUps.get(i));
                    if (bhash == fhash) {
                        num = i;
                    }
                }
            } catch (IOException e) {
            num = backUps.size() - 1;
        }

        // увеличиваем на один. Если перевалили за размер - начинаем с начала.
        num++;
        if (num > backUps.size() - 1) {
            currentChosenBackUp.put(path, backUps.size() - 1);
            throw new IndexOutOfBoundsException("Element is last one in list");
        }
        //запоминаем номер, на котором остановились, чтобы потом начать с него
        currentChosenBackUp.put(path, num);

        copyFile(backUps.get(num), f);
        return f;
    }

    /**
     * проверяет, нуждается ли файл в бэкапе.
     * Должен проверить есть ли такой файл в папке с бэкапапми, если есть - не похож ли последний на этот
     * @param f файл для бэкапа
     * @return File если нужно. Null если бэк не нужен
     */
    protected File isFileNeedBackUp(String path, File f) throws IOException {
        String subPath = path.replace(f.getName(), "");
        File backDir = getBackUpDestinationDir(subPath, f);
        try {
            if (!backDir.exists()) throw new FileNotFoundException(backDir.getCanonicalPath());//дирректории нет - нуужно делать бэкап
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
            if (noBackUpFile) throw new Exception();//такой бэкап уже есть

            return null;

        } catch (Exception e){
            return new File(backDir.getPath() + slash + composeBackUpFilename(f.getName()));
        }

    }

    /**
     * Возвращает директорию, куда надо делать бэкап
     * не проверяет наличие дирректории
     */
    private File getBackUpDestinationDir(String path, File f) throws IOException {
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

        //класс чтобы файлы были точно отсоритированны как надо
        class FileComparator implements Comparator<File> {
            @Override
            public int compare(File a, File b) {
                return a.getName().compareTo(b.getName());
            }
        }

        String fname = f.getName();
        String fbasename = FilenameUtils.getBaseName(fname);
        String fextname = FilenameUtils.getExtension(fname);
        File backDir =  getBackUpDestinationDir(path, f);
        List files = new ArrayList();


        Pattern p = Pattern.compile("^" + fbasename + "\\.(\\d{8}_\\d{6})\\." + fextname + "$");

        if (backDir.exists()) {

            for (File bf : backDir.listFiles()) {
                Matcher m = p.matcher(bf.getName());
                if (m.find()) {
                    files.add(bf);
                }
            }

            Collections.sort(files, new FileComparator());
        }
        return files;
    }

    public String TranslateNameToPath(String name) throws FileNotFoundException, IOException{
        String path;
        if (!name.contains("_")) {
            throw new IllegalArgumentException("Error: Illegal Argument \"name\":=\""+name+"\"");
        }
        String[] parts = name.split("_");
        path = parts[0]+slash+"xml"+slash+parts[1]+"Data.xml";
        File file = new File(getWebInfPath()+slash+"data"+slash+path);
        if (file.exists()) return path; //Нашли файл. Если здесь не нашли - дальше пляски с бубном

        /*//просмотр папок, имя которых похоже на первую часть имени.
        File dataFolder = new File(getWebInfPath() + slash + "data");
        File[] catFolders =  dataFolder.listFiles();
        String category=null;
        for (File catFolder : catFolders) {
            if (catFolder.getName().toUpperCase().contains(parts[0].toUpperCase())) category = catFolder.getName();
        }
        if (category!=null) {
            //просмотр файлов внутри группы
            dataFolder = new File(getWebInfPath() + slash + "data"+slash+category+slash+"xml");
            File[] files =  dataFolder.listFiles();
            String dataFilename = null;
            for (File datafiles : files) {
                if (datafiles.getName().toUpperCase().contains(parts[1].toUpperCase())) dataFilename = datafiles.getName();
            }
            path = dataFolder+slash+dataFilename+".xml";
            file = new File(path);
            if (file.exists()) return path; //Нашли файл. Если здесь не нашли - значиьт видимо нет такого файла
        }*/
        throw new FileNotFoundException(path);
    }

    public void writeStringToFile(File f, String data) throws Exception {
        if (!f.exists()) {
            File dir = f.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            f.createNewFile();
        }
        PrintWriter out = new PrintWriter(f);
        try {
            out.print(data);
        } catch (Exception e) {
            throw e;
        } finally {
            out.close();
        }
    }

    public String getFileString(File file) throws IOException {
        return FileUtils.readFileToString(file);
    }


}
