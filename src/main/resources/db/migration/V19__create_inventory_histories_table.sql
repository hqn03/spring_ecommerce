CREATE TABLE inventory_histories
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku_id       BIGINT      NOT NULL,
    user_id      BIGINT,
    reference_id VARCHAR(50) NOT NULL,
    change_qty   INT         NOT NULL,
    type         ENUM('IMPORT', 'EXPORT', 'SALE', 'RETURN', 'ADJUSTMENT') NOT NULL,
    note         TEXT,

    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_inv_sku FOREIGN KEY (sku_id) REFERENCES skus (id) ON DELETE CASCADE,
    CONSTRAINT fk_inv_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE INDEX idx_inv_sku_id ON inventory_histories(sku_id);

CREATE INDEX idx_inv_created_at ON inventory_histories(created_at);