-- USER
INSERT INTO users (name) VALUES ('mimi');
INSERT INTO users (name) VALUES ('cookie');

-- TIME
INSERT INTO times (start_at) VALUES ('10:00');
INSERT INTO times (start_at) VALUES ('10:30');
INSERT INTO times (start_at) VALUES ('11:00');
INSERT INTO times (start_at) VALUES ('11:30');
INSERT INTO times (start_at) VALUES ('12:00');
INSERT INTO times (start_at) VALUES ('12:30');
INSERT INTO times (start_at) VALUES ('13:00');
INSERT INTO times (start_at) VALUES ('13:30');

-- RESTAURANT
INSERT INTO restaurants (name) VALUES ('도넛맛집');
INSERT INTO restaurants (name) VALUES ('국밥맛집');
INSERT INTO restaurants (name) VALUES ('삼겹살맛집');
INSERT INTO restaurants (name) VALUES ('곱창맛집');

-- RESTAURANT_TIME
INSERT INTO restaurant_schedules (is_available, restaurant_id, time_id, date) VALUES ('true', 1, 1, '2025-06-10');
INSERT INTO restaurant_schedules (is_available, restaurant_id, time_id, date) VALUES ('true', 1, 2, '2025-06-10');

INSERT INTO restaurant_schedules (is_available, restaurant_id, time_id, date) VALUES ('true', 2, 1, '2025-06-12');
INSERT INTO restaurant_schedules (is_available, restaurant_id, time_id, date) VALUES ('true', 2, 2, '2025-06-12');
INSERT INTO restaurant_schedules (is_available, restaurant_id, time_id, date) VALUES ('true', 2, 3, '2025-06-12');
INSERT INTO restaurant_schedules (is_available, restaurant_id, time_id, date) VALUES ('true', 2, 4, '2025-06-12');
INSERT INTO restaurant_schedules (is_available, restaurant_id, time_id, date) VALUES ('false', 2, 5, '2025-06-12');

-- RESERVATION
INSERT into reservations (restaurant_schedule_id, user_id, created_at) VALUES (1, 1, '2025-06-09 17:00');
INSERT into reservations (restaurant_schedule_id, user_id, created_at) VALUES (2, 2, '2025-06-09 18:00');
