CREATE DATABASE upeksha_communication;

USE upeksha_communication;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    title INT
);

INSERT INTO users (user_name, email, password, title)
VALUES ('admin', 'admin@gmail.com', '12345', 0);

-- CREATE TABLE items (
--     item_id INT AUTO_INCREMENT PRIMARY KEY,
--     item_name VARCHAR(255),
--     price DECIMAL(10, 2),
--     selling_price DECIMAL(10, 2)
-- );

CREATE TABLE items (
   item_id INT AUTO_INCREMENT PRIMARY KEY,
   item_name VARCHAR(255)
);

-- CREATE TABLE stock (
--     stock_id INT AUTO_INCREMENT PRIMARY KEY,
--     item_id INT,
--     quantity INT,
--     refill_limit INT,
--     FOREIGN KEY (item_id) REFERENCES items(item_id)
-- );

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
   sale_id INT AUTO_INCREMENT PRIMARY KEY,
   user_id INT,
   item_id INT,
   sale_date DATE,
   sale_time TIME,
   discount DECIMAL(10, 2),
   sale_amount DECIMAL(10, 2),
   quantity INT,
   edit BIT,
   FOREIGN KEY (user_id) REFERENCES users(user_id),
   FOREIGN KEY (item_id) REFERENCES items(item_id)
);

CREATE TABLE sell_edits (
     user_id INT,
     sale_id INT,
     log_date DATE,
     log_time TIME,
     privies_amount DECIMAL(10, 2),
     update_amount DECIMAL(10, 2),
     privies_quantity INT,
     update_quantity INT,
     FOREIGN KEY (user_id) REFERENCES users(user_id),
     FOREIGN KEY (sale_id) REFERENCES sells(sale_id)
);

CREATE TABLE log (
   user_id INT,
   log_name VARCHAR(255),
   log_type INT, #  1) [ERROR],   2) [WARNING],   3) [INFO]
   log_date DATE,
   log_time TIME,
   amount DECIMAL(10, 2),
   income_or_expenses BOOLEAN,
   FOREIGN KEY (user_id) REFERENCES users(user_id)
);
