package com.codewithmosh.store.service;

import com.codewithmosh.store.dto.OrderResponseDto;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.exeptions.CartNotFound;
import com.codewithmosh.store.exeptions.OrderNotFound;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public User getCustomer(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long)authentication.getPrincipal();

        User user = userRepository.findById(userId).orElse(null);
        return user;
    }

    public List<OrderResponseDto> getAllOrders(){
        User customer = getCustomer();
        List<Order> allOrdersByCustomer = orderRepository.findAllByCustomer(customer);
        List<OrderResponseDto> orderResponseDto =  allOrdersByCustomer.stream().map(
                m -> orderMapper.toDto(m)
        ).toList();
        return orderResponseDto;
    }

    public OrderResponseDto getOrderById(Long id){
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null){
            throw new OrderNotFound();
        }
        return orderMapper.toDto(order);

    }
}
