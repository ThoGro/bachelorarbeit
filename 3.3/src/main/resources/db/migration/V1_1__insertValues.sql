insert into book (isbn, title, author, category) values ('9783442151479', 'Bildung - Alles, was man wissen muss', 'Dietrich Schwanitz', 'SCIENCE');
insert into book (isbn, title, author, category) values ('9783866801234', 'Schwarze Flaggen', 'Joby Warrick', 'HISTORY');
insert into book (isbn, title, author, category) values ('9783866809876', 'Blackout', 'Marc Elsberg', 'FANTASY');

insert into user (username, password, role) values ('Admin', 'admin', 'ADMIN');
insert into user (username, password, role) values ('Mitarbeiter', 'mitarbeiter', 'EMPLOYEE');
insert into user (username, password, role) values ('Kunde', 'kunde', 'CUSTOMER');

insert into statistic (count, category) values (0, 'HISTORY');
insert into statistic (count, category) values (0, 'SCIENCE');
insert into statistic (count, category) values (0, 'FANTASY');