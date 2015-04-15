package ru.sbt.bpm.mock.generator.util.TemplateEngine.impl;

import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.generator.util.TemplateEngine.ConfigTemplate;

/**
 * Created by sbt-vostrikov-mi on 10.04.2015.
 */
public class TestConfigTemplateImpl implements ConfigTemplate {
    private static TestConfigTemplateImpl ourInstance = new TestConfigTemplateImpl();

    public static TestConfigTemplateImpl getInstance() {
        return ourInstance;
    }

    private TestConfigTemplateImpl() {
    }


    public String getValue(MockConfig config) {
        return getValue();
    }
    public String getValue(MockConfig config, String[] Args) {
        return getValue();
    }

    public String getValue() {
        return "test";
    }
}
