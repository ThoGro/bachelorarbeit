insert into book (isbn, title, author, category) values ('9783442151479', 'Bildung - Alles, was man wissen muss', 'Dietrich Schwanitz', 'SCIENCE');
insert into book (isbn, title, author, category) values ('9783806234770', 'Schwarze Flaggen', 'Joby Warrick', 'HISTORY');
insert into book (isbn, title, author, category) values ('9783764504458', 'Blackout', 'Marc Elsberg', 'FANTASY');

insert into user (username, password, role) values ('Admin', 'admin1', 'ADMIN');
insert into user (username, password, role) values ('Mitarbeiter', 'mitarbeiter', 'EMPLOYEE');
insert into user (username, password, role) values ('Kunde', 'kunde1', 'CUSTOMER');

insert into statistic (statistic_count, category) values (0, 'HISTORY');
insert into statistic (statistic_count, category) values (0, 'SCIENCE');
insert into statistic (statistic_count, category) values (0, 'FANTASY');