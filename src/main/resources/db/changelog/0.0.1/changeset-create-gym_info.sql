--liquibase formatted sql
--changeset author:slava dbms:postgresql
CREATE TABLE IF NOT EXISTS gym_info (
    id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    adress VARCHAR,
);
--rollback DROP TABLE gym_info;