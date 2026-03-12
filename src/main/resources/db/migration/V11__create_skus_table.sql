CREATE TABLE skus
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT         NOT NULL,
    color_id   INT,
    size_id    INT,
    sku_code   VARCHAR(50)    NOT NULL,
    price      DECIMAL(15, 2) NOT NULL DEFAULT 0,
    stock_qty  INT                     DEFAULT 0,

    CONSTRAINT unique_product_variant UNIQUE (product_id, color_id, size_id),
    CONSTRAINT unique_sku_code UNIQUE (sku_code),
    CONSTRAINT fk_skus_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT fk_skus_color FOREIGN KEY (color_id) REFERENCES colors (id) ON DELETE SET NULL,
    CONSTRAINT fk_skus_size FOREIGN KEY (size_id) REFERENCES sizes (id) ON DELETE SET NULL
)