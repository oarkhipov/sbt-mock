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

package ru.sbt.bpm.mock.spring.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.chain.entities.ChainsEntity;
import ru.sbt.bpm.mock.chain.service.ChainsService;
import ru.sbt.bpm.mock.config.MockConfig;
import ru.sbt.bpm.mock.config.MockConfigContainer;
import ru.sbt.bpm.mock.config.entities.IntegrationPoint;
import ru.sbt.bpm.mock.config.entities.System;
import ru.sbt.bpm.mock.config.enums.Protocol;
import ru.sbt.bpm.mock.spring.bean.pojo.MockMessage;
import ru.sbt.bpm.mock.spring.service.MessageSendingService;

import java.io.IOException;
import java.util.List;

/**
 * @author sbt-bochev-as on 10.11.2016.
 *         <p>
 *         Company: SBT - Moscow
 */

@Slf4j
@Service
public class MockChainActionExecutor {

    @Autowired
    ChainsService chainsService;

    @Autowired
    MessageSendingService messageSendingService;

    @Autowired
    MockConfigContainer configContainer;

    @Scheduled(fixedDelay = 1000)
    public void findProperMockChainAndFireIt() {
        log.warn("Chains in Queue: " + chainsService.getChainsInQueue());
        List<ChainsEntity> chainsToExecute = chainsService.getChainsToExecute();
        for (ChainsEntity chainsEntity : chainsToExecute) {
            log.warn("Execute chain: " + chainsEntity);
            String systemName = chainsEntity.getSystem();
            System system = configContainer.getSystemByName(systemName);
            IntegrationPoint integrationPoint = system.getIntegrationPoints().getIntegrationPointByName(chainsEntity.getIntegrationPoint());

            Protocol protocol = system.getProtocol();
            String queueConnectionFactory = system.getQueueConnectionFactory();
            String driverOutcomeQueue = system.getDriverOutcomeQueue();
            String messagePayload = chainsEntity.getMessage();

            MockMessage mockMessage = new MockMessage(protocol, queueConnectionFactory, driverOutcomeQueue, messagePayload);
            mockMessage.setSystem(system);
            mockMessage.setIntegrationPoint(integrationPoint);

            try {
                messageSendingService.send(mockMessage);
                chainsService.checkExecutedChain(chainsEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
