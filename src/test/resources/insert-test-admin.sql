INSERT INTO gym_info (address)
VALUES ('test-address');

INSERT INTO users (first_name, email, password, role, total_visits, member_since, last_visit, gym_address)
VALUES ('admin', 'admin@gmail.com', '$2a$12$nbbaJC91yFRVK1cpW23lpedUUllAP6LLvWghZZT/9HYrDeXSaycoK',
    'ADMIN', 10, '2026-03-01', '2026-03-02', 'test-address');