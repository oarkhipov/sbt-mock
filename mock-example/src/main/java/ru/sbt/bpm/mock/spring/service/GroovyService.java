package ru.sbt.bpm.mock.spring.service;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sbt-bochev-as on 02.12.2015.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Log
@Service
public class GroovyService {

    private String groovyInit = "class RespObj {\n" +
            "def properties = [:]\n" +
            "def getProperty(String name) { properties[name] }\n" +
            "void setProperty(String name, value) { properties[name] = value }\n" +
            "}\n" +
            "def response = new RespObj()\n" +
            "\n";

    public String compile(String incomeXml, String mockXml, String script) throws Exception {
        Binding binding = new Binding();
        binding.setProperty("log", log);
        binding.setProperty("request", incomeXml);
        GroovyShell shell = new GroovyShell();
        //fill response object
//        shell.evaluate(groovyInit + script);

        //generate xml
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(mockXml);
        matcher.find();
        mockXml = matcher.replaceAll("\\$\\{response.$1\\}");
//        String inlineMockXml = mockXml.replaceAll("\\r", "").replaceAll("\\n", "");
        shell.evaluate(groovyInit + script + "\n mockXml = \"\"\"" + mockXml + "\"\"\"");

        return shell.getVariable("mockXml").toString();
    }

    public String info() {
        return "Script is invoked with log, request and response variables";
    }
}
