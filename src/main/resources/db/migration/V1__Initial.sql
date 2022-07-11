CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(45) NOT NULL,
    lastname VARCHAR(45) NOT NULL,
    email VARCHAR(60) UNIQUE NOT NULL,
    password VARCHAR(40) NOT NULL
);

INSERT INTO user(firstname, lastname, email, password) VALUES
(
 'Dmytro', 'Zhmur', 'dzhmur@griddynamics.com', 'aboba18'
),
(
 'Yevheniia', 'Komiahina', 'ykomiahina@griddynamics.com', 'password1'
),
(
 'Oleksandr', 'Kukurik', 'okukurik@girddynamics.com', 'password2'
),
(
 'Iryna', 'Subota', 'isubota@griddynamics.com', 'password3'
),
(
 'Tetiana', 'Komarova', 'tkomarova@griddynamics.com', 'password4'
);


