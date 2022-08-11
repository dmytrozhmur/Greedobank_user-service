ALTER TABLE user ALTER COLUMN role_id SET DEFAULT 2;

INSERT INTO user (id, firstname, lastname, email, password) VALUES
(
    6, 'Taras', 'Chornyi', 'tchornyi@griddynamics.com', '$2a$10$UF9ekYo41ozrorv0DxfBpuqqODT9NQJPEZhVYKTjFmGoI28Mc1HHK'
),
(
    7, 'Ivan',  'Padalka', 'ipadalka@griddynamics.com', '$2a$10$DdnTguBfmz37VhYUnOGd5ON9AjJnlqBMHombYvwiex0D81TF7L5gy'
),
(
    8, 'Matvii', 'Fedyk', 'mfedyk@griddynamics.com', '$2a$10$RZJGzh4GdA73kwzIPGcp.ONxBft8DT0NbccvqZF2kmheDhMoIDg4u'
),
(
    9, 'Volodymyr', 'Moskaliuk', 'vmoskaliuk@griddynamics.com', '$2a$10$CBKp99shgVhndrR0DR.5Ue71k0fLGfJ4oygj7wAes7vi2PBSha8YG'
),
(
    10, 'Maksym', 'Khidko', 'mkhidko@griddynamics.com', '$2a$10$CUV06rlj.wY6QXkyqe3.yuIqp79FV16db8jm.hdUOXi67fR5IQRnm'
)