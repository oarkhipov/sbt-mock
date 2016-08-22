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

package ru.sbt.bpm.mock.config;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.thoughtworks.xstream.XStream;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple3;
import ru.sbt.bpm.mock.config.entities.*;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.serialization.MockDomDriver;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Created by sbt-bochev-as on 02.04.2015.
 * <p/>
 * Company: SBT - Moscow
 */
@NoArgsConstructor
@Component
@Slf4j
public class MockConfigContainer {

    public static final Class[] CONFIG_CLASSES = {
            MockConfig.class,
            MainConfig.class,
            Systems.class,
            System.class,
            ElementSelector.class,
            XpathSelector.class,
            IntegrationPoints.class,
            IntegrationPoint.class,
            MessageTemplates.class,
            MessageTemplate.class
    };

    private static MockConfigContainer INSTANCE = null;

    @Autowired
    private ApplicationContext applicationContext;

    @Getter
    private String basePath;

    @Getter
    private MockConfig config;

    //map of wsdl projects for message generation and validation. It initializes at validator initialization method
    @Getter
    private Map<String, WsdlProject> wsdlProjectMap = new HashMap<String, WsdlProject>();

    @Getter
    private String filePath = null;


    /**
     * Создание экземпляра объекта из метода getInstance (CoreJava) или из бина с вызовом конструктора (Java EE)
     *
     * @param filePath путь до конфига
     */
    public MockConfigContainer(String filePath) {
        this.filePath = filePath;
        config = new MockConfig();
    }

    /**
     * Получение экземпляра объекта синглтона средствами core Java
     *
     * @param filePath путь до файла конфига
     */
    public static MockConfigContainer getInstance(String filePath) {
        if (INSTANCE == null)
            synchronized (MockConfigContainer.class) {
                if (INSTANCE == null)
                    INSTANCE = new MockConfigContainer(filePath);
            }
        return INSTANCE;
    }

    /**
     * MockConfig.xml file deserialization into Config object on bean creation
     *
     * @throws IOException if no config file found
     */
    @PostConstruct
    public void init() throws IOException {
        if (this.filePath == null || this.filePath.isEmpty())
            throw new IOException("no config file [" + filePath + "]!");

        File resourceFile;
        if (applicationContext == null) {
            resourceFile = new File(filePath);
            basePath = new File("").getAbsolutePath();
        } else {
            basePath = applicationContext.getResource("").getFile().getAbsolutePath();
            resourceFile = applicationContext.getResource(filePath).getFile();
        }

        log.warn("Basepath: " + basePath);

        FileReader fileReader = new FileReader(resourceFile);
        XStream xStream = new XStream(new MockDomDriver());
        // Mapping данных из xml в классы
        xStream.processAnnotations(CONFIG_CLASSES);

        this.config = (MockConfig) xStream.fromXML(fileReader);

        //fix collectionTypes
        config.getSystems().fixTypes();
        if (config.getSystems().getSystems() != null) {
            for (System system : config.getSystems().getSystems()) {
                if (system.getIntegrationPoints() != null) {
                    system.getIntegrationPoints().fixTypes();
                }
            }
        }


        //Enable GLOBAL validation
        if (config.getMainConfig() == null) {
            config.setMainConfig(new MainConfig());
        }

        if (config.getMainConfig().getValidationEnabled() == null) {
            config.getMainConfig().setValidationEnabled(true);
        }
    }


    /**
     * Serialization Config Object to xml string
     *
     * @return xml string
     */
    public String toXml() {
        XStream xStream = new XStream(new MockDomDriver());
        // Mapping данных из xml в классы
        xStream.processAnnotations(CONFIG_CLASSES);
        return xStream.toXML(config);
    }

    /**
     * Returns System by it's name
     *
     * @param systemName name of system
     * @return @ru.sbt.bpm.mock.config.entities.System
     */
    public System getSystemByName(String systemName) {
        return config.getSystems().getSystemByName(systemName);
    }

    /**
     * Returns @integrationPoint by System name and integration point name
     *
     * @param systemName           name of system, which integration point belongs
     * @param integrationPointName name of integration point
     * @return @ru.sbt.bpm.mock.config.entities.IntegrationPoint
     */
    public IntegrationPoint getIntegrationPointByName(String systemName, String integrationPointName) {
        return getSystemByName(systemName).getIntegrationPoints().getIntegrationPointByName(integrationPointName);
    }

    /**
     * Reinit Config
     *
     * @throws IOException if no config file found
     */
    public void reInit() throws IOException {
        init();
    }

    public Tuple3<System, IntegrationPoint, MessageTemplate> getConfigEntitiesByMessageTemplateUUID(UUID uuid) {
        for (System system : config.getSystems().getSystems()) {
            for (IntegrationPoint integrationPoint : system.getIntegrationPoints().getIntegrationPoints()) {
                for (MessageTemplate messageTemplate : integrationPoint.getMessageTemplates().getMessageTemplateList()) {
                    if (messageTemplate.getTemplateId().equals(uuid)) {
                        return Tuple.of(system, integrationPoint, messageTemplate);
                    }
                }
            }
        }
        throw new NoSuchElementException("No such message template with UUID: " + uuid);
    }
}
