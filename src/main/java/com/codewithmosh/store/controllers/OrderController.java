package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dto.ErrorDto;
import com.codewithmosh.store.dto.OrderResponseDto;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.exeptions.OrderNotFound;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;

    @GetMapping()
    public List<OrderResponseDto> getAllOrders(){
        return  orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public OrderResponseDto getOrderById(@PathVariable(name = "orderId") long id){
        return orderService.getOrderById(id);
    }

    @ExceptionHandler(OrderNotFound.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }



}
