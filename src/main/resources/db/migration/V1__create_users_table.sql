CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255),
    full_name VARCHAR(255),
    address VARCHAR(255),
    phone VARCHAR(20),
    enabled    BOOLEAN      NOT NULL DEFAULT FALSE,
    blocked    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT unique_username_deleted UNIQUE (username, deleted_at),
    CONSTRAINT unique_email_deleted UNIQUE (email, deleted_at)
);

