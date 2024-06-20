CREATE TABLE IF NOT EXISTS USER_ENTITY(
    USER_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    EMAIL VARCHAR(255),
    NAME VARCHAR(255),
    PASSWORD VARCHAR(255),
    CREATED_TIME TIMESTAMP,
    ROLE VARCHAR(10),
    ADDRESS VARCHAR(255),
    PHONE_NUMBER VARCHAR(255),
    MONEY BIGINT,
    LOYALTY_CARD BOOLEAN
);

INSERT INTO USER_ENTITY(EMAIL, NAME, PASSWORD, CREATED_TIME, ROLE, ADDRESS, PHONE_NUMBER, MONEY, LOYALTY_CARD)
VALUES('mati@admin.pl',
       'Mateusz',
       '$2a$10$cYyOJs2ZPlKToiyRh8MqyOQcmkMpE.j7Tms1z1wvZyA7UwGVrPs8i',
       '2023-07-26T13:50:34',
       'ADMIN',
       'ul. Aleje politechniki 13, 90-507 Lodz',
       '530995839',
       1400,
       true
);

INSERT INTO USER_ENTITY(EMAIL, NAME, PASSWORD, CREATED_TIME, ROLE, ADDRESS, PHONE_NUMBER, MONEY, LOYALTY_CARD)
VALUES('marcin@admin.pl',
       'Marcin',
       '$2a$10$oAZsPAzZ0t4cFncm.hbacOKMKZfIDqswmYxM1eaum15bSuYjyJSxK',
       '2023-06-23T07:11:23',
       'ADMIN',
       'ul. Slowackiego 7, 30-001 Krakow',
       '628945112',
       790,
       true
);

INSERT INTO USER_ENTITY(EMAIL, NAME, PASSWORD, CREATED_TIME, ROLE, ADDRESS, PHONE_NUMBER, MONEY, LOYALTY_CARD)
VALUES('mariusz@user.pl',
       'Mariusz',
       '$2a$10$2A0AAcnhVWjuvNZds.ESq.odtDhkW3kvamWrK9yUTh95y8Srr8ihe',
       '2023-08-31T11:23:45',
       'USER',
       'ul. Lecha 10, 60-001 Poznan',
       '775920455',
       320,
       false
);

INSERT INTO USER_ENTITY(EMAIL, NAME, PASSWORD, CREATED_TIME, ROLE, ADDRESS, PHONE_NUMBER, MONEY, LOYALTY_CARD)
VALUES('sam@icloud.com',
       'Samuel',
       '$2a$10$2A0AAcnhVWjuvNZds.ESq.odtDhkW3kvamWrK9yUTh95y8Srr8ihe',
       '2023-07-23T14:50:32',
       'USER',
       'ul. Kwiatowa 5, 00-001 Warszawa',
       '556090245',
       0,
       false
);

INSERT INTO USER_ENTITY(EMAIL, NAME, PASSWORD, CREATED_TIME, ROLE, ADDRESS, PHONE_NUMBER, MONEY, LOYALTY_CARD)
VALUES('davis@outlook.com',
       'David',
       '$2a$10$2A0AAcnhVWjuvNZds.ESq.odtDhkW3kvamWrK9yUTh95y8Srr8ihe',
       '2023-08-14T19:58:29',
       'USER',
       'Plac Wolnosci 1, 80-001 Gdansk',
       '662954003',
       200,
       true
);
