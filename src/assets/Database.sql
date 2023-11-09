CREATE DATABASE upeksha_communication;

USE upeksha_communication;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(45) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(45) NOT NULL,
    title INT, #     0) Developer    1) Super admin    2) Admin    3) User   4) Banded
    banded BIT
);

INSERT INTO users (user_name, email, password, title, banded)
VALUES ('developer', 'developer@gmail.com', '12345', 0, false),
       ('admin', 'admin@gmail.com', '12345', 1, false);

CREATE TABLE items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    item_name VARCHAR(100),
    set_or_reset_date DATE,
    set_or_reset_time TIME,
    stop_selling BIT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE bills (
    bill_number VARCHAR(15) PRIMARY KEY,
    user_id INT,
    discount DECIMAL(10, 2),
    total_price DECIMAL(10, 2),
    order_date DATE,
    order_time TIME,
    returns BIT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE log (
    user_id INT,
    log_name VARCHAR(100),
    log_type INT, #  1) [ERROR],   2) [WARNING],   3) [INFO]
    log_date DATE,
    log_time TIME,
    amount DECIMAL(10, 2),
    income_and_expenses_type INT, # 1) Sell Income   2) Add Money    3) Returns    4) Buy Items    5) Withdraw Money
    # 6) Pay Bills    7) Damage     8) Errors
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE stock (
    stock_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    item_id INT,
    quantity INT,
    refill_quantity INT,
    price DECIMAL(10, 2),
    selling_price DECIMAL(10, 2),
    refill_date DATE,
    refill_time TIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (item_id) REFERENCES items(item_id)
);

CREATE TABLE sells (
    sale_id VARCHAR(15) PRIMARY KEY,
    bill_number VARCHAR(15),
    item_id INT,
    stock_id INT,
    discount DECIMAL(10, 2),
    sale_amount DECIMAL(10, 2),
    profit DECIMAL(10, 2),
    quantity INT,
    edit BIT,
    returns BIT,
    FOREIGN KEY (bill_number) REFERENCES bills(bill_number),
    FOREIGN KEY (item_id) REFERENCES items(item_id)
);

CREATE TABLE sell_edits (
    user_id INT,
    sale_id VARCHAR(15),
    log_date DATE,
    log_time TIME,
    privies_amount DECIMAL(10, 2),
    new_amount DECIMAL(10, 2),
    privies_discount DECIMAL(10, 2),
    new_discount DECIMAL(10, 2),
    privies_quantity INT,
    new_quantity INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (sale_id) REFERENCES sells(sale_id)
);
