create table orders
(
    id          bigint auto_increment
        primary key,
    customer_id bigint         not null,
    created_at  datetime   default current_timestamp  not null,
    total_price decimal(10, 2) not null,
    constraint orders_customer_id_fk
        foreign key (customer_id) references `users` (id)
);

create table order_items
(
    id          bigint auto_increment
        primary key,
    order_id    bigint         not null,
    product_id  bigint         not null,
    unit_price  decimal(10, 2) not null,
    quantity    int            not null,
    total_price decimal(10, 2) not null,
    constraint order_items_order_id_fk
        foreign key (order_id) references `orders` (id),
    constraint order_items_product_id_fk
        foreign key (product_id) references `products` (id)
);
