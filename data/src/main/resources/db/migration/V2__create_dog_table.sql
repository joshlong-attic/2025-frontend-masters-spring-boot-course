create table if not exists person
(
    id   serial primary key,
    name text   not null
);

create table if not exists dog
(
    id          serial primary key,
    name        text   not null,
    description text   not null,
    person      bigint null references person (id)
);

delete from dog;

delete from person;