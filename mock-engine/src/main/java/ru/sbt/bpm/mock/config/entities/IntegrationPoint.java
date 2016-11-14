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
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;


/**
 * @author sbt-hodakovskiy-da
 * @author sbt-bochev-as
 *         on 30.01.2015
 *         <p/>
 *         Company: SBT - Saint-Petersburg
 */
@XStreamAlias("integrationPoint")
@Data
public class IntegrationPoint {

    // Тип точки интеграции Driver
    public static final String DRIVER = "Driver";
    // Тип точки интеграции Mock
    public static final String MOCK = "Mock";

    @XStreamAlias("enabled")
    private Boolean enabled;

    @XStreamAlias("name")
    @XStreamAsAttribute
    private String name;

    @XStreamAlias("type")
    private String integrationPointType;

    @XStreamAlias("delayMs")
    private Integer delayMs;

    @XStreamAlias("xpathValidation")
    private XpathSelector xpathValidatorSelector;

    //  For override
    @XStreamAlias("incomeQueue")
    private String incomeQueue;

    //  For override
    @XStreamAlias("outcomeQueue")
    private String outcomeQueue;

    @XStreamAlias("answerRequired")
    private Boolean answerRequired;

    // Так как маппинг идет по полям xml, для удобства доступа и сравнения создаем Tuple2<INCOME, OUTCOME>
    private transient Tuple2<String, String> pairOfChannels;

    @XStreamAlias("xsdFile")
    private String xsdFile;

    @XStreamAlias("rootElement")
    private ElementSelector rootElement;

    @XStreamAlias("sequenceEnabled")
    private Boolean sequenceEnabled;

    @XStreamAlias("validationEnabled")
    private Boolean validationEnabled;

    transient private int responseSequenceNum;

    @XStreamAlias("messageTemplates")
    private MessageTemplates messageTemplates;

    @XStreamAlias("mockChains")
    private MockChains mockChains;

    public IntegrationPoint(
            String name,
            String integrationPointType,
            Integer delayMs,
            XpathSelector xpathValidatorSelector,
            String incomeQueue, String outcomeQueue,
            Boolean answerRequired,
            Tuple2<String, String> pairOfChannels,
            String xsdFile,
            ElementSelector rootElement,
            Boolean sequenceEnabled) {
        this.enabled = true;
        this.name = name;
        this.integrationPointType = integrationPointType;
        this.delayMs = delayMs;
        this.xpathValidatorSelector = xpathValidatorSelector;
        this.incomeQueue = incomeQueue;
        this.outcomeQueue = outcomeQueue;
        this.answerRequired = answerRequired;
        this.pairOfChannels = pairOfChannels;
        this.xsdFile = xsdFile;
        this.rootElement = rootElement;
        this.sequenceEnabled = sequenceEnabled;
        this.validationEnabled = true;
    }

    public Tuple2<String, String> getPairOfChannels() {
        return Tuple.of(this.getIncomeQueue(), this.getOutcomeQueue());
    }

    public void setPairOfChannels(Tuple2<String, String> pairOfChannels) {
        this.setIncomeQueue(pairOfChannels.getT1());
        this.setOutcomeQueue(pairOfChannels.getT2());
    }

    public boolean isMock() {
        return integrationPointType.equals(MOCK);
    }

    public boolean isDriver() {
        return integrationPointType.equals(DRIVER);
    }

    public String getXpathString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (xpathValidatorSelector != null) {
            for (ElementSelector elementSelector : xpathValidatorSelector.getElementSelectors()) {
                stringBuilder.append(elementSelector.getElement()).append("/");
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }
        return stringBuilder.toString();
    }

    public String getXpathWithFullNamespaceString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ElementSelector elementSelector : xpathValidatorSelector.getElementSelectors()) {
            stringBuilder
                    .append("/*:")
                    .append(elementSelector.getElement())
                    .append("[namespace-uri()='")
                    .append(elementSelector.getNamespace())
                    .append("']");
        }
//        stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
        return stringBuilder.toString();
    }

    public MessageTemplates getMessageTemplates() {
        if (messageTemplates == null) {
            messageTemplates = new MessageTemplates();
        }
        return messageTemplates;
    }

    public Boolean getEnabled() {
        if (enabled == null) {
            enabled = true;
        }
        return enabled;
    }

    public Boolean getSequenceEnabled() {
        if (sequenceEnabled == null) {
            sequenceEnabled = false;
        }
        return sequenceEnabled;
    }

    public Boolean getValidationEnabled() {
        if (validationEnabled == null) {
            validationEnabled = true;
        }
        return validationEnabled;
    }

    public Boolean getAnswerRequired() {
        if (answerRequired == null) {
            answerRequired = true;
        }
        return answerRequired;
    }

    public MockChains getMockChains() {
        if (mockChains == null) {
            mockChains = new MockChains();
        }
        return mockChains;
    }
}
