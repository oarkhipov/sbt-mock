package ru.sbt.bpm.mock.logging.entities;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author sbt-bochev-as on 24.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@ToString
@NoArgsConstructor
@Entity
@Table(name = "LOGS", schema = "PUBLIC", catalog = "MOCK")
public class LogsEntity {
    private Timestamp ts;
    private String protocol;
    private String systemName;
    private String integrationPointName;
    private String fullEndpoint;
    private String shortEndpoint;
    private String messageState;
    private String messagePreview;
    private String message;

    public LogsEntity(String protocol, String systemName, String integrationPointName, String fullEndpoint, String shortEndpoint, String messageState, String messagePreview, String message) {
        this.ts = new Timestamp(new Date().getTime());
        this.protocol = protocol;
        this.systemName = systemName;
        this.integrationPointName = integrationPointName;
        this.fullEndpoint = fullEndpoint;
        this.shortEndpoint = shortEndpoint;
        this.messageState = messageState;
        this.messagePreview = messagePreview;
        this.message = message;
    }

    @Id
    @Column(name = "TS")
    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    @Basic
    @Column(name = "PROTOCOL")
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Basic
    @Column(name = "SYSTEMNAME")
    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemname) {
        this.systemName = systemname;
    }

    @Basic
    @Column(name = "INTEGRATIONPOINTNAME")
    public String getIntegrationPointName() {
        return integrationPointName;
    }

    public void setIntegrationPointName(String integrationpointname) {
        this.integrationPointName = integrationpointname;
    }

    @Basic
    @Column(name = "FULLENDPOINT")
    public String getFullEndpoint() {
        return fullEndpoint;
    }

    public void setFullEndpoint(String fullendpoint) {
        this.fullEndpoint = fullendpoint;
    }

    @Basic
    @Column(name = "SHORTENDPOINT")
    public String getShortEndpoint() {
        return shortEndpoint;
    }

    public void setShortEndpoint(String shortendpoint) {
        this.shortEndpoint = shortendpoint;
    }

    @Basic
    @Column(name = "MESSAGESTATE")
    public String getMessageState() {
        return messageState;
    }

    public void setMessageState(String messagestate) {
        this.messageState = messagestate;
    }

    @Basic
    @Column(name = "MESSAGEPREVIEW")
    public String getMessagePreview() {
        return messagePreview;
    }

    public void setMessagePreview(String messagepreview) {
        this.messagePreview = messagepreview;
    }

    @Basic
    @Column(name = "MESSAGE")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
