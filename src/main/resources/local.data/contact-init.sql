CREATE TABLE IF NOT EXISTS CONTACT_ENTITY(
    CONTACT_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    CONTACT_MAIL VARCHAR(255),
    CONTACT_PHONE_NUMBER VARCHAR(255),
    OPENING_HOURS_DAYS VARCHAR(255),
    OPENING_HOURS_OPEN_TIME VARCHAR(255),
    OPENING_HOURS_CLOSE_TIME VARCHAR(255),
    ADDRESS_CITY VARCHAR(255),
    ADDRESS_STREET VARCHAR(255),
    ADDRESS_NUMBER INTEGER
);

INSERT INTO CONTACT_ENTITY(CONTACT_MAIL,
                           CONTACT_PHONE_NUMBER,
                           OPENING_HOURS_DAYS,
                           OPENING_HOURS_OPEN_TIME,
                           OPENING_HOURS_CLOSE_TIME,
                           ADDRESS_CITY,
                           ADDRESS_STREET,
                           ADDRESS_NUMBER)
VALUES('company@temp.com', '509283543', 'monday-saturday', '9.00', '22.00', 'Warsaw', 'Golden Street', 32);