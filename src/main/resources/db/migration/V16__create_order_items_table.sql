CREATE TABLE order_items
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id     BIGINT         NOT NULL,
    sku_code     VARCHAR(50)    NOT NULL,
    product_name VARCHAR(255)   NOT NULL,
    variant      VARCHAR(255)   NOT NULL,
    quantity     INT            NOT NULL DEFAULT 1,
    price        DECIMAL(15, 2) NOT NULL,
    image        TEXT NULL,

    CONSTRAINT fk_items_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);