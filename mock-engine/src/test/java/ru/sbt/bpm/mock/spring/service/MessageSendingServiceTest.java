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

package ru.sbt.bpm.mock.spring.service;

import org.custommonkey.xmlunit.Diff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;
import ru.sbt.bpm.mock.utils.XmlUtils;

import static org.testng.Assert.assertEquals;

/**
 * @author sbt-bochev-as on 14.07.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ContextConfiguration(value = {"classpath:/env/mockapp-servlet-soap-http.xml", "classpath:/contextConfigs/logging-config.xml"})
public class MessageSendingServiceTest extends AbstractSOAPSpyneVirtualHttpServerTransactionalTestNGSpringContextTests {

    @Autowired
    MessageSendingService messageSendingService;

    @Autowired
    MockConfigContainer configContainer;

    @Test
    public void testSendWs() throws Exception {
        MockMessage message = new MockMessage(SIMPLE_REQUEST);
        System system = configContainer.getSystemByName("Spyne");
        system.setDriverWebServiceEndpoint("http://localhost:8085/path/to/dir/spyneResponse.xml");
        message.setSystem(system);
        message.setIntegrationPoint(system.getIntegrationPoints().getIntegrationPointByName("say_hello"));
        String response = messageSendingService.sendWs(message);
        Diff diff = new Diff(XmlUtils.compactXml(SIMPLE_RESPONSE), XmlUtils.compactXml(response));
        if (!diff.identical()) {
            assertEquals(response, SIMPLE_RESPONSE);
        }

    }
}