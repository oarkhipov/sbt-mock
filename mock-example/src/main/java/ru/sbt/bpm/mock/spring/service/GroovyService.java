package ru.sbt.bpm.mock.spring.service;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sbt-bochev-as on 02.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Slf4j
@Service
public class GroovyService {

    private String groovyInit = "class RespObj {\n" +
            "def properties = [:]\n" +
            "def getProperty(String name) { properties[name] }\n" +
            "void setProperty(String name, value) { properties[name] = value }\n" +
            "}\n" +
            "def response = new RespObj()\n" +
            "def requestDom \n" +
            "if(request){ requestDom = new XmlParser().parseText(request)}\n" +
            "\n";

    public String execute(String incomeXml, String mockXml, String script) throws Exception {
        Binding binding = new Binding();
        binding.setProperty("log", log);
        binding.setProperty("request", incomeXml);
        GroovyShell shell = new GroovyShell(binding);

        //TODO remove __ from replace
        //generate xml
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(mockXml);
        if (matcher.find()) {
            mockXml = matcher.replaceAll("\\$\\{response.$1\\}");
        }
        //save xml
//        final File tempFile = new File(System.currentTimeMillis() + ".dat");
//        log.debug("write mockXml to [" + tempFile.getAbsolutePath() + "]");
//        FileUtils.writeStringToFile(tempFile, mockXml);
//        final String s = tempFile.getAbsolutePath().replaceAll("\\\\","\\\\\\\\");

        //TODO move xml to file and evaliate it when read
        String scriptText = groovyInit + script + "\n" +
                "mockXml=\"\"\"" + mockXml + "\"\"\"";
//                "mockXml = new File('" + s +"').text \n" +
//                "def out = Eval.me(\"response\", response.getProperties(), \"\\\"\" + mockXml.replaceAll(\"\\\"\",\"\\\\\\\"\") + \"\\\"\" )\n" +
//                "mockXml = out";
        log.debug("Executing groovy script:\n" +
                "###########################################################################\n" +
                scriptText + "\n" +
                "###########################################################################\n");
        shell.evaluate(scriptText);

//        log.debug("delete temporary file [" + tempFile.getAbsolutePath() + "]");
//        FileUtils.deleteQuietly(tempFile);
        return shell.getVariable("mockXml").toString();
    }

    public String info() {
        return "Script is invoked with log, request and response variables";
    }
}
