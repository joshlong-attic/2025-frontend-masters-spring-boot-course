drop table if exists customer;

create table if not exists customer
(
    id   serial primary key,
    name text not null
);