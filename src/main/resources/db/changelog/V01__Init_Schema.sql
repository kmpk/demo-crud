create table book
(
    id          bigserial    not null primary key,
    title       varchar(255) not null,
    isbn        varchar(255) not null
        constraint book_isbn_key unique,
    author      varchar(255) not null,
    description varchar(255) not null
);

