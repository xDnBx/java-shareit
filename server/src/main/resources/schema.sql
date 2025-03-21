DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE
);

CREATE TABLE requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description VARCHAR NOT NULL,
    requestor_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (requestor_id) REFERENCES users (id) ON DELETE RESTRICT
);

CREATE TABLE items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    owner_id BIGINT NOT NULL,
    request_id BIGINT,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE RESTRICT,
    FOREIGN KEY (request_id) REFERENCES requests (id) ON DELETE RESTRICT
);

CREATE TABLE bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id BIGINT NOT NULL,
    booker_id BIGINT NOT NULL,
    status VARCHAR NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE RESTRICT,
    FOREIGN KEY (booker_id) REFERENCES users (id) ON DELETE RESTRICT
);

CREATE TABLE comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text VARCHAR NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE RESTRICT,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE RESTRICT
);