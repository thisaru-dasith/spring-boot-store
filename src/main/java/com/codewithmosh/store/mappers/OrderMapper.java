package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dto.OrderResponseDto;
import com.codewithmosh.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponseDto toDto(Order order);
}
