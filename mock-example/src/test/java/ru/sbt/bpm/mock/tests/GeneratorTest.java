package ru.sbt.bpm.mock.tests;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
// тест генератора контекста на чтение
public class GeneratorTest {

    @Test
    public void testAllMockxsl() throws Exception {
        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        String rootpath = dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl";
        File xslDir = new File(rootpath);

        assert xslDir.isDirectory() : "попали не в ту папку";
        File[] files = xslDir.listFiles();
        for(File f : files) {
            List<File> xslt = new ArrayList<File>();
            if (!f.getName().equals("util") && f.isDirectory()) {
                getXSLTFiles(xslt, f);

                for(File fx : xslt)
                {
                    String name = FilenameUtils.removeExtension(fx.getName());
                    String system = f.getName();
                    if (checkFileIsMockOrDriver(fx)) {
                        String resultSubPath = "xml/" + f.getName() + "/" + name + "/";

                        testXSLTmock(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\" + system + "\\" + name + ".xsl",
                                dir + "\\..\\..\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq1.xml",
                                "xml/" + system + "/" + name + "/rs1.xml");
                        System.out.println(name + " part one Done!");

                        String secondRq = FileUtils.readFileToString(new File(dir + "\\..\\..\\src\\test\\resources\\xml\\"+system+"\\"+name+"\\rq2.xml"));

                        if (secondRq.contains(">test1<")) { //проверяем второй запрос только если там содержится linked tag

                            testXSLTmock(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\" + system + "\\" + name + ".xsl",
                                    dir + "\\..\\..\\src\\test\\resources\\xml\\" + system + "\\" + name + "\\rq2.xml",
                                    "xml/" + system + "/" + name + "/rs2.xml");
                            System.out.println(name + " part two Done!");
                        }

                        System.out.println();
                        System.out.println(name + " Mock Done!");
                        System.out.println();
                    } else {

                        checkXSLTdriver(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\"+system+"\\"+name+".xsl",
                                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\"+system+"\\xml\\"+name+"Data.xml",
                                "xml/"+system+"/"+name+"/rq1.xml");
                        System.out.println(name + " part one Done!");

                        Map<String, String> params = new HashMap<String, String>(1);
                        params.put("name","test1");
                        checkXSLTdriver(dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\xsl\\"+system+"\\"+name+".xsl",
                                dir + "\\..\\..\\src\\main\\webapp\\WEB-INF\\data\\"+system+"\\xml\\"+name+"Data.xml",
                                "xml/"+system+"/"+name+"/rq2.xml", params);
                        System.out.println(name + " part two Done!");

                        System.out.println();
                        System.out.println(name + " Driver Done!");
                        System.out.println();
                    }
                }
            }
        }
    }

    /**
     * проверяем, явлется ли xsl драйвером или заглушкой
     * @throws Exception
     */
    private boolean checkFileIsMockOrDriver(File f) throws Exception {
        String xml = FileUtils.readFileToString(f);
        if (xml.contains("<xsl:template match=\"/\">")) {
            return false; //драйвер
        } else if (xml.contains("<xsl:template match=\"soap:Envelope\">") || xml.contains("<xsl:template match=\"soap:Message\">")) {
            return true; //заглушка
        }
        throw new IllegalArgumentException("file is nor driver nor mock");
    }

    /**
     * получить все xsl-файлы из папки
     * @param xslts Out выходной список файлов
     * @param file входной файл/дирректория
     */
    private void getXSLTFiles(List<File> xslts, File file) {
        if (file.isDirectory()) {
            for(File f : file.listFiles()) {
                getXSLTFiles(xslts, f);
            }
        } else {
            if (file.getName().endsWith(".xsl")) {
                xslts.add(file);
            }
        }
    }

    private void testXSLTmock(String XSLTFile, String XMLFile, String validateFile) throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        String result = new String();
        String validateFileXML = FileUtils.readFileToString(new File(dir + validateFile));

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        Diff diff = new Diff(validateFileXML,result);
        if (!diff.identical()) {
            DetailedDiff detailedDiff = new DetailedDiff(diff);
            List differences = detailedDiff.getAllDifferences();
            for (Object difference : differences) {
                System.out.println("***********************");
                System.out.println(String.valueOf((Difference) difference));
            }

            assertEquals(validateFileXML, result);
        }

    }

    protected void checkXSLTdriver (String XSLTFile, String XMLFile, String validateFile ) throws Exception {
        checkXSLTdriver(XSLTFile, XMLFile, validateFile, null);
    }

    protected void checkXSLTdriver (String XSLTFile, String XMLFile, String validateFile, Map<String,String> params ) throws Exception {

        final String dir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(dir);
        String result = "";
        String validateFileXML = FileUtils.readFileToString(new File(dir + validateFile));

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        Diff diff = new Diff(validateFileXML,result);
        if (!diff.identical()) {
            DetailedDiff detailedDiff = new DetailedDiff(diff);
            List differences = detailedDiff.getAllDifferences();
            for (Object difference : differences) {
                System.out.println("***********************");
                System.out.println(String.valueOf((Difference) difference));
            }

            assertEquals(validateFileXML, result);
        }
    }
}
