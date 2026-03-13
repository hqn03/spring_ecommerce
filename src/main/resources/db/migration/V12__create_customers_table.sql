CREATE TABLE customers
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255) NOT NULL,
    phone       VARCHAR(20),
    email       VARCHAR(255),
    address     TEXT,
    total_spent DECIMAL(15, 2) DEFAULT 0,
    created_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,

    CONSTRAINT unique_user_id UNIQUE (user_id),
    CONSTRAINT unique_email UNIQUE (email),
    CONSTRAINT fk_customer_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE INDEX idx_customers_phone ON customers(phone);