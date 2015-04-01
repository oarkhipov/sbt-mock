/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.sbt.bpm.mock.spring.integration.service;

import java.util.Arrays;
import java.util.Collection;
import javax.xml.transform.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import ru.sbt.bpm.mock.spring.integration.bean.RefreshableXSLTransformer;

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

    public Collection<String> getTransformers() {
        return Arrays.asList(appContext.getBeanNamesForType(RefreshableXSLTransformer.class));
    }
}
