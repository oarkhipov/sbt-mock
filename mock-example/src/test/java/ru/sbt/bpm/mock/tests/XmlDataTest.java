package ru.sbt.bpm.mock.tests;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbt.bpm.mock.service.TransformService;

import java.io.IOException;

/**
 * Created by sbt-bochev-as on 13.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
public class XmlDataTest {
    @Autowired
    TransformService transformService;

    @Test
    public void getTest() throws IOException {
        transformService.getXmlData("/WEB-INF/web.xml");
    }
}
