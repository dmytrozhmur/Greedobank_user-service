INSERT INTO role VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');

INSERT INTO user(id, firstname, lastname, email, password, role_id) VALUES
(
 1, 'Dmytro', 'Zhmur', 'dmytro.zhmur@nure.ua', 'password', 1
);