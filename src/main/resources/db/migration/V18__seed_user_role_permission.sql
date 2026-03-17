INSERT INTO permissions (name, description) VALUES
('USER:CREATE', 'Create new user'),
('USER:READ', 'View user'),
('USER:UPDATE', 'Update user'),
('USER:DELETE', 'Delete user'),
('ROLE:CREATE', 'Create new role'),
('ROLE:READ', 'View role'),
('ROLE:UPDATE', 'Update role'),
('ROLE:DELETE', 'Delete role'),
('ROLE:ASSIGN_PERMISSION', 'Assign permission to role'),
('COLOR:CREATE', 'Create color'),
('COLOR:READ', 'View color'),
('COLOR:UPDATE', 'Update color'),
('COLOR:DELETE', 'Delete color'),
('SIZE:CREATE', 'Create size'),
('SIZE:READ', 'View size'),
('SIZE:UPDATE', 'Update size'),
('SIZE:DELETE', 'Delete size'),
('CATEGORY:CREATE', 'Create category'),
('CATEGORY:READ', 'View category'),
('CATEGORY:UPDATE', 'Update category'),
('CATEGORY:DELETE', 'Delete category'),
('PRODUCT:CREATE', 'Create product'),
('PRODUCT:READ', 'View product'),
('PRODUCT:UPDATE', 'Update product'),
('PRODUCT:DELETE', 'Delete product');


INSERT INTO roles (name, description) VALUES
('SUPER_ADMIN', 'System super administrator'),
('ADMIN', 'Administrator'),
('USER', 'Normal user');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
    CROSS JOIN permissions p
WHERE r.name = 'SUPER_ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
    JOIN permissions p
    ON p.name IN (
      'USER:CREATE',
      'USER:READ',
      'USER:UPDATE',
      'USER:DELETE',
      'ROLE:READ'
    )
WHERE r.name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
    JOIN permissions p ON p.name = 'USER:READ'
WHERE r.name = 'USER';

INSERT INTO users (username, email, password, enabled, blocked)
VALUES ('superadmin', 'superadmin@gmail.com', '$2a$10$Uf8hesYwFaWzZz11AzX1nOmqV5lHn/hb2o4F6tkzl1XTttJm3.nCq', true, false),
       ('admin', 'admin@gmail.com', '$2a$10$Uf8hesYwFaWzZz11AzX1nOmqV5lHn/hb2o4F6tkzl1XTttJm3.nCq', true, false),
       ('qclabburn1', 'tsharple1@1und1.de', 'rC8&W''+.FIF"nWP', false, false),
       ('nreef2', 'etinman2@google.co.uk', 'xF5,gi+hWA_LF', false, true),
       ('ekamen0', 'swanless0@umich.edu', 'nA0!wjY`z', true, false),
       ('agotfrey3', 'nmcmonies3@discuz.net', 'oJ2''.Mb*UO*', true, true),
       ('lblinkhorn4', 'mdumphries4@ebay.co.uk', 'aU3}2)KB', true, true),
       ('ycrilley5', 'sblackborough5@ezinearticles.com', 'rY0''>?XVq)yT', true, true),
       ('dpresho6', 'glinneman6@cornell.edu', 'eJ5}iQ9gv\l+!vmU', false, false),
       ('sdanford7', 'svaldes7@jimdo.com', 'xF1#_1MYsfn+,''x', true, false),
       ('mpinchen8', 'gwellman8@examiner.com', 'aB0|*z$u*(rl.eL', true, false),
       ('psemonin9', 'lmccolgan9@chron.com', 'pP4!g3OU0', false, true);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
    JOIN roles r ON r.name = 'SUPER_ADMIN'
WHERE u.username = 'superadmin';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
    JOIN roles r ON r.name = 'ADMIN'
WHERE u.username = 'admin';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.name = 'USER'
WHERE u.username NOT IN ('admin', 'superadmin');

INSERT INTO colors (name, hex_code)
VALUES ('Black', '#000000'),
       ('White', '#FFFFFF'),
       ('Red', '#FF0000'),
       ('Gray', '#808080');

INSERT INTO sizes (name)
VALUES ('S'),
    ('M'),
    ('L'),
    ('XL'),
    ('XXL');


INSERT INTO categories (name, slug, description, parent_id)
VALUES ('Men', 'men', 'Men fashion', NULL),
       ('Women', 'women', 'Women fashion', NULL),
       ('Kids', 'kids', 'Kids fashion', NULL),
       ('Accessories', 'accessories', 'Fashion accessories', NULL);

INSERT INTO categories (name, slug, description, parent_id)
VALUES ('T-Shirts', 'men-tshirts', 'Men t-shirts', 1),
       ('Shirts', 'men-shirts', 'Men shirts', 1),
       ('Jeans', 'men-jeans', 'Men jeans', 1),
       ('Dresses', 'women-dresses', 'Women dresses', 2),
       ('Skirts', 'women-skirts', 'Women skirts', 2),
       ('Blouses', 'women-blouses', 'Women blouses', 2),
       ('Toys', 'kids-toys', 'Kids toys', 3),
       ('Kids Clothing', 'kids-clothing', 'Kids clothing', 3),
       ('Bags', 'bags', 'Fashion bags', 4),
       ('Watches', 'watches', 'Wrist watches', 4),
       ('Sunglasses', 'sunglasses', 'Fashion sunglasses', 4);

