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
VALUES ((select nextval('sequence_generator')), 'Haircut', 25),
       ((select nextval('sequence_generator')), 'Haircut and beard', 30)


