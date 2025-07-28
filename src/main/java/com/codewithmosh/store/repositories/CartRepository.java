package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {




}
