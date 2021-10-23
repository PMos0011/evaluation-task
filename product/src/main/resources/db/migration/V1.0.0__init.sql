create TABLE product
(
    credit_id bigint not null,
    product_name varchar not null,
    value decimal(19,2) not null,
    created timestamp not null,
    primary key (credit_id)
);