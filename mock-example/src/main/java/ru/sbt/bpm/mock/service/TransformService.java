/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.sbt.bpm.mock.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import javax.xml.transform.*;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import ru.sbt.bpm.mock.bean.RefreshableXSLTransformer;

/**
 *
 * @author sbt-barinov-sv
 */
public class TransformService {
    @Autowired
    private ApplicationContext appContext;
    
    public String transform(String transformerName, String message) throws TransformerException {
        return appContext.getBean(transformerName, RefreshableXSLTransformer.class).transform(message);
    }
    
    public String getXSL(String name) {
        return appContext.getBean(name, RefreshableXSLTransformer.class).getXSL();
    }
    
    public void putXSL(String name, String value) {
        appContext.getBean(name, RefreshableXSLTransformer.class).setXSL(value);
    }

    public String getXmlData(String name) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream xmlStream = classLoader.getResourceAsStream(name);
        return IOUtils.toString(xmlStream, "UTF-8");
    }
    
    public Collection<String> getTransformers() {
        return Arrays.asList(appContext.getBeanNamesForType(RefreshableXSLTransformer.class));
    }
}
