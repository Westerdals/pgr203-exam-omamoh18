DROP TABLE IF EXISTS PLAYERS;

DROP TABLE IF EXISTS CATEGORIES;

CREATE TABLE PLAYERS
(
    id serial,
    name varchar(200) not null ,
    age integer

);

CREATE TABLE CATEGORIES
(
    id serial,
    name varchar(200) not null

);

INSERT INTO CATEGORIES (name) VALUES ('ONES'),('TWOS'),
                                     ('THREES'),('FOURS'),
                                     ('FIVES'),('SIXES'),
                                     ('ONE PAIR'),('TWO PAIRS'),
                                     ('THREE OF A KIND'),('FOUR OF A KIND'),
                                     ('SMALL STRAIGHT'),('LARGE STRAIGHT'),
                                     ('FULL HOUSE'),('CHANCE'),
                                     ('YATZY');