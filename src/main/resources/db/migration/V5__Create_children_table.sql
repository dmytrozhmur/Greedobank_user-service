CREATE TABLE child_account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(30) NOT NULL,
    contact_details VARCHAR(60),
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE user_child (
    user_id INT NOT NULL,
    child_id INT NOT NULL,
    linked_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (child_id) REFERENCES child_account(id)
        ON UPDATE CASCADE ON DELETE CASCADE
);