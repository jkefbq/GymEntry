--liquibase formatted sql
--changeset author:slava dbms:postgresql
CREATE TABLE IF NOT EXISTS users (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR,
    email VARCHAR UNIQUE,
    password VARCHAR,
    role VARCHAR,
    total_visits INT,
    member_since DATE,
    last_visit DATE,
    gym_info_id UUID,
    FOREIGN KEY (gym_info_id) REFERENCES gym_info(id)
);
--rollback DROP TABLE users;