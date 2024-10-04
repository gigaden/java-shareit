drop table if exists comments cascade;
drop table if exists bookings cascade;
drop table if exists items cascade;
drop table if exists requests cascade;
drop table if exists users cascade;
drop table if exists answers cascade;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                                        NOT NULL,
    email VARCHAR(512)                                        NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR(512)                                        NOT NULL,
    create_date  TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    requestor_id BIGINT                                              NOT NULL,
    FOREIGN KEY (requestor_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items
(
    id           BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(255)                                        NOT NULL,
    description  VARCHAR(512)                                        NOT NULL,
    is_available BOOLEAN,
    owner_id     BIGINT,
    request_id   BIGINT,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (request_id) REFERENCES requests (id) ON DELETE CASCADE
);


DROP TYPE IF EXISTS booking_status CASCADE;
CREATE TYPE booking_status AS ENUM ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    item_id    BIGINT                                              NOT NULL,
    booker_id  BIGINT                                              NOT NULL,
    status     booking_status default 'WAITING',
    FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE,
    FOREIGN KEY (booker_id) REFERENCES users (id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      VARCHAR(512)                                        NOT NULL,
    item_id   BIGINT                                              NOT NULL,
    author_id BIGINT                                              NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS answers
(
    id          BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_id     BIGINT                                              NOT NULL,
    owner_id    BIGINT                                              NOT NULL,
    request_id  BIGINT                                              NOT NULL,
    create_date TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (request_id) REFERENCES requests (id) ON DELETE CASCADE
);