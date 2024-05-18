create table cond_way.users
(
    id         bigint auto_increment
        primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    email      varchar(255) not null,
    password   varchar(255) not null,
    constraint email
        unique (email)
);

