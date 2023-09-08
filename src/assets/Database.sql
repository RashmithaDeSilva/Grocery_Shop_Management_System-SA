CREATE DATABASE upeksha_communication;

USE upeksha_communication;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

INSERT INTO users (user_name, email, password)
VALUES ('nipun', 'nipun@gmail.com', '12345');

CREATE TABLE items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(255),
    price DECIMAL(10, 2),
    selling_price DECIMAL(10, 2)
);

CREATE TABLE stock (
    stock_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT,
    quantity INT,
    refill_limit INT,
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
   FOREIGN KEY (user_id) REFERENCES users(user_id),
   FOREIGN KEY (item_id) REFERENCES items(item_id)
);
