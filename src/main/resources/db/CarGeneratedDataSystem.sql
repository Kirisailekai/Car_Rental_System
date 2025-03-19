-- Создание таблиц (DDL) остаётся без изменений.

-- Заполнение таблицы "user" (10 записей вместо 100)
INSERT INTO "user" (name, surname, email, phone, address, password) VALUES
                                                                        ('Leonid', 'Gagen', 'lgagen0@moonfruit.com', '130 448 7918', '0110 Carberry Avenue', 'zC5/Ex,R~CGW`#Nu'),
                                                                        ('Sara-ann', 'Parramore', 'sparramore1@yahoo.com', '707 229 7018', '02622 Sundown Point', 'rD4*X8WV8'),
                                                                        ('Dimitri', 'Massey', 'dmassey3@google.es', '400 623 3878', '7243 Sunnyside Place', 'oS8+s@FYynsO)2/'),
                                                                        ('Nate', 'Tackley', 'ntackley4@tinyurl.com', '318 306 8670', '0 Porter Terrace', 'iT5}BH~IXWWkVzV$'),
                                                                        ('Luca', 'Nawton', 'lnawton5@nbcnews.com', '597 874 3560', '18166 John Wall Circle', 'kG8.%72j)%UMUZ!$');

-- Заполнение таблицы "car" (10 записей вместо 100, исправлены бренды)
INSERT INTO car (type, price_per_day, color, brand, volume, capacity) VALUES
                                                                          ('MPV', 354, 'red', 'Bugatti', 65, 2),
                                                                          ('Sport', 221, 'white', 'Porsche', 72, 4),
                                                                          ('Sedan', 269, 'white', 'Ford', 79, 3),
                                                                          ('SUV', 307, 'black', 'Lamborghini', 63, 3),
                                                                          ('Coupe', 210, 'grey', 'Lamborghini', 65, 5);

-- Заполнение таблицы "license" (10 записей вместо 100)
INSERT INTO license (number, category) VALUES
                                           ('UJD0762743', 'A1'),
                                           ('DCW3732134', 'T'),
                                           ('XDD7257389', 'T'),
                                           ('EJL3490054', 'A'),
                                           ('JQN9732077', 'T');

-- Заполнение таблицы "passport" (10 записей вместо 100)
INSERT INTO passport (passportnumber) VALUES
                                          ('53163512271297'),
                                          ('72210987147794'),
                                          ('43301323520715'),
                                          ('08641862610161'),
                                          ('75133600481998');

-- Заполнение таблицы "images" (10 записей вместо 100)
INSERT INTO images (image_path) VALUES
                                    ('car (1).jpg'),
                                    ('car (2).jpg'),
                                    ('car (3).jpg'),
                                    ('car (4).jpg'),
                                    ('car (5).jpg');

-- Заполнение таблицы "payment" (10 записей вместо 100, даты в формате YYYY-MM-DD)
INSERT INTO payment (card_number, card_holder, expiration_date, cvc) VALUES
                                                                         ('4966 3097 1239 8917', (SELECT CONCAT(name, ' ', surname) FROM "user" WHERE id = 1), '2023-11-03', '498'),
                                                                         ('7994 0459 9951 7245', (SELECT CONCAT(name, ' ', surname) FROM "user" WHERE id = 2), '2021-06-17', '002'),
                                                                         ('8641 0940 2025 8425', (SELECT CONCAT(name, ' ', surname) FROM "user" WHERE id = 3), '2022-08-10', '145'),
                                                                         ('9706 8532 6430 5164', (SELECT CONCAT(name, ' ', surname) FROM "user" WHERE id = 4), '2023-11-21', '512'),
                                                                         ('2919 3685 2629 6214', (SELECT CONCAT(name, ' ', surname) FROM "user" WHERE id = 5), '2022-04-10', '092');

-- Заполнение таблицы "reservation" (10 записей вместо 100, даты в формате YYYY-MM-DD)
INSERT INTO reservation (user_id, car_id, starting_date, ending_date, pickup_location, return_location, total_cost) VALUES
                                                                                                                        (1, 1, '2023-11-17', '2023-05-11', '3 Burning Wood Street', '76438 Heath Center', 271),
                                                                                                                        (2, 2, '2023-11-27', '2023-11-27', '38 Gateway Center', '03 Becker Center', 365),
                                                                                                                        (3, 3, '2023-11-12', '2023-10-30', '2 Corry Point', '647 Anderson Place', 244),
                                                                                                                        (4, 4, '2023-11-20', '2023-12-27', '84 Dixon Point', '0 Browning Hill', 228),
                                                                                                                        (5, 5, '2023-11-24', '2023-11-12', '608 Jenifer Point', '4 Riverside Avenue', 346);

-- Администраторы (исправлены опечатки)
INSERT INTO administrator (email, name, surname, position, phone, password) VALUES
                                                                                ('kirisailekai.futatsu@gmail.com', 'Tilek', 'Sakyev', 'Director', '700913892', '1234567890'),
                                                                                ('nurislam.kubanychbekov@alatoo.edu.kg', 'Nur Islam', 'Kubanychbekov', 'Manager', '777301626', 'qwerty');