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
        this.transactionId = transactionId.toString();
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
