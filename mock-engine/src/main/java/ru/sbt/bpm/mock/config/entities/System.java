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
import lombok.NoArgsConstructor;
import reactor.tuple.Tuple;
import reactor.tuple.Tuple2;
import ru.sbt.bpm.mock.config.enums.Protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */

@XStreamAlias("system")
@Data
@NoArgsConstructor
public class System {

    @XStreamAlias("enabled")
    private Boolean enabled;

    @XStreamAlias("name")
    @XStreamAsAttribute
    private String systemName;

    @XStreamAlias("remoteRootSchema")
    private String remoteRootSchema;

    @XStreamAlias("localRootSchema")
    private String localRootSchema;

    @XStreamAlias("integrationPointSelector")
    private XpathSelector integrationPointSelector;

    @XStreamAlias("protocol")
    private Protocol protocol;

    @XStreamAlias("queueConnectionFactory")
    private String queueConnectionFactory;

    @XStreamAlias("mockIncomeQueue")
    private String mockIncomeQueue;

    @XStreamAlias("mockOutcomeQueue")
    private String mockOutcomeQueue;

    @XStreamAlias("driverOutcomeQueue")
    private String driverOutcomeQueue;

    @XStreamAlias("driverIncomeQueue")
    private String driverIncomeQueue;

    @XStreamAlias("driverWebServiceEndpoint")
    private String driverWebServiceEndpoint;

    @XStreamAlias("rootElement")
    private ElementSelector rootElement;

    @XStreamAlias("integrationPoints")
    private IntegrationPoints integrationPoints;

    @XStreamAlias("validationEnabled")
    private Boolean validationEnabled;

    public System(String systemName, String remoteRootSchema, String localRootSchema, XpathSelector integrationPointSelector, Protocol protocol, String queueConnectionFactory, String mockIncomeQueue, String mockOutcomeQueue, String driverOutcomeQueue, String driverIncomeQueue, String driverWebServiceEndpoint, ElementSelector rootElement, IntegrationPoints integrationPoints) {
        this.enabled = true;
        this.systemName = systemName;
        this.remoteRootSchema = remoteRootSchema;
        this.localRootSchema = localRootSchema;
        this.integrationPointSelector = integrationPointSelector;
        this.protocol = protocol;
        this.queueConnectionFactory = queueConnectionFactory;
        this.mockIncomeQueue = mockIncomeQueue;
        this.mockOutcomeQueue = mockOutcomeQueue;
        this.driverOutcomeQueue = driverOutcomeQueue;
        this.driverIncomeQueue = driverIncomeQueue;
        this.driverWebServiceEndpoint = driverWebServiceEndpoint;
        this.rootElement = rootElement;
        this.integrationPoints = integrationPoints;
        this.validationEnabled = true;
    }

//for soap
//    @XStreamOmitField
//    private WsdlProject wsdlProject;

    public List<String> getIntegrationPointNames() {
        List<String> result = new ArrayList<String>();
        if (integrationPoints != null && integrationPoints.getIntegrationPoints() != null) {
            for (IntegrationPoint point : getIntegrationPoints().getIntegrationPoints()) {
                result.add(point.getName());
            }
        }
        return result;
    }


    public List<IntegrationPoint> getMockIntegrationPoints() {
        List<IntegrationPoint> mockIntegrationPoints = new ArrayList<IntegrationPoint>();
        if (integrationPoints != null && integrationPoints.getIntegrationPoints() != null) {
            for (IntegrationPoint integrationPoint : integrationPoints.getIntegrationPoints()) {
                if (integrationPoint.isMock()) {
                    mockIntegrationPoints.add(integrationPoint);
                }
            }
        }
        return mockIntegrationPoints;
    }

    public List<IntegrationPoint> getDriverIntegrationPoints() {
        List<IntegrationPoint> driverIntegrationPoints = new ArrayList<IntegrationPoint>();
        if (integrationPoints != null && integrationPoints.getIntegrationPoints() != null) {
            for (IntegrationPoint integrationPoint : integrationPoints.getIntegrationPoints()) {
                if (integrationPoint.isDriver()) {
                    driverIntegrationPoints.add(integrationPoint);
                }
            }
        }
        return driverIntegrationPoints;
    }

    public Tuple2<String, String> getMockQueues() {
        return Tuple.of(mockIncomeQueue, mockOutcomeQueue);
    }

    public Tuple2<String, String> getDriverQueues () {
        return Tuple.of(driverIncomeQueue, driverOutcomeQueue);
    }

    public IntegrationPoints getIntegrationPoints() {
        if (integrationPoints == null) {
            integrationPoints = new IntegrationPoints();
        }
        return integrationPoints;
    }

    public Boolean getEnabled() {
        if (enabled == null) {
            enabled = true;
        }
        return enabled;
    }

    public Boolean getValidationEnabled() {
        if (validationEnabled == null) {
            validationEnabled = true;
        }
        return validationEnabled;
    }


}
