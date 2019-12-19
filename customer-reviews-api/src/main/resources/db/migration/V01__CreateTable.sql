CREATE TABLE products(
    product_id INT AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    product_description VARCHAR(1000) NOT NULL,
--    mongo_reviews VARCHAR(1000),
    PRIMARY KEY (product_id)
    );
CREATE TABLE reviews(
    review_id INT AUTO_INCREMENT,
    product_id INT NOT NULL,
    review VARCHAR(2000) NOT NULL,
    PRIMARY KEY (review_Id),
    CONSTRAINT review_product_fk
        FOREIGN KEY (product_id) references PRODUCTS (product_id)
);
CREATE TABLE comments(
    comment_id INT AUTO_INCREMENT,
    review_id INT,
    comment VARCHAR(2000) NOT NULL,
    mongo_review_id VARCHAR(100),
    PRIMARY KEY (comment_Id),
    CONSTRAINT comment_review_fk
        FOREIGN KEY (review_id) references REVIEWS (review_id)
);