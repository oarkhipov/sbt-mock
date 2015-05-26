package ru.sbt.bpm.mock.generator.util.TemplateEngine.impl;

import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.generator.util.TemplateEngine.ConfigTemplate;

/**
 * Created by sbt-vostrikov-mi on 23.04.2015.
 */
public class FileNameTemplateImpl implements ConfigTemplate {
    private static FileNameTemplateImpl ourInstance = new FileNameTemplateImpl();

    public static FileNameTemplateImpl getInstance() {
        return ourInstance;
    }

    private FileNameTemplateImpl() {
    }


    public String getValue(MockConfig config) {
        return config.getFilename();
    }
    public String getValue(MockConfig config, String[] Args) {
        return getValue(config);
    }
}

