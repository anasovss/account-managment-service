--liquibase formatted sql logicalFilePath:R1_001_ACCOUNT.sql
--changeset sanasov:1.1 runOnChange:false context:main

CREATE TABLE ACCOUNT (
  id                UUID  NOT NULL default RANDOM_UUID()  CONSTRAINT account_pk PRIMARY KEY,
  client_id         UUID,
  account_number    varchar (20) NOT NULL,
  sum_rub           varchar (40) NOT NULL,
  create_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  modify_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP()
--   created_by        VARCHAR(100)                NOT NULL,
--   created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
--   updated_by        VARCHAR(100)                NOT NULL,
--   updated_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
--   deleted_by        VARCHAR(100),
--   deleted_at        TIMESTAMP WITHOUT TIME ZONE
);

CREATE UNIQUE INDEX account_number_index ON ACCOUNT(account_number);