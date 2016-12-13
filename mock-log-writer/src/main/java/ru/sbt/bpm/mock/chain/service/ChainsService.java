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

package ru.sbt.bpm.mock.chain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbt.bpm.mock.chain.entities.ChainsEntity;
import ru.sbt.bpm.mock.chain.entities.ChainsEntityPK;
import ru.sbt.bpm.mock.chain.repository.ChainsRepository;

import javax.persistence.TemporalType;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author sbt-bochev-as on 10.11.2016.
 *         <p>
 *         Company: SBT - Moscow
 */

@Service
public class ChainsService {
    @Autowired
    ChainsRepository repository;
    Map<Long,ChainsEntity> cache = new HashMap<Long,ChainsEntity>();

    public List<ChainsEntity> getChainsToExecute() {
        return getChainsToExecute(new Timestamp(System.currentTimeMillis()));
    }

    public List<ChainsEntity> getChainsInQueue() {
        return repository.findAll();
    }

    public List<ChainsEntity> getChainsToExecute(Date date) {
        return repository.findByTriggerTimeBefore(date);
    }

    public void checkExecutedChain(ChainsEntity chainsEntity) {
        repository.delete(chainsEntity);
    }

    public void put(ChainsEntity e){
        cache.put(e.getId(),e);
    }

    public ChainsEntity get(Long id){
        return cache.get(id);
    }

    public void remove(Long id){
        cache.remove(id);
    }



    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ChainsEntity addMockChain(Long delay, String calledSystemName, String calledIntegrationPointName, UUID messageTemplateId, String payload) {
        Timestamp triggerTime = new Timestamp(System.currentTimeMillis() + delay);
        ChainsEntity chainsEntity = new ChainsEntity(
                triggerTime,
                calledSystemName,
                calledIntegrationPointName,
                messageTemplateId == null ? "" : messageTemplateId.toString(),
                payload
        );
        chainsEntity = repository.saveAndFlush(chainsEntity);
        return chainsEntity;
    }

    public ChainsEntity getOne(ChainsEntityPK pk){
        return repository.getOne(pk);
    }

}
