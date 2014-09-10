/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.sbt.bpm.mock.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileUtils;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

/**
 *
 * @author sbt-barinov-sv
 */
public class TransformService {
    private final Map<String, String> map;
    private final TransformerFactory factory;
    
    public TransformService(Set<String> files) throws IOException {
        factory = TransformerFactory.newInstance();
        map = new HashMap<String, String>();
        for(String fname:files) {
            String xsl = FileUtils.readFileToString(new File(fname));
            map.put(fname.replace('/', '_'), xsl);
        }
    }
    
    public String transform(String key, String message) throws TransformerException {
        String xsl = (String)map.get(key);
        Transformer transformer = factory.newTransformer(new StringSource(xsl));
        Result result = new StringResult();
        transformer.transform(new StringSource(message), result);
        return result.toString();
    }
    
    public String getXSL(String name) {
        return (String)map.get(name);
    }
    
    public void putXSL(String name, String value) {
        map.put(name, value);
    }
    
    public Set<String> keySet() {
        return map.keySet();
    }
}
