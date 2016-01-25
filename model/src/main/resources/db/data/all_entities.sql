TRUNCATE TABLE bts.map RESTART IDENTITY CASCADE;
TRUNCATE TABLE bts.station RESTART IDENTITY CASCADE;
TRUNCATE TABLE bts.arc RESTART IDENTITY CASCADE;
TRUNCATE TABLE bts.route RESTART IDENTITY CASCADE;
TRUNCATE TABLE bts.route_arc RESTART IDENTITY CASCADE;
TRUNCATE TABLE bts.route_pair RESTART IDENTITY CASCADE;
TRUNCATE TABLE bts."user" RESTART IDENTITY CASCADE;
TRUNCATE TABLE bts.bus RESTART IDENTITY CASCADE;
TRUNCATE TABLE bts.map_route RESTART IDENTITY CASCADE;


-- map
INSERT INTO bts.map (map_id, name, describe, is_deleted) VALUES (1, 'kharkov', 'Схема харьковских маршрутов', FALSE);

SELECT pg_catalog.setval(pg_get_serial_sequence('bts.map', 'map_id'), (SELECT MAX(map_id) FROM bts.map)+1);

-- station
INSERT INTO bts.station(station_id, name) VALUES (1, 'Тахиаташская');
INSERT INTO bts.station(station_id, name) VALUES (5, 'Киевская');
INSERT INTO bts.station(station_id, name) VALUES (6, 'Горбатый мост');
INSERT INTO bts.station(station_id, name) VALUES (7, 'Московский пр.');
INSERT INTO bts.station(station_id, name) VALUES (8, 'Советская');
INSERT INTO bts.station(station_id, name) VALUES (15, 'Парк Горького');
INSERT INTO bts.station(station_id, name) VALUES (25, 'Барабашова');
INSERT INTO bts.station(station_id, name) VALUES (36, 'Площадь Фейербаха');
INSERT INTO bts.station(station_id, name) VALUES (47, 'Площадь восстания');
INSERT INTO bts.station(station_id, name) VALUES (101, 'Тахиаташская');
INSERT INTO bts.station(station_id, name) VALUES (105, 'Киевская');
INSERT INTO bts.station(station_id, name) VALUES (106, 'Горбатый мост');
INSERT INTO bts.station(station_id, name) VALUES (107, 'Московский пр.');
INSERT INTO bts.station(station_id, name) VALUES (108, 'Советская');
INSERT INTO bts.station(station_id, name) VALUES (115, 'Парк Горького');
INSERT INTO bts.station(station_id, name) VALUES (125, 'Барабашова');
INSERT INTO bts.station(station_id, name) VALUES (136, 'Площадь Фейербаха');
INSERT INTO bts.station(station_id, name) VALUES (147, 'Площадь восстания');

SELECT pg_catalog.setval(pg_get_serial_sequence('bts.station', 'station_id'), (SELECT MAX(station_id) FROM bts.station)+1);

-- arc
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(101, 1, 5, 3);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(102, 5, 6, 2);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(103, 6, 7, 5);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(104, 7, 8, 5);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(115, 15, 5, 6);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(215, 25, 5, 7);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(315, 36, 6, 3);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(415, 47, 7, 4);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(1101, 101, 105, 3);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(1102, 105, 106, 2);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(1103, 106, 107, 5);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(1104, 107, 108, 5);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(1115, 115, 105, 6);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(1215, 125, 105, 7);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(1315, 136, 106, 3);
INSERT INTO bts.arc(arc_id, node_left_id, node_right_id, duration) VALUES(1415, 147, 107, 4);

SELECT pg_catalog.setval(pg_get_serial_sequence('bts.arc', 'arc_id'), (SELECT MAX(arc_id) FROM bts.arc)+1);

-- route
INSERT INTO bts.route(route_id, first_node_id, number, describe, cost, is_deleted) VALUES (1, 1, 171, '', 4.25, FALSE);
INSERT INTO bts.route(route_id, first_node_id, number, describe, cost, is_deleted) VALUES (11, 136, 171, '', 4.25, FALSE);
INSERT INTO bts.route(route_id, first_node_id, number, describe, cost, is_deleted) VALUES (2, 1, 172, '', 4, FALSE);
INSERT INTO bts.route(route_id, first_node_id, number, describe, cost, is_deleted) VALUES (12, 147, 172, '', 4, FALSE);
INSERT INTO bts.route(route_id, first_node_id, number, describe, cost, is_deleted) VALUES (3, 1, 173, '', 3, FALSE);
INSERT INTO bts.route(route_id, first_node_id, number, describe, cost, is_deleted) VALUES (13, 125, 173, '', 3, FALSE);
INSERT INTO bts.route(route_id, first_node_id, number, describe, cost, is_deleted) VALUES (4, 15, 174, '', 4.5, FALSE);
INSERT INTO bts.route(route_id, first_node_id, number, describe, cost, is_deleted) VALUES (14, 108, 174, '', 4.5, FALSE);
INSERT INTO bts.route(route_id, first_node_id, number, describe, cost, is_deleted) VALUES (5, 36, 175, '', 3.75, FALSE);
INSERT INTO bts.route(route_id, first_node_id, number, describe, cost, is_deleted) VALUES (15, 108, 175, '', 3.75, FALSE);

