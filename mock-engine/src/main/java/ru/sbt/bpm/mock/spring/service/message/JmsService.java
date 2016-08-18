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

package ru.sbt.bpm.mock.spring.service.message;

import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.utils.XmlUtils;

import java.util.NoSuchElementException;

/**
 * @author sbt-bochev-as on 17.03.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
@Service
public class JmsService {
    @Autowired
    private MockConfigContainer configContainer;

    public System getSystemByPayload(String payload) {
        return getSystemByPayload(payload, "");
    }

    public System getSystemByPayload(String payload, String queue) {
        for (System system : configContainer.getConfig().getSystems().getSystems()) {
            //check queue name if parameter is not empty
            if (queue.isEmpty() || queue.equals(system.getMockIncomeQueue())) {
                String xpath = system.getIntegrationPointSelector().toXpath();
                try {
                    XdmNode xdmItems = (XdmNode) XmlUtils.evaluateXpath(payload, xpath);
                    if (system.getIntegrationPointNames().contains(xdmItems.getNodeName().getLocalName())) {
                        return system;
                    }
                } catch (SaxonApiException e) {
//                e.printStackTrace();
                    //this is not system, that we are looking for
                    log.debug(e.getMessage(), e);
                } catch (ClassCastException e) {
                    //this is not system, that we are looking for
                    log.debug(e.getMessage(), e);
                }
            }
        }
        throw new NoSuchElementException("No JMS system found by payload:\n" + payload);
    }
}
