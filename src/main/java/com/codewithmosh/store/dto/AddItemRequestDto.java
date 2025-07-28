package com.codewithmosh.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AddItemRequestDto {
    @NotNull
    private Long productId;
}
