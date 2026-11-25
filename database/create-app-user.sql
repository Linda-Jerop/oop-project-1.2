-- Run this once as the MySQL administrator (root), AFTER running schema.sql:
--     mysql -u root -p < database/create-app-user.sql
--
-- It creates the user that the app logs in with (see DatabaseConnection.java).
-- This user may only read and change the tables of strathmore_rooms. It cannot drop tables or touch other databases.
-- "stratizen_dev" is only a practice password for a database on your own computer.
-- Choose your own password, put the same one in DatabaseConnection.java, and never use a real password in code you share.

CREATE USER IF NOT EXISTS 'stratizen'@'localhost' IDENTIFIED BY 'stratizen_dev';
GRANT SELECT, INSERT, UPDATE, DELETE ON strathmore_rooms.* TO 'stratizen'@'localhost';
