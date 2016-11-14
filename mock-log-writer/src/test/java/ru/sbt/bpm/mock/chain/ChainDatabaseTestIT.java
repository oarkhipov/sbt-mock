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

package ru.sbt.bpm.mock.chain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.sbt.bpm.mock.chain.entities.ChainsEntity;
import ru.sbt.bpm.mock.chain.repository.ChainsRepository;
import ru.sbt.bpm.mock.chain.service.ChainsService;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author sbt-bochev-as on 24.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ContextConfiguration({"classpath:test-spring-config.xml"})
public class ChainDatabaseTestIT extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    ChainsService chainsService;

    @Autowired
    ChainsRepository repository;

    public void fillData() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            long dateLong = Date.UTC(2016, i + 1, 1, 0, 0, 0);
            repository.save(new ChainsEntity(
                    new Date(dateLong),
                    "system" + i,
                    "integrationPoint1",
                    UUID.randomUUID().toString(),
                    "some message"
            ));
        }
        Thread.sleep(10);
    }

    @Test
    public void FindAllTest() throws Exception {
        fillData();
        List<ChainsEntity> entities = repository.findAll();
        for (ChainsEntity chainsEntity : entities) {
            System.out.println(chainsEntity);
        }
        assertEquals(entities.size(), 10);
    }

    @Test
    public void FindByDateTest() throws Exception {
        fillData();
        List<ChainsEntity> shortEndpointName = chainsService.getChainsToExecute(new Date(2016, 5, 2));
        assertTrue(shortEndpointName.size() == 5);
    }

    @Test
    public void checkExecutionTest() throws Exception {
        fillData();
        Page<ChainsEntity> page = repository.findAll(new PageRequest(0, 1));
        ChainsEntity entity = page.getContent().get(0);
        chainsService.checkExecutedChain(entity);
        assertTrue(repository.findAll().size() == 9);
    }
}
