--liquibase formatted sql logicalFilePath:V1_002_ACCOUNT.sql
--changeset sanasov:1.2 runOnChange:false context:main

CREATE TABLE ACCOUNT (
  id                UUID                        NOT NULL
    CONSTRAINT agent_pkey PRIMARY KEY
                                                         DEFAULT uuid_generate_v4(),
  code              VARCHAR(6)                  NOT NULL,
  short_name        VARCHAR(255)                NOT NULL,
  full_name         VARCHAR(255)                NOT NULL,
  legal_address     VARCHAR(255)                NOT NULL,
  actual_address    VARCHAR(255)                NOT NULL,
  license_number    VARCHAR(255)                NOT NULL,
  license_date      DATE                        NOT NULL,
  license_validity  DATE,
  license_issued_by VARCHAR(255)                NOT NULL,
  created_by        VARCHAR(100)                NOT NULL,
  created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  updated_by        VARCHAR(100)                NOT NULL,
  updated_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  deleted_by        VARCHAR(100),
  deleted_at        TIMESTAMP WITHOUT TIME ZONE
);