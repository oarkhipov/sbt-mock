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

package ru.sbt.bpm.mock.logging.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * @author sbt-bochev-as on 24.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "LOGS", schema = "PUBLIC", catalog = "MOCK")
public class LogsEntity {
    @Id
    @Column(name = "TS")
    private Timestamp ts;

    @Basic
    @Column(name = "TID")
    private String transactionId;

    @Basic
    @Column(name = "PROTOCOL")
    private String protocol;

    @Basic
    @Column(name = "SYSTEMNAME")
    private String systemName;

    @Basic
    @Column(name = "INTEGRATIONPOINTNAME")
    private String integrationPointName;

    @Basic
    @Column(name = "FULLENDPOINT")
    private String fullEndpoint;

    @Basic
    @Column(name = "SHORTENDPOINT")
    private String shortEndpoint;

    @Basic
    @Column(name = "MESSAGESTATE")
    private String messageState;

    @Basic
    @Column(name = "MESSAGEPREVIEW")
    private String messagePreview;

    @Basic
    @Column(name = "MESSAGE")
    private String message;

    public LogsEntity(UUID transactionId, String protocol, String systemName, String integrationPointName, String fullEndpoint, String shortEndpoint, String messageState, String messagePreview, String message) {
        this.ts = new Timestamp(new Date().getTime());
        this.transactionId = (transactionId != null ? transactionId.toString(): null);
        this.protocol = protocol;
        this.systemName = systemName;
        this.integrationPointName = integrationPointName;
        this.fullEndpoint = fullEndpoint;
        this.shortEndpoint = shortEndpoint;
        this.messageState = messageState;
        this.messagePreview = messagePreview;
        this.message = message;
    }
}
