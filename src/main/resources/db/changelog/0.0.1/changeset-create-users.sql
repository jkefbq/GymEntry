--liquibase formatted sql
--changeset author:slava dbms:postgresql
CREATE TABLE IF NOT EXISTS users (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR,
    email VARCHAR,
    password VARCHAR,
    authority VARCHAR
);
--rollback DROP TABLE users;