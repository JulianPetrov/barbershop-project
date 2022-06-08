INSERT INTO t_authority (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

INSERT INTO t_user (id, email, first_name, last_name, password_hash, username)
VALUES ((select nextval('sequence_generator')), 'julian@test.com', 'Julian', 'Petrov',
        'julian',
        'julian'),
       ((select nextval('sequence_generator')), 'test@test.com', 'test', 'test',
        '$2a$10$2O7fgfzE.UH9EZWk/1Zad.4xVkIoYCZ6XzY53rGDl.Be/KHWndN6K', 'test'),
       ((select nextval('sequence_generator')), 'noadmin@test.com', 'noadmin', 'noadmin',
        '$2a$10$Drjs8h.ZQqF3nsBu7VNuAey8gRu1n.7uuSmB3DqJnMBfPFq7N04Ni',
        'noadmin');

INSERT INTO t_user_authority(user_id, authority_name)
VALUES ((select id from t_user where email = 'julian@test.com'), 'ROLE_USER'),
       ((select id from t_user where email = 'test@test.com'), 'ROLE_USER'),
       ((select id from t_user where email = 'test@test.com'), 'ROLE_ADMIN');

INSERT INTO service(id, name, duration_minutes)
VALUES ((select nextval('sequence_generator')), 'Haircut', 30),
       ((select nextval('sequence_generator')), 'Haircut and beard', 45),
       ((select nextval('sequence_generator')), 'Hair dye', 30),
       ((select nextval('sequence_generator')), 'Eyebrows', 30);

INSERT INTO salon(id, name, address, city, start_time, end_time, user_id)
VALUES ((select nextval('sequence_generator')), 'Uncle Bobby', 'Bul. "Todor Alexandronv" 15', 'SOFIA', '10:00', '20:00',
        (select id from t_user where email = 'test@test.com')),
       ((select nextval('sequence_generator')), 'Gentleman', 'Ul. "Pirotska" 134', 'SOFIA', '11:00', '19:00',
        (select id from t_user where email = 'test@test.com'));

INSERT INTO salon_workday(id, week_day, salon_id)
VALUES ((select nextval('sequence_generator')), 'TUESDAY', (select id from salon where name = 'Uncle Bobby')),
       ((select nextval('sequence_generator')), 'WEDNESDAY', (select id from salon where name = 'Uncle Bobby')),
       ((select nextval('sequence_generator')), 'THURSDAY', (select id from salon where name = 'Uncle Bobby')),
       ((select nextval('sequence_generator')), 'FRIDAY', (select id from salon where name = 'Uncle Bobby')),
       ((select nextval('sequence_generator')), 'SATURDAY', (select id from salon where name = 'Uncle Bobby')),
       ((select nextval('sequence_generator')), 'SUNDAY', (select id from salon where name = 'Uncle Bobby')),
       ((select nextval('sequence_generator')), 'MONDAY', (select id from salon where name = 'Gentleman')),
       ((select nextval('sequence_generator')), 'TUESDAY', (select id from salon where name = 'Gentleman')),
       ((select nextval('sequence_generator')), 'WEDNESDAY', (select id from salon where name = 'Gentleman')),
       ((select nextval('sequence_generator')), 'THURSDAY', (select id from salon where name = 'Gentleman'));

INSERT INTO salon_service(id, salon_id, service_id, price)
VALUES ((select nextval('sequence_generator')), (select id from salon where name = 'Uncle Bobby'),
        (select id from service where name = 'Haircut and beard'), 40),
       ((select nextval('sequence_generator')), (select id from salon where name = 'Uncle Bobby'),
        (select id from service where name = 'Haircut'), 30),
       ((select nextval('sequence_generator')), (select id from salon where name = 'Uncle Bobby'),
        (select id from service where name = 'Eyebrows'), 20),
       ((select nextval('sequence_generator')), (select id from salon where name = 'Gentleman'),
        (select id from service where name = 'Haircut and beard'), 50),
       ((select nextval('sequence_generator')), (select id from salon where name = 'Gentleman'),
        (select id from service where name = 'Haircut'), 40),
       ((select nextval('sequence_generator')), (select id from salon where name = 'Gentleman'),
        (select id from service where name = 'Hair dye'), 60);

INSERT INTO employee(id, first_name, last_name, salon_id)
VALUES ((select nextval('sequence_generator')), 'Borislav', 'Petrov',
        (select id from salon where name = 'Uncle Bobby')),
       ((select nextval('sequence_generator')), 'Ivan', 'Georgiev', (select id from salon where name = 'Uncle Bobby')),
       ((select nextval('sequence_generator')), 'Georgi', 'Manev', (select id from salon where name = 'Gentleman')),
       ((select nextval('sequence_generator')), 'Petar', 'Petrov', (select id from salon where name = 'Gentleman'));

INSERT INTO image (id, name, salon_id)
VALUES ((select nextval('sequence_generator')),
        'uncle_bobi_1.jpg',
        (select id from salon where name = 'Uncle Bobby')),
       ((select nextval('sequence_generator')),
        'uncle_bobi_2.jpg',
        (select id from salon where name = 'Uncle Bobby')),
       ((select nextval('sequence_generator')),
        'gentleman_1.jpg',
        (select id from salon where name = 'Gentleman'))

