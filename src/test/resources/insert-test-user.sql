INSERT INTO users (id, first_name, email, password, role, total_visits, member_since, last_visit)
VALUES ('0962ce5c-1605-44e8-84d8-061bc01d3a49', 'user', 'user@gmail.com',
        '$2a$12$c3I3f0IukONHhp8KW6iSIO/v3VYFiehjLfkUnYXG11eWMHC3dyPc.','USER', 10, '2026-03-01', '2026-03-02');

INSERT INTO subscriptions (visits_total, snapshot_price, purchase_at, is_active, visits_left, tariff_type, user_id)
VALUES (12, 3000, '2026-03-01', true, 10, 'BASIC', '0962ce5c-1605-44e8-84d8-061bc01d3a49'),
(12, 3000, '2026-03-01', false, 10, 'BASIC', '0962ce5c-1605-44e8-84d8-061bc01d3a49'),
(10, 2500, '2026-03-01', false, 10, 'PREMIUM', '0962ce5c-1605-44e8-84d8-061bc01d3a49'),
(8, 3000, '2026-03-01', false, 8, 'MIDDLE', '0962ce5c-1605-44e8-84d8-061bc01d3a49');