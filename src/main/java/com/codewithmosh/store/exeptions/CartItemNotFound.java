package com.codewithmosh.store.exeptions;

public class CartItemNotFound extends RuntimeException {

    public CartItemNotFound(){
        super("Cart Item not found");
    }
}
