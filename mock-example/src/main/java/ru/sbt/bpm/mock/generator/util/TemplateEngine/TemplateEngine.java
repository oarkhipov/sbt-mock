package ru.sbt.bpm.mock.generator.util.TemplateEngine;

import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.generator.spring.integration.GatewayContextGenerator;
import ru.sbt.bpm.mock.generator.util.TemplateEngine.impl.*;
import ru.sbt.bpm.mock.spring.bean.TemplateEngineBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-vostrikov-mi on 10.04.2015.
 */
public class TemplateEngine  implements TemplateEngineBean {
    private Map<String, ConfigTemplate> templates;
    MockConfig config;
    private static TemplateEngine ourInstance;

    public static TemplateEngine getInstance(MockConfig config) {
        if (ourInstance == null) {
            ourInstance = new TemplateEngine(config);
        }
        return ourInstance;
    }

    private TemplateEngine(MockConfig config) {
        this.config = config;
        templates = new HashMap<String, ConfigTemplate>();
        templates.put("test", TestConfigTemplateImpl.getInstance());
        templates.put("GATEWAY", GatewayContextGenerator.getInstance());
        templates.put("CHANELS", ChannelTemplateImpl.getInstance());
        templates.put("ROUTER_MAPPING", RouterMappingTemplateImpl.getInstance());
        templates.put("BEANS_RefreshableXSLTransformer", BeanTransformerTemplateImpl.getInstance());
        templates.put("TRANSFORMERS", TransformerTemplateImpl.getInstance());
        templates.put("CONFIG_FILENAME", FileNameTemplateImpl.getInstance());
    }

    public String applyTemplate(String input) {
        StringBuilder resuslt = new StringBuilder();

        for (int i = 0; i<input.length(); i++) {
            if (i<input.length()-3 //мимнимальная длина шаблона - 4 символа (${.}). поэтому в конце строки шаблоны искать бессмысленно
                    && input.charAt(i)=='$'
                    && input.charAt(i+1)=='{') {
                int j;
                boolean applyedTemplate = false;
                for (j=2; i+j<input.length() && input.charAt(i+j)!='}'; j++);
                if (input.charAt(i+j)=='}') { //если charAt(i+j) не скобка, то мы в конце строки, и скобка осталась незакрытой
                    String templateName = input.substring(i+2, i+j); //содержимое скобок {}
                    String templateResult = getTemplate(templateName);
                    if (templateResult!=null) {
                        resuslt.append(templateResult);
                        i+=j;
                        applyedTemplate = true;
                    }
                }
                if (!applyedTemplate)
                {
                    resuslt.append(input.charAt(i));
                }
            } else {
                resuslt.append(input.charAt(i));
            }
        }
        return resuslt.toString();
    }

    public String htmlInfo() {
        return ""; //TODO что-то надо рассказать об этом классе
    }

    private String getTemplate(String tempalateName) {
        if (!templates.containsKey(tempalateName)) return null;
        return templates.get(tempalateName).getValue(config);
    }
}
