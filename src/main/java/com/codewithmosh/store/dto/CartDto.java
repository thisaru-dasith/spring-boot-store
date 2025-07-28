package com.codewithmosh.store.dto;

import com.codewithmosh.store.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private UUID cartId;
    private Set<CartItemDto> items = new LinkedHashSet<>();
    private  BigDecimal totalPrice = BigDecimal.ZERO;



}
