--liquibase formatted sql
--changeset author:slava dbms:postgresql
CREATE TABLE IF NOT EXISTS visits (
    id              uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at      TIMESTAMP,
    gym_info_id     UUID,
    subscription_id UUID,
    FOREIGN KEY (gym_info_id) REFERENCES gym_info(id),
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
);
--rollback DROP TABLE visits;