ALTER TABLE user ALTER COLUMN role_id SET DEFAULT 2;

INSERT INTO user (firstname, lastname, email, password) VALUES
(
    'Taras', 'Chornyi', 'tchornyi@griddynamics.com', '$2a$10$UF9ekYo41ozrorv0DxfBpuqqODT9NQJPEZhVYKTjFmGoI28Mc1HHK'
),
(
    'Ivan',  'Padalka', 'ipadalka@griddynamics.com', '$2a$10$DdnTguBfmz37VhYUnOGd5ON9AjJnlqBMHombYvwiex0D81TF7L5gy'
),
(
    'Matvii', 'Fedyk', 'mfedyk@griddynamics.com', '$2a$10$RZJGzh4GdA73kwzIPGcp.ONxBft8DT0NbccvqZF2kmheDhMoIDg4u'
),
(
    'Volodymyr', 'Moskaliuk', 'vmoskaliuk@griddynamics.com', '$2a$10$CBKp99shgVhndrR0DR.5Ue71k0fLGfJ4oygj7wAes7vi2PBSha8YG'
),
(
    'Maksym', 'Khidko', 'mkhidko@griddynamics.com', '$2a$10$CUV06rlj.wY6QXkyqe3.yuIqp79FV16db8jm.hdUOXi67fR5IQRnm'
);
