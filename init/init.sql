DROP TABLE IF EXISTS users;

--CREATE DATABASE IF NOT EXISTS demo_db;
--USE demo_db;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Insert sample user
INSERT INTO users (username, password) VALUES ('admin', 'adminpass');