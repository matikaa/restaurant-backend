CREATE TABLE IF NOT EXISTS FOOD_ENTITY(
    FOOD_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    CATEGORY_ID BIGINT,
    POSITION_ID BIGINT,
    FOOD_NAME VARCHAR(255),
    FOOD_PRICE INT
);

-- Starters
INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(1, 1, 'Bruschetta', 12.50);

INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(1, 2, 'Garlic Bread', 8.00);

INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(1, 3, 'Stuffed Mushrooms', 15.00);

-- Soups
INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(2, 1, 'Tomato Soup', 7.50);

INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(2, 2, 'Chicken Noodle Soup', 8.50);

INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(2, 3, 'Mushroom Soup', 9.00);

-- Main Courses
INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(3, 1, 'Grilled Chicken', 25.00);

INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(3, 2, 'Beef Steak', 30.00);

INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(3, 3, 'Vegetable Stir Fry', 20.00);

-- Desserts
INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(4, 1, 'Chocolate Cake', 10.00);

INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(4, 2, 'Ice Cream Sundae', 8.00);

INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(4, 3, 'Cheesecake', 9.00);

-- Cocktails
INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(5, 1, 'Mojito', 12.00);

INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(5, 2, 'Margarita', 11.00);

INSERT INTO FOOD_ENTITY(CATEGORY_ID, POSITION_ID, FOOD_NAME, FOOD_PRICE)
VALUES(5, 3, 'Old Fashioned', 14.00);
