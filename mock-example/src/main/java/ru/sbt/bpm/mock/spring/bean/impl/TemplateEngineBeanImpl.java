package ru.sbt.bpm.mock.spring.bean.impl;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.spring.bean.TemplateEngineBean;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sbt-bochev-as on 03.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@Service
public class TemplateEngineBeanImpl implements TemplateEngineBean {

    private Logger log = LoggerFactory.getLogger(TemplateEngineBeanImpl.class);

    public String applyTemplate(String input) throws IOException {
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());

        Map<String, Object> model = new HashMap<String, Object>();


        model.put("timestamp10",new SimpleDateFormat("yyMMddHHmm").format(new Date()));
        model.put("timestamp12",new SimpleDateFormat("yyMMddHHmmss").format(new Date()));
        model.put("timestamp15",new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()));
        model.put("random100", new Random().nextInt(100));
        model.put("random1000", new Random().nextInt(1000));

        Writer  out = new StringWriter();
        try {
            Template template = new Template("inputTemplate", new StringReader(input), configuration);

            template.process(model, out);
        } catch (TemplateException e) {
            log.error("Template Error", e);
        }

        return out.toString();
    }

    public String htmlInfo() {
        return "<b>Переменные шаблонизатора:</b><br/>" +
                "<b>${timestamp10}</b> - печатает timestamp в формате yyMMddHHmm<br/>" +
                "<b>${timestamp12}</b> - печатает timestamp в формате yyMMddHHmmss<br/>" +
                "<b>${timestamp15}</b> - печатает timestamp в формате yyMMddHHmmssSSS (with milliseconds)<br/>" +
                "<b>${random100}</b> - печатает случайное число от 0 до 100<br/>" +
                "<b>${random1000}</b> - печатает случайное число от 0 до 1000";
    }

}
