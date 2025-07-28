package com.codewithmosh.store.exeptions;

public class CartItemsIsEmpty extends  RuntimeException{

    public CartItemsIsEmpty(){
        super("Cart Items is empty");
    }
}
