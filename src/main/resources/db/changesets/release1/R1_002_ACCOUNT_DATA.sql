--liquibase formatted sql logicalFilePath:R1_002_ACCOUNT_DATA.sql
--changeset sanasov:1.2 runOnChange:false context:main

INSERT INTO ACCOUNT(account_number,  sum_rub) VALUES ('11112222333344445555', 200.00);
INSERT INTO ACCOUNT(account_number,  sum_rub) VALUES ('11112222333344445556', 1000.00);