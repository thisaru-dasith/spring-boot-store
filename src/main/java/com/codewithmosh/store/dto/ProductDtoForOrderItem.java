package com.codewithmosh.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class ProductDtoForOrderItem {
    private Long id;
    private String name;
    private BigDecimal price;
}
