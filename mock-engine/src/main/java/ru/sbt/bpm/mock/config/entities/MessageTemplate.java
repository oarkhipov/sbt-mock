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

package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import ru.sbt.bpm.mock.config.enums.DispatcherTypes;
import ru.sbt.bpm.mock.config.serialization.CdataValue;

import java.util.UUID;

/**
 * @author sbt-bochev-as on 28.07.2016.
 *         <p>
 *         Company: SBT - Moscow
 */

@Data
@XStreamAlias("messageTemplate")
public class MessageTemplate {

    @XStreamAlias("id")
    private UUID templateId;

    @XStreamAlias("caption")
    private String caption;

    @XStreamAlias("dispatcherType")
    private DispatcherTypes dispatcherType;

    @CdataValue
    @XStreamAlias("dispatcherExpression")
    private String dispatcherExpression;

    @XStreamAlias("regexGroups")
    private String regexGroups;

    @XStreamAlias("value")
    private String value;

    @XStreamAlias("mockChains")
    private MockChains mockChains;

    public MessageTemplate() {
        templateId = UUID.randomUUID();
    }

    public MessageTemplate(String caption, DispatcherTypes dispatcherType, String dispatcherExpression, String regexGroups, String value) {
        templateId = UUID.randomUUID();
        this.caption = caption;
        this.dispatcherType = dispatcherType;
        this.dispatcherExpression = dispatcherExpression;
        this.regexGroups = regexGroups;
        this.value = value;
    }

    public MockChains getMockChains() {
        if (mockChains == null) {
            mockChains = new MockChains();
        }
        return mockChains;
    }
}
