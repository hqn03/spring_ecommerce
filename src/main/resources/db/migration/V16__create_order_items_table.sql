CREATE TABLE order_items
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id        BIGINT         NOT NULL,
    sku_id          BIGINT         NOT NULL,
    product_name    VARCHAR(255)   NOT NULL,
    variant         VARCHAR(255)   NOT NULL,
    quantity        INT            NOT NULL DEFAULT 1,
    price           DECIMAL(15, 2) NOT NULL,
    original_price  DECIMAL(15, 2) NOT NULL,
    discount_amount DECIMAL(15, 2) NOT NULL,
    sub_total       DECIMAL(15, 2) NOT NULL,

    CONSTRAINT fk_items_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_items_sku FOREIGN KEY (sku_id) REFERENCES skus (id)
);