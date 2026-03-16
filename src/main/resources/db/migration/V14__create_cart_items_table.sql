CREATE TABLE cart_items
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id  BIGINT NOT NULL,
    sku_id   BIGINT NOT NULL,
    quantity INT    NOT NULL DEFAULT 1,

    CONSTRAINT fk_item_cart FOREIGN KEY (cart_id) REFERENCES carts (id) ON DELETE CASCADE,
    CONSTRAINT fk_item_sku FOREIGN KEY (sku_id) REFERENCES skus (id) ON DELETE CASCADE,
    UNIQUE KEY uq_cart_sku (cart_id, sku_id)
);