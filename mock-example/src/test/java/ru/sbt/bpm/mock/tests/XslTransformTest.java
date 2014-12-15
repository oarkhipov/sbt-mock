package ru.sbt.bpm.mock.tests;


import org.apache.commons.io.IOUtils;
import org.junit.Test;
import ru.sbt.bpm.mock.utils.XslTransformer;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringWriter;

/**
 * Created by sbt-bochev-as on 12.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
public class XslTransformTest {

    @Test
    public void firstTest() throws TransformerException {
        System.out.println(
                XslTransformer.transform("C:\\work\\IdeaProjects\\GitProjects\\Mock\\mock-example\\src\\main\\webapp\\WEB-INF\\xsl\\CRM\\ForceSignal.xsl",
                        "C:\\work\\IdeaProjects\\GitProjects\\Mock\\mock-example\\src\\test\\resources\\xml\\CRM\\ForceSignal\\rq1.xml")
        );
    }

}
