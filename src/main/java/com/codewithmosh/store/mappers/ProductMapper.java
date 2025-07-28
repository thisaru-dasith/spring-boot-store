package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dto.ProductDto;
import com.codewithmosh.store.dto.ProductDtoForOrderItem;
import com.codewithmosh.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toProductDto(Product product);

    Product toEntity(ProductDto productDto);

    ProductDtoForOrderItem toDto(Product product);

}
