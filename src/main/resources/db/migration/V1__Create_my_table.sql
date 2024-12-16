CREATE TABLE User
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    password VARCHAR(255)        NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    name     VARCHAR(255),
    role     VARCHAR(15)         NOT NULL
);
