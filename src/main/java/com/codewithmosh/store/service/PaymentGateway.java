package com.codewithmosh.store.service;

import com.codewithmosh.store.entities.Order;

public interface PaymentGateway {

    CheckOutSession createCheckOutSession(Order order);
}
