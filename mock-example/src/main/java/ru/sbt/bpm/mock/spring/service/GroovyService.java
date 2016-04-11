package ru.sbt.bpm.mock.spring.service;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;

import java.io.File;
import java.io.IOException;
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
    public static final String EXTRACTED_VARIABLE_PREFIX = "extracted_variable_";
    private Pattern expressionPattern = Pattern.compile("(.*)(\\$\\{=.*?\\})(.*)");
    private static final String GROOVY_IMPORTS =
            "import java.io.*;\n" +
                    "import freemarker.template.*;\n";
    private static final String GROOVY_INIT =
            "class RespObj {\n" +
                    "def properties = [:]\n" +
                    "def getProperty(String name) { properties[name] }\n" +
                    "def getProperties() { properties }\n" +
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

        final File tempSrcFile = new File("mock_" + System.currentTimeMillis() + ".dat");
        final File tempDstFile = new File("mockMod_" + System.currentTimeMillis() + ".dat");
        try {
            Tuple2<String, String> extractionResult = extractVariablesFromInlineExpressions(mockXml);
            String extractionVariables = extractionResult.getT1();
            mockXml = extractionResult.getT2();

            log.debug("write mockXml[" + mockXml + "] to [" + tempSrcFile.getAbsolutePath() + "]");
            FileUtils.writeStringToFile(tempSrcFile, mockXml);
            final String tempSrcFileAbsolutePath = tempSrcFile.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\");
            final String tempDstFileAbsolutePath = tempDstFile.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\");
            final String tempSrcFileName = tempSrcFile.getName();
            final String tempSrcFileDir = tempSrcFileAbsolutePath.substring(0, tempSrcFileAbsolutePath.length() - tempSrcFileName.length());

            String scriptText = GROOVY_IMPORTS + GROOVY_INIT + script + "\n" + extractionVariables + "\n" +
                    "def tempDstFile = \"" + tempDstFileAbsolutePath + "\"\n" +
                    "def tempSrcDir = \"" + tempSrcFileDir + "\"\n" +
                    "def tempSrcName = \"" + tempSrcFileName + "\"\n" +
                    "FileOutputStream fileOutputStream = new FileOutputStream(new File(tempDstFile))\n" +
                    "OutputStreamWriter streamWriter = new OutputStreamWriter(fileOutputStream)\n" +
                    "Configuration freeConfiguration = new Configuration();\n" +
                    "freeConfiguration.directoryForTemplateLoading = new File(tempSrcDir);\n" +
                    "def freeTemplate = freeConfiguration.getTemplate(tempSrcName)\n" +
                    "freeTemplate.process(response.getProperties(), streamWriter )\n" +
                    "fileOutputStream.close()\n";

            shell.evaluate(scriptText);

            mockXml = FileUtils.readFileToString(tempDstFile);
        } finally {
            log.debug("delete temporary file [" + tempSrcFile.getAbsolutePath() + "]");
            FileUtils.deleteQuietly(tempSrcFile);
            log.debug("delete temporary file [" + tempDstFile.getAbsolutePath() + "]");
            FileUtils.deleteQuietly(tempDstFile);
        }
        return mockXml;
    }

    public Tuple2<String, String> extractVariablesFromInlineExpressions(String content) throws IOException {
        log.debug("Begin content variable extraction");
        StringBuilder extractedVariablesStringBuilder = new StringBuilder();
        if (content.contains("${=")) {
            Matcher expressionMatcher = expressionPattern.matcher(content);
            log.debug("matcher created");
            while (expressionMatcher.find()) {
                log.debug("new matcher find iteration");
                String expression = expressionMatcher.group(2).substring(3);
                expression = expression.substring(0, expression.length() - 1);
                String varName = EXTRACTED_VARIABLE_PREFIX + System.currentTimeMillis();
                extractedVariablesStringBuilder
                        .append("response.")
                        .append(varName)
                        .append(" = ")
                        .append(expression)
                        .append("\n");
                content = expressionMatcher.replaceFirst("$1\\$\\{" + varName + "\\}$3");
                log.debug(varName + " variable extracted");
                if (content.contains("${=")) {
                    expressionMatcher = expressionPattern.matcher(content);
                    log.debug("matcher updated");
                } else {
                    break;
                }
            }
            log.debug("Variables:\n" + extractedVariablesStringBuilder.toString());
            log.debug("Modified content: " + content);
        } else {
            log.debug("No expressions");
        }
        log.debug("Extraction Finished!");
        return Tuple.of(extractedVariablesStringBuilder.toString(), content);
    }

    public String info() {
        return "Script is invoked with log, request and response variables";
    }
}
