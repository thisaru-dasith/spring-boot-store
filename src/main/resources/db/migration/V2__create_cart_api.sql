create table cart
(
    id   binary(16) default (UUID_TO_BIN(uuid())) not null
        primary key,
    date date       default (curdate())          not null
);

create table cart_item
(
    id         Bigint auto_increment
        primary key,
    cart_id    binary(16)    not null,
    product_id BIGINT        not null,
    quantity   int default 1 not null,
    constraint cart_item_cart_product_unique
        unique (product_id, cart_id),
    constraint cart_item_Cart_id_fk
        foreign key (cart_id) references cart (id)
            on delete cascade,
    constraint cart_item_products_id_fk
        foreign key (product_id) references products (id)
            on delete cascade
);