INSERT INTO products (name, slug, description, category_id)
VALUES
-- Men T-Shirts (ID 5)
('Basic Cotton Tee', 'basic-cotton-tee', 'High quality cotton', 5),
('V-Neck Summer Shirt', 'v-neck-summer-shirt', 'Breathable fabric', 5),
-- Men Shirts (ID 6)
('Oxford Button Down', 'oxford-button-down', 'Classic formal look', 6),
('Flannel Checkered Shirt', 'flannel-checkered-shirt', 'Warm and cozy', 6),
-- Men Jeans (ID 7)
('Slim Fit Denim', 'slim-fit-denim', 'Stretchable denim', 7),
('Classic Straight Jeans', 'classic-straight-jeans', 'Timeless style', 7),
-- Women Dresses (ID 8)
('Floral Summer Dress', 'floral-summer-dress', 'Beautiful floral print', 8),
('Little Black Dress', 'little-black-dress', 'Elegant evening wear', 8),
-- Women Skirts (ID 9)
('Pleated Midi Skirt', 'pleated-midi-skirt', 'Flowy and stylish', 9),
('Denim Mini Skirt', 'denim-mini-skirt', 'Casual daily wear', 9),
-- Women Blouses (ID 10)
('Silk Office Blouse', 'silk-office-blouse', 'Professional look', 10),
('Chiffon Party Top', 'chiffon-party-top', 'Light and airy', 10),
-- Kids Toys (ID 11)
('Building Blocks Set', 'building-blocks', 'Educational toy', 11),
('Plush Bear', 'plush-bear', 'Soft and cuddly', 11),
-- Kids Clothing (ID 12)
('Cartoon Print Hoodie', 'kids-hoodie', 'Fun for kids', 12),
('Toddler Cotton Set', 'toddler-set', 'Gentle on skin', 12),
-- Bags (ID 13)
('Leather Tote Bag', 'leather-tote', 'Spacious and durable', 13),
('Canvas Backpack', 'canvas-backpack', 'For school and travel', 13),
-- Watches (ID 14)
('Classic Silver Watch', 'silver-watch', 'Quartz movement', 14),
-- Sunglasses (ID 15)
('Aviator Sunglasses', 'aviator-sun', 'UV400 protection', 15);

INSERT IGNORE INTO skus (product_id, color_id, size_id, sku_code, price, stock_qty)
SELECT
    p.id,
    c.id,
    s.id,
    UPPER(CONCAT(SUBSTRING(p.slug, 1, 3), p.id, '-', c.name, '-', s.name)) AS sku_code, -- Thêm p.id vào code để đảm bảo duy nhất
    (250000 + (p.id * 5000)) AS price,
    100 AS stock_qty
FROM products p
         CROSS JOIN colors c
         CROSS JOIN sizes s
WHERE p.id BETWEEN (SELECT MIN(id) FROM (SELECT id FROM products ORDER BY id DESC LIMIT 20) AS t) AND (SELECT MAX(id) FROM products)
  AND c.name IN ('Black', 'White')
  AND s.name IN ('S', 'M', 'L', 'XL', 'XXL');

-- INSERT PRODUCT IMAGES
INSERT INTO product_images (product_id, sku_id, image_url, is_main, sort_order)

-- PHẦN A: 5 ảnh chung cho mỗi sản phẩm
SELECT
    p.id as product_id,
    NULL as sku_id,
    CONCAT('https://picsum.photos/seed/p-', p.id, '-', n.num, '/800/1000') as image_url,
    (n.num = 1) as is_main, -- Chỉ ảnh đầu tiên (số 1) là ảnh chính
    n.num as sort_order
FROM products p
         CROSS JOIN (
    SELECT 1 AS num UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
) n
WHERE p.id BETWEEN (SELECT MIN(id) FROM (SELECT id FROM products ORDER BY id DESC LIMIT 20) AS t) AND (SELECT MAX(id) FROM products)

UNION ALL

-- PHẦN B: 3 ảnh riêng cho mỗi SKU
SELECT
    s.product_id,
    s.id as sku_id,
    CONCAT('https://picsum.photos/seed/sku-', s.id, '-', n.num, '/800/1000') as image_url,
    FALSE as is_main,
    (n.num + 10) as sort_order -- Đặt thứ tự lớn hơn để nằm sau ảnh chung
FROM skus s
         CROSS JOIN (
    SELECT 1 AS num UNION ALL SELECT 2 UNION ALL SELECT 3
) n
WHERE s.product_id BETWEEN (SELECT MIN(id) FROM (SELECT id FROM products ORDER BY id DESC LIMIT 20) AS t) AND (SELECT MAX(id) FROM products);