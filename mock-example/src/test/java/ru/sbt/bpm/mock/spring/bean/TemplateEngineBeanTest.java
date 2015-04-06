package ru.sbt.bpm.mock.spring.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.sbt.bpm.mock.spring.bean.impl.TemplateEngineBeanImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by sbt-bochev-as on 03.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/env/mockapp-servlet.xml"})
public class TemplateEngineBeanTest {

    @Autowired
    private TemplateEngineBean templateEngineBean;

    @Test
    public void testApplyTemplate() throws Exception {
        assertTrue(
                ("__" + new SimpleDateFormat("YYYYMMddHHmm").format(new Date()) + "__")
                        .equals(templateEngineBean.applyTemplate("__${timestamp12}__"))
        );
    }
}