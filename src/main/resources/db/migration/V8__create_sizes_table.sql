CREATE TABLE sizes
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(20) NOT NULL,
    description VARCHAR(100),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    deleted     BOOLEAN   DEFAULT 0
);