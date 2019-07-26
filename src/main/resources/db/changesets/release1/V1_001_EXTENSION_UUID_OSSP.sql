--liquibase formatted sql logicalFilePath:V1_001_EXTENSION_UUID_OSSP.sql
--changeset sanasov:1.1 runOnChange:false context:main
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
