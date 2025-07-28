package com.codewithmosh.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutResponseDto {
    private Long OrderId;
    private String checkOutUrl;
}
