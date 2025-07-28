package com.codewithmosh.store.service;


import com.codewithmosh.store.dto.AddItemRequestDto;
import com.codewithmosh.store.dto.CartDto;
import com.codewithmosh.store.dto.CartItemDto;
import com.codewithmosh.store.dto.UpdateItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.exeptions.CartNotFound;
import com.codewithmosh.store.exeptions.ProductNotFound;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {

    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CartMapper cartMapper;

    public CartDto createCart(){
        Cart createdCart = cartRepository.save(new Cart());
        return cartMapper.toDto(createdCart);

    }

    public CartItemDto addProductToCart(UUID cartId, AddItemRequestDto addItemRequestDto){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null){
            throw new CartNotFound();
        }

        Product product = productRepository.findById(addItemRequestDto.getProductId()).orElse(null);
        if (product == null){
            throw new ProductNotFound();
        }

        CartItem cartItem = cart.addCartItem(product);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);

    }

    public CartDto getCart(UUID cartId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null){
            throw new CartNotFound();
        }

        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCart(UUID cartId, Long productId, UpdateItemDto updateItemDto){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null){
            throw new CartNotFound();
        }
        CartItem cartItem = cart.getCartItem(productId);
        if (cartItem == null){
            throw new ProductNotFound();
        }

        cartItem.setQuantity(updateItemDto.getQuantity());
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);

    }

    public void deleteCartItem(UUID cartId, Long productId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null){
            throw new CartNotFound();
        }

        cart.removeCartItem(productId);
        cartRepository.save(cart);

    }

    public void clearCart(UUID cartId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null){
            throw new CartNotFound();
        }
        cart.removeAllCartItems();
        cartRepository.save(cart);
    }
}
