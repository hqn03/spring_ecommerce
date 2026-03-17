CREATE TABLE product_images
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    sku_id     BIGINT NULL,
    image_url  TEXT   NOT NULL,
    is_main    BOOLEAN   DEFAULT FALSE,
    sort_order INT       DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_img_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT fk_img_sku FOREIGN KEY (sku_id) REFERENCES skus (id) ON DELETE CASCADE

);

CREATE INDEX idx_product_id ON product_images(product_id);

CREATE INDEX idx_sku_id ON product_images(sku_id);