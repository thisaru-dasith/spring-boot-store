package com.codewithmosh.store.entities;

import com.codewithmosh.store.util.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public static Order fromCart(Cart cart, User user){
        Order order = new Order();
        order.setStatus(Status.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(cart.getTotalPrice());
        order.setCustomer(user);

        cart.getItems().forEach(
                cartItem -> {OrderItem item = new OrderItem(order,cartItem.getProduct(),cartItem.getQuantity());
                    order.orderItems.add(item);
                }
        );
        return order;
    }



}
