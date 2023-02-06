CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(45) NOT NULL,
    lastname VARCHAR(45) NOT NULL,
    email VARCHAR(60) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL
);

INSERT INTO user(firstname, lastname, email, password) VALUES
(
 'Dmytro', 'Zhmur', 'dzhmur@griddynamics.com', '$2a$10$0dZOoSKB7sbHdaE4AHn6OOZa1scaQcA..LqrTSBB1OrohDAbCXCHm'
),
(
 'Yevheniia', 'Komiahina', 'ykomiahina@griddynamics.com', '$2a$10$LBzEJKnHOhvAkutOPYUhC.WC08FY0ZUkvVqLxINVfB8cRsnwpdjpy'
),
(
 'Oleksandr', 'Kukurik', 'okukurik@girddynamics.com', '$2a$10$nsZC3u1olcAv2Fr07xBBQeXHnthWGVZWsW6ZOe4cT7stc8obNF8Ny'
),
(
 'Iryna', 'Subota', 'isubota@griddynamics.com', '$2a$10$1maZO7sFXsgfMizZ5SQdEuZghbbVEhBPo2EgHV9LFK0m8qQTUVs0K'
),
(
 'Tetiana', 'Komarova', 'tkomarova@griddynamics.com', '$2a$10$FRPM1djz.q6nfAZOoFKjPecFmTCRts9Bq0UVMVLKSICuZBnsJur1m'
);


