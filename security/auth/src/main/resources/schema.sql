drop table if exists authorities;
drop table if exists users;

CREATE TABLE authorities
(
    username  text NOT NULL,
    authority text NOT NULL
);



CREATE TABLE users
(
    username text    NOT NULL,
    password text    NOT NULL,
    enabled  boolean NOT NULL
);

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (username);

CREATE UNIQUE INDEX ix_auth_username ON authorities USING btree (username, authority);

ALTER TABLE ONLY authorities
    ADD CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username);

