DROP TABLE IF EXISTS administrator CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS car CASCADE;
DROP TABLE IF EXISTS passport CASCADE;
DROP TABLE IF EXISTS license CASCADE;
DROP TABLE IF EXISTS images CASCADE;
DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS payment CASCADE;

CREATE TABLE administrator (
                               id SERIAL PRIMARY KEY,
                               email VARCHAR,
                               "name" VARCHAR,
                               surname VARCHAR,
                               "position" VARCHAR,
                               phone VARCHAR,
                               password VARCHAR
);

CREATE TABLE "user" (
                        id SERIAL PRIMARY KEY,
                        "name" VARCHAR,
                        surname VARCHAR,
                        email VARCHAR,
                        phone VARCHAR,
                        address VARCHAR,
                        "password" VARCHAR,
                        birthday DATE NULL
);

CREATE TABLE car (
                     id SERIAL PRIMARY KEY,
                     "type" VARCHAR,
                     price_per_day NUMERIC,
                     color VARCHAR,
                     brand VARCHAR,
                     volume INT4,
                     capacity INT4
);

CREATE TABLE passport (
                          user_id INT4 PRIMARY KEY,
                          passportnumber CHAR(14) NOT NULL,
                          CONSTRAINT passport_fk FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE license (
                         user_id INT4 PRIMARY KEY,
                         "number" CHAR(10) NOT NULL,
                         category VARCHAR NULL,
                         CONSTRAINT license_fk FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE images (
                        car_id INT4 PRIMARY KEY,
                        image_path VARCHAR NULL,
                        CONSTRAINT images_fk FOREIGN KEY (car_id) REFERENCES car (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE reservation (
                             reservation_id SERIAL PRIMARY KEY,
                             user_id INT4,
                             car_id INT4,
                             starting_date DATE,
                             ending_date DATE,
                             pickup_location VARCHAR,
                             return_location VARCHAR,
                             total_cost NUMERIC(10, 2),
                             arrival_time TIMESTAMP,
                             arrival_fine NUMERIC,
                             CONSTRAINT reservation_user_fk FOREIGN KEY (user_id) REFERENCES "user" (id),
                             CONSTRAINT reservation_car_fk FOREIGN KEY (car_id) REFERENCES car (id)
);

CREATE TABLE payment (
                         payment_id SERIAL PRIMARY KEY,
                         user_id INT4,
                         card_number CHAR(19),
                         card_holder VARCHAR,
                         expiration_date DATE,
                         cvc CHAR(4) NOT NULL,
                         CONSTRAINT payment_fk FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
);
