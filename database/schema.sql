-- STRATHMORE ROOM FINDER - MySQL script
-- Run it once in MySQL, for example:  mysql -u root -p < database/schema.sql
-- WARNING: running it again deletes the two tables and puts the sample data back.

CREATE DATABASE IF NOT EXISTS strathmore_rooms;
USE strathmore_rooms;

DROP TABLE IF EXISTS timetable;
DROP TABLE IF EXISTS rooms;

-- TABLE 1: rooms. One row for every room on campus.
-- room_type is either 'Lab' or 'Classroom'.
CREATE TABLE rooms (
    room_name VARCHAR(30) PRIMARY KEY,
    building  VARCHAR(30) NOT NULL,
    room_type VARCHAR(10) NOT NULL,
    capacity  INT         NOT NULL
);

-- TABLE 2: timetable. One row for every period when a room is in use.
-- It holds the normal class timetable AND the rooms booked by lecturers or class reps
-- (a booking is a row whose class_group is 'Booking').
-- day_of_week is written like 'Monday'. start_time and end_time are times like 08:15.
-- A room is occupied at a given day and time when one of its rows covers that time.
CREATE TABLE timetable (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    class_group   VARCHAR(20) NOT NULL,
    course_name   VARCHAR(80) NOT NULL,
    lecturer_name VARCHAR(80) NOT NULL,
    day_of_week   VARCHAR(10) NOT NULL,
    start_time    TIME        NOT NULL,
    end_time      TIME        NOT NULL,
    room          VARCHAR(30) NOT NULL,
    FOREIGN KEY (room) REFERENCES rooms(room_name)
);

-- ROOMS. The capacities are only guesses, change them to the real numbers.
INSERT INTO rooms (room_name, building, room_type, capacity) VALUES
    ('LT 1', 'Central Building', 'Classroom', 150),
    ('LT 2', 'Central Building', 'Classroom', 150),
    ('LT 3', 'Central Building', 'Classroom', 150),
    ('LT 4', 'Central Building', 'Classroom', 150),
    ('Suswa Lab', 'Central Building', 'Lab', 75),
    ('Masinga Lab', 'Central Building', 'Lab', 75),
    ('SLS Shaba', 'Oval Building', 'Classroom', 100),
    ('SS Zumaridi', 'Oval Building', 'Classroom', 100),
    ('STMB F1-02', 'STMB', 'Classroom', 75),
    ('STMB F1-03', 'STMB', 'Classroom', 75),
    ('STMB F1-04', 'STMB', 'Classroom', 75),
    ('STMB F1-05', 'STMB', 'Classroom', 75),
    ('STMB F2-01', 'STMB', 'Classroom', 75),
    ('STMB F2-02', 'STMB', 'Classroom', 75),
    ('STMB F2-03', 'STMB', 'Classroom', 75),
    ('STMB F2-04', 'STMB', 'Classroom', 75),
    ('STMB F2-05', 'STMB', 'Classroom', 75),
    ('STMB F5-01', 'STMB', 'Classroom', 75),
    ('STMB F5-02', 'STMB', 'Classroom', 75),
    ('STMB F5-03', 'STMB', 'Classroom', 75),
    ('STMB F5-04', 'STMB', 'Classroom', 75),
    ('STMB F5-05', 'STMB', 'Classroom', 75),
    ('MSB 1', 'MSB', 'Classroom', 80),
    ('MSB 2', 'MSB', 'Classroom', 80),
    ('MSB 3', 'MSB', 'Classroom', 80),
    ('MSB 4', 'MSB', 'Classroom', 80),
    ('MSB 5', 'MSB', 'Classroom', 80),
    ('MSB 6', 'MSB', 'Classroom', 80),
    ('MSB 7', 'MSB', 'Classroom', 80),
    ('MSB 8', 'MSB', 'Classroom', 80),
    ('MSB 9', 'MSB', 'Classroom', 80),
    ('MSB 10', 'MSB', 'Classroom', 80),
    ('MSB 11', 'MSB', 'Classroom', 80),
    ('MSB 12', 'MSB', 'Classroom', 80),
    ('MSB 13', 'MSB', 'Classroom', 80),
    ('MSB 14', 'MSB', 'Classroom', 80),
    ('FORGE FG-01', 'FORGE', 'Classroom', 75),
    ('FORGE FG-02', 'FORGE', 'Classroom', 75),
    ('FORGE F1-04', 'FORGE', 'Classroom', 75),
    ('LIB 1', 'LIB', 'Classroom', 80),
    ('SBS 1', 'SBS', 'Classroom', 100),
    ('SBS 2', 'SBS', 'Classroom', 100);

