CREATE TABLE carts
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NULL,
    session_id  VARCHAR(100) NULL,
    voucher_id  INT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,

    CONSTRAINT fk_cart_customer FOREIGN KEY (customer_id) REFERENCES customers (id)
);

CREATE INDEX idx_cart_customer ON carts(customer_id);
CREATE INDEX idx_cart_session ON carts(session_id);