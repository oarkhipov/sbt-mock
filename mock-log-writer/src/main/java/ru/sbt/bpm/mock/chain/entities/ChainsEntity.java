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

package ru.sbt.bpm.mock.chain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author sbt-bochev-as on 10.11.2016.
 *         <p>
 *         Company: SBT - Moscow
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "CHAINS", schema = "PUBLIC", catalog = "MOCK")
@IdClass(ChainsEntityPK.class)
public class ChainsEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @SequenceGenerator(name = "SEQ_GENERATOR", sequenceName = "SEQ_GENERATOR", allocationSize = 1)
    private long id;

    @Id
    @Column(name = "TS")
    private Timestamp ts;

    @Basic
    @Column(name = "TRIGGERTIME")
    @Temporal(TemporalType.TIME)
    private Date triggerTime;

    @Basic
    @Column(name = "SYSTEM")
    private String system;

    @Basic
    @Column(name = "INTEGRATIONPOINT")
    private String integrationPoint;

    @Basic
    @Column(name = "MESSAGETEMPLATEID")
    private String messageTemplateId;

    @Basic
    @Column(name = "MESSAGE")
    private String message;

    public ChainsEntity(Date triggerTime, String system, String integrationPoint, String messageTemplateId, String message) {
        this.ts = new Timestamp(System.currentTimeMillis());
        this.triggerTime = triggerTime;
        this.system = system;
        this.integrationPoint = integrationPoint;
        this.messageTemplateId = messageTemplateId;
        this.message = message;
    }
}
