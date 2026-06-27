-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS campus_second_hand
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE campus_second_hand;

-- 2. 用户表
DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) NOT NULL COMMENT '昵称',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    role VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色：USER-普通用户，ADMIN-管理员',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户表';

-- 3. 分类表
DROP TABLE IF EXISTS category;
CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    sort_num INT NOT NULL DEFAULT 0 COMMENT '排序号',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='商品分类表';

-- 4. 商品表
DROP TABLE IF EXISTS product;
CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    title VARCHAR(100) NOT NULL COMMENT '商品标题',
    description TEXT COMMENT '商品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    image VARCHAR(255) DEFAULT NULL COMMENT '商品图片地址',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    user_id BIGINT NOT NULL COMMENT '发布用户ID',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '商品状态：PENDING待审核，ON_SALE已上架，OFF_SALE已下架',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id),
    CONSTRAINT fk_product_user FOREIGN KEY (user_id) REFERENCES user(id)
) COMMENT='商品表';

-- 5. 订单表
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    buyer_id BIGINT NOT NULL COMMENT '买家ID',
    seller_id BIGINT NOT NULL COMMENT '卖家ID',
    price DECIMAL(10,2) NOT NULL COMMENT '成交价格',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING_DEAL' COMMENT '订单状态：PENDING_DEAL待交易，FINISHED已完成，CANCELLED已取消',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_orders_product FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT fk_orders_buyer FOREIGN KEY (buyer_id) REFERENCES user(id),
    CONSTRAINT fk_orders_seller FOREIGN KEY (seller_id) REFERENCES user(id)
) COMMENT='订单表';

-- 6. 收藏表
DROP TABLE IF EXISTS favorite;
CREATE TABLE favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT fk_favorite_product FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT uk_user_product UNIQUE (user_id, product_id)
) COMMENT='收藏表';

-- 7. 初始化测试数据
INSERT INTO user (username, password, nickname, phone, role)
VALUES ('admin', '123456', '系统管理员', '13800000000', 'ADMIN');

INSERT INTO user (username, password, nickname, phone, role)
VALUES ('user1', '123456', '张三', '13900000001', 'USER');

INSERT INTO user (username, password, nickname, phone, role)
VALUES ('user2', '123456', '李四', '13900000002', 'USER');

INSERT INTO category (name, sort_num) VALUES ('书籍', 1);
INSERT INTO category (name, sort_num) VALUES ('数码', 2);
INSERT INTO category (name, sort_num) VALUES ('日用品', 3);
INSERT INTO category (name, sort_num) VALUES ('服饰鞋包', 4);

INSERT INTO product (title, description, price, image, category_id, user_id, status)
VALUES ('二手Java核心技术', '九成新，适合初学者', 25.00, '/images/java-book.svg', 1, 2, 'ON_SALE');

INSERT INTO product (title, description, price, image, category_id, user_id, status)
VALUES ('小米充电宝', '正常使用，无拆修', 45.00, '/images/power-bank.svg', 2, 3, 'ON_SALE');

INSERT INTO product (title, description, price, image, category_id, user_id, status)
VALUES ('宿舍台灯', '毕业转卖，功能正常', 20.00, '/images/lamp.svg', 3, 2, 'PENDING');
