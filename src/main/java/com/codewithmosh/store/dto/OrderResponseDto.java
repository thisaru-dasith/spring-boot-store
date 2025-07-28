package com.codewithmosh.store.dto;

import com.codewithmosh.store.util.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class OrderResponseDto {

    private Long id;
    private Status status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> orderItems;
    private BigDecimal totalPrice;

}
