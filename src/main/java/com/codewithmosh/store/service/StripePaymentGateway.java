package com.codewithmosh.store.service;


import com.codewithmosh.exeptions.PaymentException;
import com.codewithmosh.store.entities.Order;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripePaymentGateway implements PaymentGateway{
    @Value("${stripe.websiteUrl}")
    private String webSiteUrl;

    @Override
    public CheckOutSession createCheckOutSession(Order order) {

        try{
            SessionCreateParams.Builder builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(webSiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(webSiteUrl + "/checkout-cancel")
                    .putMetadata("order_id" ,order.getId().toString());


            order.getOrderItems().forEach(orderItem -> {
                var lineItem = SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(orderItem.getQuantity()))
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmountDecimal(orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(100)))
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(orderItem.getProduct().getName())
                                                        .build()
                                        )
                                        .build()
                        ).build();
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());
            return new CheckOutSession(session.getUrl());

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new PaymentException();
        }


    }
}
