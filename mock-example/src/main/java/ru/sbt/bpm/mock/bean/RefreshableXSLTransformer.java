/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.sbt.bpm.mock.bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.Message;
import org.springframework.integration.message.GenericMessage;
import org.springframework.integration.xml.transformer.XsltPayloadTransformer;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

/**
 *
 * @author sbt-barinov-sv
 */
public class RefreshableXSLTransformer {
    @Autowired
    private ApplicationContext appContext;
    
    private XsltPayloadTransformer delegate;
    private String xsl;
    
    public void setXslPath(String path) throws Exception {
        Resource resource = appContext.getResource(path);
        delegate = new XsltPayloadTransformer(resource);
        xsl = FileUtils.readFileToString(resource.getFile());
    }
    public void setXSL(String xsl) {
    	System.out.println("Mock XSL start working");
        XsltPayloadTransformer backup = delegate;
        try {
            Resource resource = new ByteArrayResource(xsl.getBytes("UTF-8"));
            delegate = new XsltPayloadTransformer(resource);
            this.xsl = xsl;
        } catch(Exception e) {
        	System.out.println("Mock XSL some error: "+e.getMessage());
            e.printStackTrace(System.out);
            delegate = backup;        
        }
    }
    public String getXSL() {
        return xsl;
    }    
    public String transform(String message) throws TransformerException {
        Message msg = new GenericMessage(message);
        return String.valueOf(delegate.transform(msg).getPayload());
    }
}