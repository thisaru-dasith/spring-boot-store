package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dto.AddItemRequestDto;
import com.codewithmosh.store.dto.CartItemDto;
import com.codewithmosh.store.dto.CartDto;
import com.codewithmosh.store.dto.UpdateItemDto;
import com.codewithmosh.store.exeptions.CartNotFound;
import com.codewithmosh.store.exeptions.ProductNotFound;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
@AllArgsConstructor
@RequestMapping("/carts")
@Tag(name = "Cart")
public class CartController {

    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CartMapper cartMapper;
    private CartService cartService;

    @PostMapping()
    public ResponseEntity<CartDto> createCart(){
        CartDto cartDto = cartService.createCart();
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);

    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addProductToCart(
            @PathVariable(name = "cartId") UUID cartId,
            @RequestBody AddItemRequestDto addItemRequestDto
    ){
        CartItemDto cartItemDto = cartService.addProductToCart(cartId, addItemRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(
            @PathVariable(name = "cartId") UUID cartId
    ){
        CartDto cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto);


    }

    @PutMapping("{cartId}/items/{productId}")
    public ResponseEntity<?> updateCart(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId,
            @RequestBody UpdateItemDto updateItemDto

    ){
        CartItemDto cartItemDto = cartService.updateCart(cartId, productId, updateItemDto);
        return ResponseEntity.ok(cartItemDto);

    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId
    ){
        cartService.deleteCartItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(
            @PathVariable(name = "cartId") UUID cartId
    ){

        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFound.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound(){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("Error", "Cart not found")
        );

    }
    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("Error", "Product not found")
        );
    }



}
