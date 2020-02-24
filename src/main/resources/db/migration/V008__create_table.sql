DROP TABLE IF EXISTS PLAYERS;

DROP TABLE IF EXISTS CATEGORIES;

DROP TABLE IF EXISTS GAME;

DROP TABLE IF EXISTS GAMESESSION;

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

CREATE TABLE GAME
(
    id serial,
    player varchar(200) not null,
    category varchar(50) not null,
    dice varchar(100),
    score varchar(250),
    session varchar(200)
);

CREATE TABLE GAMESESSION
(

    id serial,
    name varchar(200),
    description varchar(200)
);