SELECT pg_catalog.setval(pg_get_serial_sequence('bts.route', 'route_id'), (SELECT MAX(route_id) FROM bts.route)+1);

-- route_arc
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (1, 101);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (1, 102);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (1, 315);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (11, 1315);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (11, 1102);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (11, 1101);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (2, 101);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (2, 102);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (2, 103);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (2, 415);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (12, 1415);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (12, 1103);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (12, 1102);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (12, 1101);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (3, 101);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (3, 215);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (13, 1215);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (13, 1101);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (4, 115);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (4, 102);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (4, 103);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (4, 104);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (14, 1104);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (14, 1103);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (14, 1102);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (14, 1115);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (5, 315);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (5, 103);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (5, 104);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (15, 1104);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (15, 1103);
INSERT INTO bts.route_arc(route_id, arc_id) VALUES (15, 1315);


-- pair
INSERT INTO bts.route_pair(route_forw_id, route_back_id) VALUES (1, 11);
INSERT INTO bts.route_pair(route_forw_id, route_back_id) VALUES (2, 12);
INSERT INTO bts.route_pair(route_forw_id, route_back_id) VALUES (3, 13);
INSERT INTO bts.route_pair(route_forw_id, route_back_id) VALUES (4, 14);
INSERT INTO bts.route_pair(route_forw_id, route_back_id) VALUES (5, 15);

-- map_route
INSERT INTO bts.map_route(map_id, route_id) VALUES (1, 1);
INSERT INTO bts.map_route(map_id, route_id) VALUES (1, 2);
INSERT INTO bts.map_route(map_id, route_id) VALUES (1, 3);
INSERT INTO bts.map_route(map_id, route_id) VALUES (1, 4);
INSERT INTO bts.map_route(map_id, route_id) VALUES (1, 5);
INSERT INTO bts.map_route(map_id, route_id) VALUES (1, 11);
INSERT INTO bts.map_route(map_id, route_id) VALUES (1, 12);
INSERT INTO bts.map_route(map_id, route_id) VALUES (1, 13);
INSERT INTO bts.map_route(map_id, route_id) VALUES (1, 14);
INSERT INTO bts.map_route(map_id, route_id) VALUES (1, 15);

-- user
INSERT INTO bts."user" (username, password, first_name, last_name, email, created, updated, is_deleted)
VALUES ('puva', 'pass', 'Vasiliy', 'Pupkin', 'puva@mail.ru', current_timestamp, current_timestamp, FALSE);
INSERT INTO bts."user" (username, password, first_name, last_name, email, created, updated, is_deleted)
VALUES ('siiva', 'pass', 'Sidorov', 'Ivan', 'siiva@mail.ru', current_timestamp, current_timestamp, FALSE);
INSERT  INTO bts."user" (username, password, first_name, last_name, email, created, updated, is_deleted)
VALUES ('dmma', 'pass', 'Dmitriev', 'Maxim', 'dmma@mail.ru', current_timestamp, current_timestamp, FALSE);
INSERT  INTO  bts."user" (username, password, first_name, last_name, email, created, updated, is_deleted)
VALUES ('vlbe', 'pass', 'Vladimir', 'Beluy', 'vlbe@mail.ru', current_timestamp, current_timestamp, FALSE);
INSERT INTO bts."user" (username, password, first_name, last_name, email, created, updated, is_deleted)
VALUES ('sema', 'pass', 'Sergienko', 'Maxim', 'sema@mail.ru', current_timestamp, current_timestamp, FALSE);
INSERT INTO bts."user" (username, password, first_name, last_name, email, created, updated, is_deleted)
VALUES ('kaal', 'pass', 'Kartomin', 'Alexandr', 'kaal@mail.ru', current_timestamp, current_timestamp, FALSE);
INSERT INTO bts."user" (username, password, first_name, last_name, email, created, updated, is_deleted)
VALUES ('alel', 'pass', 'Alekseeva', 'Elena', 'alel@mail.ru', current_timestamp, current_timestamp, FALSE);
INSERT INTO bts."user" (username, password, first_name, last_name, email, created, updated, is_deleted)
VALUES ('aban', 'pass', 'Abahina', 'Anastasia', 'aban@mail.ru', current_timestamp, current_timestamp, FALSE);
INSERT INTO bts."user" (username, password, first_name, last_name, email, created, updated, is_deleted)
VALUES ('avma', 'pass', 'Avdeeva', 'Mariya', 'avma@mail.ru', current_timestamp, current_timestamp, FALSE);
INSERT INTO bts."user" (username, password, first_name, last_name, email, created, updated, is_deleted)
VALUES ('miev', 'pass', 'Mironov', 'Evgeniy', 'miev@mail.ru', current_timestamp, current_timestamp, FALSE);



