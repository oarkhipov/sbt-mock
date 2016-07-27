package ru.sbt.bpm.mock.config.serialization;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.config.entities.*;
import ru.sbt.bpm.mock.config.entities.System;

import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-bochev-as on 27.07.2016.
 *         <p>
 *         Company: SBT - Moscow
 */
public class MockDomDriver extends DomDriver {
    private static List<Class> configClasses = new ArrayList<Class>() {{
//        add(MockConfig.class);
//        add(Systems.class);
//        add(System.class);
//        add(ElementSelector.class);
//        add(XpathSelector.class);
//        add(IntegrationPoints.class);
        add(IntegrationPoint.class);
    }};
    @Override
    public HierarchicalStreamWriter createWriter(Writer out) {
        return new PrettyPrintWriter(out) {
            boolean cdataField = false;

            @Override
            public void startNode(String name) {
                for (Class configClass : configClasses) {
                    for (Field field : configClass.getDeclaredFields()) {
                        if (field.getName().equals(name) ||
                                (field.getAnnotation(XStreamImplicit.class)!= null && field.getAnnotation(XStreamImplicit.class).itemFieldName().equals(name)) ||
                                (field.getAnnotation(XStreamAlias.class)!= null && field.getAnnotation(XStreamAlias.class).value().equals(name))) {
                            if (field.getAnnotation(CdataValue.class) != null) {
                                cdataField = true;
                            }
                        }
                    }
                }
                super.startNode(name);
            }

            @Override
            protected void writeText(QuickWriter writer, String text) {
                if (cdataField) {
                    writer.write("<![CDATA[" + text + "]]>");
                    cdataField = false;
                } else {
                    writer.write(text);
                }

            }
        };
    }
}
