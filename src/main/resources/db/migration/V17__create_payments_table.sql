CREATE TABLE payments
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id VARCHAR(255)   NOT NULL,
    order_id          BIGINT         NOT NULL,
    amount            DECIMAL(19, 2) NOT NULL,
    currency          VARCHAR(10),
    status            ENUM('PAID', 'FAILED', 'REFUNDED'),
    payment_method    VARCHAR(50),
    created_at        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_session_id UNIQUE (session_id),
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders (id)
);