package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dto.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @GetMapping("/products")
    public List<ProductDto> getAllProducts(
            @RequestParam(name = "sort", required = false) Byte categoryId,
           @RequestHeader(name = "x-auth-token",required = false) String authToken

    ){
        List<Product> products;
        if (categoryId != null){
           products = productRepository.findByCategoryId(categoryId);
        }else{
            products = productRepository.findAll();
        }
        //System.out.println(authToken);
        return products.stream()
                .map(productMapper::toProductDto)
                .toList();

    }

    @GetMapping("/products/{id}")
    public ProductDto getProductById(@PathVariable Long id){
        Product product = productRepository.findById(id).orElse(null);
        return  productMapper.toProductDto(product);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriBuilder
    ){
        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);

        ProductDto savedProductDto = productMapper.toProductDto(product);

        URI uri = uriBuilder.path("/products/{id").build().encode().toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null){
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }






}
