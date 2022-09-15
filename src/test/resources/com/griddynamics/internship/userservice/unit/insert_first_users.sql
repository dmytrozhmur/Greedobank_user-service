INSERT INTO role VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');

INSERT INTO user(id, firstname, lastname, email, password, role_id) VALUES
                                                                        (
                                                                            1, 'Dmytro', 'Zhmur', 'dzhmur@griddynamics.com', '$2a$10$LDtyFWOF9vr5XFLasQYVUO8sbnmM8esVpghwPbp5oZqu6IRgkhVg6', 1
                                                                        ),
                                                                        (
                                                                            2, 'Tetiana', 'Komarova', 'tkomarova@griddynamics.com', '$2a$10$FRPM1djz.q6nfAZOoFKjPecFmTCRts9Bq0UVMVLKSICuZBnsJur1m', 2
                                                                        );