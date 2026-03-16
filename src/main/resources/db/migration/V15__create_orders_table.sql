CREATE TABLE orders
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id        BIGINT         NOT NULL,
    order_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount       DECIMAL(15, 2) NOT NULL,
    status             ENUM('pending','processing', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',

    shipping_full_name VARCHAR(255)   NOT NULL,
    shipping_phone     VARCHAR(20)    NOT NULL,
    shipping_address   TEXT           NOT NULL,

    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers (id)
);