package ru.sbt.bpm.mock.generator.util.TemplateEngine;

import ru.sbt.bpm.mock.config.MockConfig;

/**
 * Created by sbt-vostrikov-mi on 10.04.2015.
 */
public interface ConfigTemplate {

    public String getValue(MockConfig config);
    public String getValue(MockConfig config, String[] Args);
}
