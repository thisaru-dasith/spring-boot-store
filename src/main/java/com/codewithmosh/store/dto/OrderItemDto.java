package com.codewithmosh.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Setter
@Getter
public class OrderItemDto {
    private ProductDtoForOrderItem product;
    private int quantity;
    private BigDecimal totalPrice;


}
