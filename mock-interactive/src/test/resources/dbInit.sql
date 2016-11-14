CREATE TABLE IF NOT EXISTS LOGS
(
  TS TIMESTAMP PRIMARY KEY NOT NULL,
  TID VARCHAR(50) NOT NULL,
  PROTOCOL VARCHAR(15) NOT NULL,
  SYSTEMNAME VARCHAR(50) NOT NULL,
  INTEGRATIONPOINTNAME VARCHAR(50) NOT NULL,
  FULLENDPOINT VARCHAR(250) NOT NULL,
  SHORTENDPOINT VARCHAR(50) NOT NULL,
  MESSAGESTATE VARCHAR(20) NOT NULL,
  MESSAGEPREVIEW VARCHAR(50) NOT NULL,
  MESSAGE CLOB NOT NULL
);
CREATE INDEX IF NOT EXISTS LOGS_MESS_PREVIEW_INDEX ON LOGS (MESSAGEPREVIEW);
CREATE INDEX IF NOT EXISTS LOGS_TS_INDEX ON LOGS (TS);
-- migrate 3.x.x to 3.6.2
ALTER TABLE LOGS ADD TID VARCHAR(50) AFTER TS;

-- add mock chain mechanism
CREATE TABLE IF NOT EXISTS PUBLIC.chains
(
  id INT AUTO_INCREMENT NOT NULL,
  ts TIMESTAMP DEFAULT NOW(),
  triggerTime DATE NOT NULL,
  system VARCHAR(100) NOT NULL,
  integrationPoint VARCHAR(100) NOT NULL,
  messageTemplateId UUID,
  message CLOB NOT NULL,
  CONSTRAINT chains_id_ts_pk PRIMARY KEY (id, ts)
);
CREATE UNIQUE INDEX IF NOT EXISTS "chains_id_uindex" ON PUBLIC.chains (id);
CREATE INDEX IF NOT EXISTS "chains_triggerTime_system_integrationPoint_messageTemplateId_index" ON PUBLIC.chains (triggerTime DESC, system, integrationPoint, messageTemplateId);

CREATE SEQUENCE IF NOT EXISTS "SEQ_GENERATOR"
  START WITH 1
  INCREMENT BY 1
  CACHE 1;
