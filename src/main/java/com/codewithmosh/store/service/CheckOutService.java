package com.codewithmosh.store.service;

import com.codewithmosh.exeptions.PaymentException;
import com.codewithmosh.store.dto.CheckOutRequestDto;
import com.codewithmosh.store.dto.CheckOutResponseDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.exeptions.CartItemsIsEmpty;
import com.codewithmosh.store.exeptions.CartNotFound;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CheckOutService {
    private final CartRepository cartRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final StripePaymentGateway stripePaymentGateway;

    @Value("${stripe.websiteUrl}")
    private String webSiteUrl;

    @Transactional
    public CheckOutResponseDto checkOut(CheckOutRequestDto checkOutRequestDto)  {

        Cart cart = cartRepository.findById(checkOutRequestDto.getCartId()).orElse(null);
        if (cart == null){
            throw new CartNotFound();
        }
        if (cart.itemsIsEmpty()){
            throw new CartItemsIsEmpty();
        }
        Order order = Order.fromCart(cart, orderService.getCustomer());
        Order save = orderRepository.save(order);

        try{
            var checkOutSession = stripePaymentGateway.createCheckOutSession(order);
            return new CheckOutResponseDto(save.getId(), checkOutSession.getCheckOutUrl());

        } catch (PaymentException e) {
            System.out.println(e.getMessage());
            orderRepository.delete(order);
            throw e;
        }



    }
}
