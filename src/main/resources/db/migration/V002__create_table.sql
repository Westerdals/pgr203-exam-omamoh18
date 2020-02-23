DROP TABLE IF EXISTS PLAYERS;

CREATE TABLE PLAYERS
(
  id serial,
  name varchar(200) not null ,
  age integer

);

DROP TABLE IF EXISTS CATEGORIES;

CREATE TABLE CATEGORIES
(
  id serial,
  name varchar(200) not null

);