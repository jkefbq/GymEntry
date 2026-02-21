--liquibase formatted sql
--changeset author:slava dbms:postgresql
CREATE TABLE IF NOT EXISTS tariffs (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    tariff_name VARCHAR UNIQUE,
    description VARCHAR,
    price_per_lesson NUMERIC,
    tariff_type VARCHAR UNIQUE
);
--rollback DROP TABLE tariffs;