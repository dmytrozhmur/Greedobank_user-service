CREATE TABLE child_account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    created_for_user INT,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (created_for_id) REFERENCES user(id)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE user_child (
    user_id INT NOT NULL,
    child_id INT NOT NULL,
    linked_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (child_id) REFERENCES child_account(id)
        ON UPDATE CASCADE ON DELETE CASCADE
);