-- TIMETABLE: BICS 1A (Aug-Dec 2026)
INSERT INTO timetable (class_group, course_name, lecturer_name, day_of_week, start_time, end_time, room) VALUES
    ('BICS 1A', 'Object Oriented Programming I', 'Dr N Ochieng', 'Monday', '08:15', '10:15', 'STMB F5-01'),
    ('BICS 1A', 'Communication Skills II', 'E Gitonga', 'Monday', '10:15', '12:15', 'STMB F1-02'),
    ('BICS 1A', 'Computer Networks', 'H Talo', 'Monday', '14:15', '16:15', 'FORGE F1-04'),
    ('BICS 1A', 'Integral Calculus', 'A Muia', 'Tuesday', '08:15', '10:15', 'MSB 2'),
    ('BICS 1A', 'Communication Skills II', 'E Gitonga', 'Tuesday', '10:15', '11:15', 'MSB 4'),
    ('BICS 1A', 'Object Oriented Programming I', 'Dr N Ochieng', 'Tuesday', '12:15', '14:15', 'MSB 13'),
    ('BICS 1A', 'Maisha Program II 1A Gents', 'F Kalulu', 'Tuesday', '16:15', '17:15', 'MSB 7'),
    ('BICS 1A', 'Database Systems', 'N Maingi', 'Wednesday', '10:15', '12:15', 'FORGE FG-02'),
    ('BICS 1A', 'Integral Calculus', 'A Muia', 'Wednesday', '13:15', '14:15', 'MSB 9'),
    ('BICS 1A', 'Linear Algebra', 'Dr P Otieno', 'Thursday', '08:15', '10:15', 'MSB 9'),
    ('BICS 1A', 'Computer Networks', 'H Talo', 'Thursday', '10:15', '12:15', 'MSB 2'),
    ('BICS 1A', 'Database Systems', 'N Maingi', 'Thursday', '14:15', '16:15', 'MSB 14'),
    ('BICS 1A', 'Principles of Management', 'R Wanyama', 'Thursday', '16:15', '17:15', 'SBS 1'),
    ('BICS 1A', 'Principles of Management', 'R Wanyama', 'Friday', '10:15', '12:15', 'MSB 2'),
    ('BICS 1A', 'Linear Algebra', 'Dr P Otieno', 'Friday', '12:15', '13:15', 'MSB 5');

-- TIMETABLE: BICS 1B (Aug-Dec 2026)
INSERT INTO timetable (class_group, course_name, lecturer_name, day_of_week, start_time, end_time, room) VALUES
    ('BICS 1B', 'Integral Calculus', 'A Muia', 'Monday', '10:15', '12:15', 'STMB F5-04'),
    ('BICS 1B', 'Maisha Program II 1B Gents', 'G Mwendwa', 'Monday', '12:15', '13:15', 'STMB F2-01'),
    ('BICS 1B', 'Communication Skills II', 'E Gitonga', 'Monday', '15:15', '17:15', 'STMB F2-01'),
    ('BICS 1B', 'Computer Networks', 'H Talo', 'Tuesday', '08:15', '10:15', 'LIB 1'),
    ('BICS 1B', 'Object Oriented Programming I', 'Dr N Ochieng', 'Tuesday', '10:15', '12:15', 'FORGE FG-02'),
    ('BICS 1B', 'Communication Skills II', 'E Gitonga', 'Tuesday', '13:15', '14:15', 'STMB F5-02'),
    ('BICS 1B', 'Linear Algebra', 'Dr P Otieno', 'Tuesday', '14:15', '15:15', 'STMB F5-02'),
    ('BICS 1B', 'Principles of Management', 'R Wanyama', 'Wednesday', '08:15', '10:15', 'STMB F1-05'),
    ('BICS 1B', 'Object Oriented Programming I', 'Dr N Ochieng', 'Wednesday', '12:15', '14:15', 'STMB F5-01'),
    ('BICS 1B', 'Database Systems', 'N Maingi', 'Wednesday', '15:15', '17:15', 'STMB F1-05'),
    ('BICS 1B', 'Database Systems', 'N Maingi', 'Thursday', '08:15', '10:15', 'MSB 8'),
    ('BICS 1B', 'Linear Algebra', 'Dr P Otieno', 'Thursday', '10:15', '12:15', 'STMB F1-04'),
    ('BICS 1B', 'Integral Calculus', 'A Muia', 'Thursday', '16:15', '17:15', 'MSB 13'),
    ('BICS 1B', 'Computer Networks', 'H Talo', 'Friday', '10:15', '12:15', 'MSB 10'),
    ('BICS 1B', 'Principles of Management', 'R Wanyama', 'Friday', '13:15', '14:15', 'MSB 13');

-- TIMETABLE: CNS 1 (Aug-Dec 2026)
INSERT INTO timetable (class_group, course_name, lecturer_name, day_of_week, start_time, end_time, room) VALUES
    ('CNS 1', 'Database and Enterprise Systems', 'S Kibe', 'Monday', '08:15', '10:15', 'LT 4'),
    ('CNS 1', 'Introduction Computer Network', 'J Gichuhi', 'Monday', '10:15', '12:15', 'Suswa Lab'),
    ('CNS 1', 'Linear Algebra', 'Dr P Otieno', 'Monday', '14:15', '15:15', 'STMB F5-01'),
    ('CNS 1', 'Fundamentals of Cyber Security', 'E Edwine', 'Monday', '15:15', '17:15', 'STMB F1-02'),
    ('CNS 1', 'Introduction Computer Network', 'J Gichuhi', 'Tuesday', '08:15', '10:15', 'MSB 9'),
    ('CNS 1', 'Object Oriented Programming', 'Dr Orero', 'Tuesday', '10:15', '12:15', 'MSB 8'),
    ('CNS 1', 'Communication Skills II', 'J Njiru', 'Tuesday', '14:15', '16:15', 'MSB 5'),
    ('CNS 1', 'Fundamentals of Cyber Security', 'E Edwine', 'Wednesday', '08:15', '10:15', 'Masinga Lab'),
    ('CNS 1', 'Calculus II', 'E Akinyi', 'Wednesday', '11:15', '12:15', 'STMB F5-01'),
    ('CNS 1', 'Communication Skills II', 'J Njiru', 'Wednesday', '13:15', '14:15', 'MSB 2'),
    ('CNS 1', 'Object Oriented Programming', 'Dr Orero', 'Wednesday', '14:15', '16:15', 'MSB 5'),
    ('CNS 1', 'Calculus II', 'E Akinyi', 'Thursday', '08:15', '10:15', 'STMB F1-04'),
    ('CNS 1', 'Database and Enterprise Systems', 'S Kibe', 'Thursday', '14:15', '16:15', 'Suswa Lab'),
    ('CNS 1', 'Linear Algebra', 'Dr P Otieno', 'Friday', '08:15', '10:15', 'SBS 1');
