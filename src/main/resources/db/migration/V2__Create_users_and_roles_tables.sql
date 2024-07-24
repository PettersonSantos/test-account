CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL
);

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       enabled BOOLEAN NOT NULL,
                       role_id INT REFERENCES roles(id)
);

CREATE INDEX idx_users_username ON users(username);