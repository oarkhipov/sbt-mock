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

package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmValue;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.entities.Systems;
import ru.sbt.bpm.mock.utils.XmlUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

/**
 * Created by sbt-hodakovskiy-da on 14.06.2016.
 */

@Slf4j
@Service
public class XsdAnalysisService {

    // xPath
    private static final String LOCAL_NAME_SCHEMA_TARGET_NAMESPACE = "//*[local-name()='schema']/@targetNamespace";
    private static final String LOCAL_NAME_IMPORT_NAMESPACE = "//*[local-name()='import']/@namespace";
    private static final String XPATH_ELEMENT_ATTRIBUTE_NAME = "//*[local-name()='element']/@name";

    // RegExp
    private static final Comparator<String> STRING_COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            if (o1 == o2)
                return 0;
            if (o1 == null)
                return -1;
            if (o2 == null)
                return 1;
            return o1.compareTo(o2);
        }
    };

    @Autowired
    private MockConfigContainer configContainer;

    @Autowired
    private DataFileService dataFileService;

    // Maps
    private Map<String, Set<String>> mapNamespacesByXpath = new HashMap<String,
            Set<String>>();
    private Map<String, Map<String, List<Tuple2<String, String>>>> mapOfElements = new HashMap<String,
            Map<String, List<Tuple2<String, String>>>>();


    public void reInit() throws IOException, SaxonApiException {
        mapNamespacesByXpath.clear();
        mapOfElements.clear();
        init();
    }

    @PostConstruct
    private void init() throws IOException, SaxonApiException {
        Systems systemContainer = configContainer.getConfig().getSystems();
        if (systemContainer != null) {
            Set<System> systems = systemContainer.getSystems();
            if (systems != null) {
                for (System system : systems) {
                    init(system);
                }
            }
        }
    }

    protected void init (System system) throws IOException, SaxonApiException {
        String systemName = system.getSystemName();
        log.info("Init XSD Analyzer for system: " + systemName);
        try {
            File systemPath = dataFileService.getSystemXsdDirectoryFile(systemName).getCanonicalFile();
            List<File> systemXsdFile = getFilesFromDir(systemPath);
            fillElementsMap(systemName, systemXsdFile);
            fillMapNamespace(systemName, systemXsdFile);
        } catch (Exception e) {
            log.error("Unable to init Analyzer for system: " + systemName, e);
        }
    }

    public void reInit(String systemName) throws IOException, SaxonApiException {
        System system = configContainer.getConfig().getSystems().getSystemByName(systemName);
        mapNamespacesByXpath.remove(systemName);
        mapOfElements.remove(systemName);
        init(system);
    }

    public Set<String> getNamespaceFromXsd(String systemName) {
        return mapNamespacesByXpath.get(systemName);
    }

    public List<Tuple2<String, String>> getElementsForSystem (String systemName, String namespaceName) {
        if (mapOfElements.get(systemName) != null)
            return mapOfElements.get(systemName).get(namespaceName);
        else
            return new ArrayList<Tuple2<String, String>>();
    }

    public List<Tuple2<String, String>> getElementsForSystem (String systemName) {
        List<Tuple2<String, String>> list = new ArrayList<Tuple2<String, String>>();
        for (String namespace : mapOfElements.get(systemName).keySet())
            list.addAll(getElementsForSystem(systemName, namespace));
        return list;
    }

    private void fillElementsMap (String systemName, List<File> files) throws IOException, SaxonApiException {
        Map<String, List<Tuple2<String, String>>> mapElementsByNamespace = new HashMap<String, List<Tuple2<String, String>>>();
        printLog(systemName);
        for (File xsdFile : files) {
            log.debug("analyze file: " + xsdFile.getName());
            String inputXml = readFileWithoutBOM(xsdFile);
            if (inputXml.length() > 0) {
                mapElementsByNamespace.putAll(getNamespaceByxPath(inputXml, LOCAL_NAME_SCHEMA_TARGET_NAMESPACE, XPATH_ELEMENT_ATTRIBUTE_NAME));
            }
        }
        mapOfElements.put(systemName, mapElementsByNamespace);
    }

    private List<Tuple2<String, String>> getElementFromXsd(String inputXml, String xPath, String namespace) throws SaxonApiException {
        List<Tuple2<String, String>> set = new ArrayList<Tuple2<String, String>>();
        XdmValue xdmValue = XmlUtils.evaluateXpath(inputXml, xPath);
        for (int i = 0; i < xdmValue.size(); i++) {
            String element = xdmValue.itemAt(i).getStringValue();
            set.add(Tuple.of(namespace, element));
            log.debug(String.format("Element name: %s", element));
        }
        return set;
    }

    private void printLog(String systemName) {
        log.debug("====================================================================");
        log.debug("");
        log.debug(String.format("      READ XSD FILES FOR SYSTEM: %s", systemName));
        log.debug("");
        log.debug("====================================================================");
    }

    /**
     * Получение namespace из файла xsd по xPath
     *
     * @param inputXml
     * @param xPathNamespace
     * @param xPathElement
     * @throws SaxonApiException
     */
    private Map<String, List<Tuple2<String, String>>> getNamespaceByxPath(String inputXml, String xPathNamespace, String xPathElement)
            throws SaxonApiException {

        Map<String, List<Tuple2<String, String>>> map = new TreeMap<String, List<Tuple2<String, String>>>(STRING_COMPARATOR);
        if (inputXml.length()>0) {
            String namespace;
            XdmValue xdmValue = XmlUtils.evaluateXpath(inputXml, xPathNamespace);
            for (int i = 0; i < xdmValue.size(); i++) {
                namespace = xdmValue.itemAt(i).getStringValue();
                log.debug(String.format("Namespace: %s", namespace));
                map.put(namespace, getElementFromXsd(inputXml, xPathElement, namespace));
            }
        }
        return map;
    }

    private void fillMapNamespace (String systemName, List<File> files) throws IOException, SaxonApiException {
        Set<String> setXsdNamespace = new TreeSet<String>(STRING_COMPARATOR);
        printLog(systemName);
        for (File xsdFile : files) {
            log.debug("Read file: " + xsdFile.getName());
            String inputXml = readFileWithoutBOM(xsdFile);
            // Проходим по target
            setXsdNamespace.addAll(getNamespaceByxPath(inputXml, LOCAL_NAME_SCHEMA_TARGET_NAMESPACE));

            // Проходим по import
            setXsdNamespace.addAll(getNamespaceByxPath(inputXml, LOCAL_NAME_IMPORT_NAMESPACE));
        }
        mapNamespacesByXpath.put(systemName, setXsdNamespace);
    }

    /**
     * Получение namespace из файла xsd по xPath
     *
     * @param inputXml
     * @param xPath
     * @return
     * @throws SaxonApiException
     */
    private Set<String> getNamespaceByxPath(String inputXml, String xPath) throws SaxonApiException {
        Set<String> set = new TreeSet<String>(STRING_COMPARATOR);
        if (inputXml.length()>0) {
            XdmValue xdmValue = XmlUtils.evaluateXpath(inputXml, xPath);
            for (int i = 0; i < xdmValue.size(); i++) {
                String namespace = xdmValue.itemAt(i).getStringValue();
                log.debug(String.format("Namespace: %s", namespace));
                set.add(namespace);
            }
        }
        return set;
    }

    private String readFileWithoutBOM(File file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new BOMInputStream(new FileInputStream(file), false)));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line).append(" ");
        br.close();
        return sb.toString();
    }

    /**
     * Получение списка xsd фалов для системы
     *
     * @param dir - директория xsd файлов для системы
     * @return список xsd файлов системы
     */
    private List<File> getFilesFromDir(File dir) {
        List<File> listFiles = new ArrayList<File>();
        if (dir != null && dir.listFiles() != null) {
            for (File fileEntry : dir.listFiles())
                if (fileEntry.isDirectory())
                    listFiles.addAll(getFilesFromDir(fileEntry));
                else
                    listFiles.add(fileEntry);
        }
        return listFiles;
    }
}
