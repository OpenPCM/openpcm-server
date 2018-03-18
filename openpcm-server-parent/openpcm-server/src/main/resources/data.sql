/* Inserting the default roles */
INSERT INTO role (role_id, name)
VALUES (1, 'ROLE_ADMIN');

INSERT INTO role (role_id, name)
VALUES (2, 'ROLE_USER');

INSERT INTO role (role_id, name)
VALUES (3, 'ROLE_DOCTOR');

INSERT INTO role (role_id, name)
VALUES (4, 'ROLE_NURSE');

INSERT INTO role (role_id, name)
VALUES (5, 'ROLE_PATIENT');

/* Inserting default admin account */
INSERT INTO user (address_line_one, address_line_two, city, state, zip_code, date_of_birth, email, enabled, first_name, gender, last_name, maiden_name, middle_name, mrn, password, phone_number, ssn, username, user_id)
VALUES (NULL,NULL,NULL,NULL,NULL,NULL,'openpcm@gmail.com',true,'Admin',NULL,'User',NULL,NULL,NULL,'$2a$10$sfx.zurap9dhYL8fyb9Y.O82EBwIAhODSJJk9g7XoyY2vbfUpgzm2',NULL,NULL,'admin',1);

INSERT INTO user_role (user_id,role_id)
VALUES (1,1);