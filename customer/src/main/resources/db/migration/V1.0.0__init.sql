create TABLE customer
(
    credit_id bigint not null,
    first_name varchar not null,
    surname varchar not null,
    pesel varchar not null,
    created timestamp not null,
    primary key (credit_id)
);