/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.sbt.bpm.mock.service;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author sbt-barinov-sv
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/mockapp-servlet.xml"})
public class TransformServiceTest extends TestCase {
    @Autowired
    ApplicationContext appContext;
    
    @Autowired
    TransformService service;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    public void testResource() throws Exception {
        System.out.println(FileUtils.readFileToString(appContext.getResource("WEB-INF/web.xml").getFile()));
        
    }
    /**
     * Test of transform method, of class TransformService.
     */
    @Test
    public void testTransform() throws Exception {
        System.out.println("transform");
        
        String message = FileUtils.readFileToString(appContext.getResource("WEB-INF/request/vivat.xml").getFile());
        String result = service.transform("sendInfo", message);
        System.out.println(result);
        result = result.replace('\n',' ').replace('\r',' ').replaceAll(".*<title>","").replaceAll("</title>.*", "");
        assertEquals("SendBasicDocumentInformation.xsl", result);
    }  
    
}
