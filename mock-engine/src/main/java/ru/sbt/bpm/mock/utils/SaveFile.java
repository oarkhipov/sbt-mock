/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ru.sbt.bpm.mock.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SBT-Vostrikov-MI on 16.12.2014.
 */

@Slf4j
public class SaveFile {
    static String rootpath;
    private static SaveFile ourInstance = new SaveFile();
    public int logSize = 5;//TODO засунуть в конфиг
    protected String slash;
    private Map<String, Integer> currentChosenBackUp;
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

    private SaveFile() {
        slash = File.separator;
        currentChosenBackUp = new HashMap<String, Integer>();
    }

    /**
     * получает путь через ApplicationContext
     *
     * @param AppContext
     * @return
     */
    public static SaveFile getInstance(ApplicationContext AppContext) throws IOException {
        rootpath = AppContext.getResource("/WEB-INF").getFile().getCanonicalPath();
        return ourInstance;
    }

    /**
     * получает путь напрямую
     *
     * @return
     * @throws IOException
     */
    public static SaveFile getInstance(String RootPath) throws IOException {
        rootpath = RootPath;
        return ourInstance;
    }

    public String getWebInfPath() throws IOException {
        if (webInfPath != null) return webInfPath;
        webInfPath = rootpath;
        return webInfPath;
    }

    /*public String getResourcesPath() {
        if (resourcesPath!=null) return resourcesPath;
        resourcesPath = System.getProperty("user.dir") +slash+"src"+slash+"test"+slash+"resources";
        return resourcesPath;
    }*/

    public String getOutPath() throws IOException {
        if (outPath != null) return outPath;
        outPath = getWebInfPath() + slash + "backup";
        return outPath;
    }

    /**
     * Возвращает файл из папки WEB-INF/data
     *
     * @param path путь формата "AMRLiRT/xml/CalculateDebtCapacityData.xml"
     * @return Java.io.File
     */
    protected File getDataFile(String path) throws IOException {
        File f = new File(getWebInfPath() + slash + "data" + slash + path);
        log.debug("Trying to get file: " + f.getAbsolutePath());
        if (!f.exists()) {
            log.debug("File not found!");
            throw new FileNotFoundException();
        }
        return f;
    }

    /**
     * Возвращает файл из папки WEB-INF/data, предварительно сохранив бэкап
     *
     * @param path путь формата "AMRLiRT/xml/CalculateDebtCapacityData.xml"
     * @return Java.io.File
     */
    public File getBackUpedDataFile(String path) throws IOException {
        if (path.contains(".." + slash)) throw new FileNotFoundException("other directories are blocked");
        try {
            File f = getDataFile(path);
            File back = isFileNeedBackUp(path, f);
            if (back != null) {
                List<File> backUps = getBackUpFilesList(path, f);
                while (backUps.size() > logSize - 1) {
                    FileUtils.forceDelete(backUps.get(1));//получаем второй файл с конца, не первый. Это важно - нельзя удалять самый старый
                    backUps = getBackUpFilesList(path, f);
                }
                copyFile(f, back);
                return f;
            }
        } catch (FileNotFoundException e) {
            log.debug("No such file: " + path);
            return new File(path);
        }
        throw new IllegalStateException("unreachable code reached at method" + this.getClass().getPackage().toString() + "getBackUpedDataFile");
    }

    /**
     * Возвращает файл из бэкапа WEB-INF/data
     *
     * @param path путь формата "AMRLiRT/xml/CalculateDebtCapacityData.xml"
     * @return Java.io.File
     */
    public File restoreBackUpedDataFile(String path) throws IOException {
        return restoreBackUpedDataFile(path, 1);
    }

    public File restoreBackUpedDataFile(String path, int num) throws IOException {
        assert !path.contains(".." + slash) : "other directories are blocked";
        assert num >= 1 : "cant restore [" + num + "] backupfile";
        //создаем бэекап текущего, но только если уже нет такого же
        File f = getBackUpedDataFile(path);
        List<File> backUps = getBackUpFilesList(path, f);
        assert num < backUps.size() + 1 : "cant restore [" + num + "] backupfile. there is only [" + backUps.size() + "] backups";

        copyFile(backUps.get(num - 1), f);
        return f;
    }

    /**
     * Возвращает файл из папки WEB-INF/data, предварительно вернув его из бэкапа
     * Каждый следующий вызов возвращает новую запись из бэкапа.
     *
     * @param path путь формата "AMRLiRT/xml/CalculateDebtCapacityData.xml"
     * @return Java.io.File
     */
    public File getNextBackupDataFile(String path) throws IOException, IndexOutOfBoundsException {
        assert !path.contains(".." + slash) : "other directories are blocked";
        //создаем бэекап текущего, но только если уже нет такого же
        File f = getBackUpedDataFile(path);
        List<File> backUps = getBackUpFilesList(path, f);

        int num = -1;
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
        if (num < 0) num = backUps.size() - 1;

        //запоминаем номер, на котором остановились, чтобы потом начать с него
        currentChosenBackUp.put(path, num);

        //copyFile(backUps.get(num), f);
        return backUps.get(num);
    }


