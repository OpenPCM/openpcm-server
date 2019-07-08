/* Inserting the default roles */
INSERT INTO role (role_id, name)
VALUES (14505, 'ROLE_ADMIN');

INSERT INTO role (role_id, name)
VALUES (14506, 'ROLE_USER');

INSERT INTO role (role_id, name)
VALUES (14507, 'ROLE_DOCTOR');

INSERT INTO role (role_id, name)
VALUES (14508, 'ROLE_NURSE');

INSERT INTO role (role_id, name)
VALUES (14509, 'ROLE_PATIENT');

/* Inserting default admin account */
INSERT INTO user (address_line_one, address_line_two, city, state, zip_code, date_of_birth, email, enabled, first_name, gender, last_name, maiden_name, middle_name, mrn, password, phone_number, ssn, username, user_id)
VALUES (NULL,NULL,NULL,NULL,NULL,NULL,'openpcm@gmail.com',true,'Admin',NULL,'User',NULL,NULL,NULL,'$2a$10$sfx.zurap9dhYL8fyb9Y.O82EBwIAhODSJJk9g7XoyY2vbfUpgzm2',NULL,NULL,'admin',14510);

INSERT INTO user (address_line_one, address_line_two, city, state, zip_code, date_of_birth, email, enabled, first_name, gender, last_name, maiden_name, middle_name, mrn, password, phone_number, ssn, username, user_id)
VALUES (NULL,NULL,NULL,NULL,NULL,NULL,'demo@gmail.com',true,'Demo',NULL,'User',NULL,NULL,'15489672','$2a$10$sfx.zurap9dhYL8fyb9Y.O82EBwIAhODSJJk9g7XoyY2vbfUpgzm2','305-303-3033','123-45-6789','demo',14511);


INSERT INTO user_role (user_id,role_id)
VALUES (14510,14505);

INSERT INTO user_role (user_id,role_id)
VALUES (14511,14506);

INSERT INTO encounter_type (name, encounter_type_id)
VALUES('CHECKUP', 14512);
