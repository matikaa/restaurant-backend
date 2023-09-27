INSERT INTO USER_ENTITY(EMAIL, NAME, PASSWORD, CREATED_TIME, ROLE, ADDRESS, PHONE_NUMBER, MONEY, LOYALTY_CARD)
VALUES ('pro8l@admin.pl',
        'George',
        '$2a$10$oAZsPAzZ0t4cFncm.hbacOKMKZfIDqswmYxM1eaum15bSuYjyJSxK', -- admin
        '2023-06-23T07:11:23',
        'ADMIN',
        'ul. Ogrodowa 40, 86-400 Wroclaw',
        '512090967',
        2700,
        true);

INSERT INTO USER_ENTITY(EMAIL, NAME, PASSWORD, CREATED_TIME, ROLE, ADDRESS, PHONE_NUMBER, MONEY, LOYALTY_CARD)
VALUES ('mariuszek@user.pl',
        'Mariuszek',
        '$2a$10$2A0AAcnhVWjuvNZds.ESq.odtDhkW3kvamWrK9yUTh95y8Srr8ihe', -- user
        '2023-08-31T11:23:45',
        'USER',
        'ul. Piekna 230, 97-604 Rzeszow',
        '812944508',
        20,
        false);
