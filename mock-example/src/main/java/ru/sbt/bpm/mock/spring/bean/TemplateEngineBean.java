package ru.sbt.bpm.mock.spring.bean;

import java.io.IOException;

/**
 * Created by sbt-bochev-as on 03.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */

/**
 * For Autowired annotation purpose interface
 */
public interface TemplateEngineBean {
    public String applyTemplate(String input) throws IOException;
    public String htmlInfo();
}
