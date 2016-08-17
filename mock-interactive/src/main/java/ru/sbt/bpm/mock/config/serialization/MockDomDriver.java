/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
        add(MockConfig.class);
        add(MainConfig.class);
        add(Systems.class);
        add(System.class);
        add(ElementSelector.class);
        add(XpathSelector.class);
        add(IntegrationPoints.class);
        add(IntegrationPoint.class);
        add(MessageTemplates.class);
        add(MessageTemplate.class);
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
