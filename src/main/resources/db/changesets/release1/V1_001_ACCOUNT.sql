--liquibase formatted sql logicalFilePath:V1_001_ACCOUNT.sql
--changeset sanasov:1.2 runOnChange:false context:main

CREATE TABLE ACCOUNT (
  id                UUID                        NOT NULL default random_uuid()  CONSTRAINT account_key PRIMARY KEY
  client_id         UUID
  account_number    varchar (40) NOT NULL,
  sum_rub           varchar (40) NOT NULL,
  create_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  modify_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
--   created_by        VARCHAR(100)                NOT NULL,
--   created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
--   updated_by        VARCHAR(100)                NOT NULL,
--   updated_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
--   deleted_by        VARCHAR(100),
--   deleted_at        TIMESTAMP WITHOUT TIME ZONE
);