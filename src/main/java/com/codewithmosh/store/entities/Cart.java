package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BinaryOperator;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date", insertable = false, updatable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<CartItem> items = new LinkedHashSet<>();


    public BigDecimal getTotalPrice(){
       return items.stream()
               .map(cartItem -> cartItem.getTotalPrice())
               .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getCartItem(Long productId){
           return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findAny()
                .orElse(null);
    }

    public CartItem addCartItem(Product product){
        CartItem cartItem = getCartItem(product.getId());

        if (cartItem != null ){
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }else{
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            items.add(cartItem);
        }
        return cartItem;
    }

    public void removeCartItem(Long productId){
        CartItem cartItem = getCartItem(productId);
        if (cartItem != null){
            items.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public void removeAllCartItems(){
        items.clear();
    }

    public boolean itemsIsEmpty(){
        return items.isEmpty();
    }





}
