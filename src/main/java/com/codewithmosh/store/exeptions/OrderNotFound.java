package com.codewithmosh.store.exeptions;

public class OrderNotFound extends RuntimeException {
    public OrderNotFound()
    {
        super("Cart is not found");
    }
}
