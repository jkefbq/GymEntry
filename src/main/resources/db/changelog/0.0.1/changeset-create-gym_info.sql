--liquibase formatted sql
--changeset author:slava dbms:postgresql
CREATE TABLE IF NOT EXISTS gym_info (
    address VARCHAR PRIMARY KEY DEFAULT gen_random_uuid()
);
--rollback DROP TABLE gym_info;