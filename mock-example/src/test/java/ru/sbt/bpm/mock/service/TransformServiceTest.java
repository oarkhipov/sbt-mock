/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.sbt.bpm.mock.service;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author sbt-barinov-sv
 */
public class TransformServiceTest extends TestCase {
    
    public TransformServiceTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of transform method, of class TransformService.
     */
    public void testTransform() throws Exception {
        System.out.println("transform");
        String key = "src/main/webapp/WEB-INF/xsl/SendBasicDocumentInformation.xsl";
        String message = FileUtils.readFileToString(new File("src/main/webapp/WEB-INF/example/vivat.xml"));
        String expResult = FileUtils.readFileToString(new File("src/main/webapp/WEB-INF/example/vivat-result.xml"));
        
        Set<String> files = new HashSet<String>();
        files.add(key);
        key = key.replace('/', '_');
        TransformService instance = new TransformService(files);
        String result = instance.transform(key, message);
        result = result.replace('\n',' ').replace('\r',' ').replaceAll(".*<title>","").replaceAll("</title>.*", "");
        assertEquals("SendBasicDocumentInformation.xsl", result);
    }  
    
}