    /**
     * Возвращает файл из папки WEB-INF/data, предварительно вернув его из бэкапа
     * Каждый следующий вызов возвращает новую запись из бэкапа.
     *
     * @param path путь формата "AMRLiRT/xml/CalculateDebtCapacityData.xml"
     * @return Java.io.File
     */
    public File rollbackNextBackupDataFile(String path) throws IOException {
        assert !path.contains(".." + slash) : "other directories are blocked";
        //создаем бэекап текущего, но только если уже нет такого же
        File f = getBackUpedDataFile(path);
        List<File> backUps = getBackUpFilesList(path, f);

        int num = -1;
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
        if (num < 0) {
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
     *
     * @param path путь формата "AMRLiRT/xml/CalculateDebtCapacityData.xml"
     * @return Java.io.File
     */
    public File rollbackPervBackUpedDataFile(String path) throws IOException {
        assert !path.contains(".." + slash) : "other directories are blocked";
        //создаем бэекап текущего, но только если уже нет такого же
        File f = getBackUpedDataFile(path);
        List<File> backUps = getBackUpFilesList(path, f);

        int num = backUps.size();
        try {
            long fhash = FileUtils.checksumCRC32(f);
            for (int i = 0; num >= backUps.size() && i < backUps.size(); i++) {
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
     *
     * @param f файл для бэкапа
     * @return File если нужно. Null если бэк не нужен
     */
    protected File isFileNeedBackUp(String path, File f) throws IOException {
        String subPath = path.replace(f.getName(), "");
        File backDir = getBackUpDestinationDir(subPath, f);
        try {
            if (!backDir.exists())
                throw new FileNotFoundException(backDir.getCanonicalPath());//дирректории нет - нуужно делать бэкап
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

        } catch (Exception e) {
            return new File(backDir.getPath() + slash + composeBackUpFilename(f.getName()));
        }

    }

    /**
     * Возвращает директорию, куда надо делать бэкап
     * не проверяет наличие дирректории
     */
    private File getBackUpDestinationDir(String path, File f) throws IOException {
        String subPath = path.replace(f.getName(), "");
        File dir = new File(getOutPath() + slash + subPath);
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
        } catch (IOException e) {

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
            return m.replaceFirst("$1." + date + ".$2");
        }
        throw new IllegalArgumentException("wrong filename : [" + filename + "]");
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
        if (!bname.contains(fbasename)) return false;
        if (!bname.contains(fextname)) return false;

        Pattern p = Pattern.compile("^[\\w,\\s-]+\\.(\\d{8}_\\d{6})\\.[\\w]{3}$");
        Matcher m = p.matcher(bname);
        if (!m.find())
            return false; //это какой-то не нами созданный файл. или создан позднее 9999 года - тогда программа будет работать странно.

        if (bold == null) { //если на этом этапе файл подходит - он может быть бэкапом
            return true;
        }
        String dateA = m.group(1);
        Matcher mB = p.matcher(bold.getName());
        mB.find();//по идее если файл оказался как bold, то мы его уже проверили регуляркой
        String dateB = mB.group(1);
        return dateA.compareTo(dateB) > 0; //сравниваем строково. Даты одинакового формата, дата по убыванию. Должна вернуть более позднюю.
    }

    protected void copyFile(File f, File back) throws IOException {
        FileUtils.copyFile(f, back);
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
        File backDir = getBackUpDestinationDir(path, f);
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

    public String TranslateNameToPath(String name) throws IOException {
        String path;
        if (!name.contains("__")) {
            throw new IllegalArgumentException("Error: Illegal Argument \"name\":=\"" + name + "\"");
        }
        String[] parts = name.split("__");
        path = StringUtils.join(parts, slash);
        log.debug("Translated: " + name + " to path: " + path);
        return getWebInfPath() + slash + "data" + slash + path;
    }

    public String TranslateNameToSystem(String name) throws IOException {
        String path;
        if (!name.contains("__")) {
            throw new IllegalArgumentException("Error: Illegal Argument \"name\":=\"" + name + "\"");
        }
        String[] parts = name.split("__");
        path = parts[0];
        File file = new File(getWebInfPath() + slash + "data" + slash + path);
        if (file.exists()) return parts[0];

        throw new FileNotFoundException("Directory [" + parts[0] + "] not found");
    }

    public void writeStringToFile(File f, String data) throws Exception {
        log.debug("Writing string to file: " + f.getAbsolutePath());
        if (!f.exists()) {
            File dir = f.getParentFile();
            if (!dir.exists()) {
                log.debug("Creating directory tree: " + dir.getAbsolutePath());
                dir.mkdirs();
            }
            log.debug("Create empty file: " + f.getAbsolutePath());
            f.createNewFile();
        }
        try {
            FileUtils.writeStringToFile(f, data, Charset.forName("UTF-8"));
            log.debug("String was written to file: " + f.getAbsoluteFile());
        } catch (Exception e) {
            throw e;
        }
    }

    public String getFileString(File file) throws IOException {
        return FileUtils.readFileToString(file, Charset.forName("UTF-8"));
    }


}
