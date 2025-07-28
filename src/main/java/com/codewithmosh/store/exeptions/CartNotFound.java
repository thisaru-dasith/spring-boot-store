package com.codewithmosh.store.exeptions;

public class CartNotFound extends RuntimeException{

    public CartNotFound(){
        super("Cart not found");
    }
}
