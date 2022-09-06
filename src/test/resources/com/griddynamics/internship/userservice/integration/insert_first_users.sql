INSERT INTO role VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');

INSERT INTO user(id, firstname, lastname, email, password, role_id) VALUES
(
 1, 'Dmytro', 'Zhmur', 'dmytro.zhmur@nure.ua', '$2a$10$LDtyFWOF9vr5XFLasQYVUO8sbnmM8esVpghwPbp5oZqu6IRgkhVg6', 1
),
(
 2,'Oleksandr', 'Kukurik', 'okukurik@girddynamics.com', '$2a$10$nsZC3u1olcAv2Fr07xBBQeXHnthWGVZWsW6ZOe4cT7stc8obNF8Ny', 2
);
