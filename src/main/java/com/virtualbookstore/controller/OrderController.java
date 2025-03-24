package com.virtualbookstore.controller;


import com.virtualbookstore.entity.Order;
import com.virtualbookstore.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    public List<Order> getAllOrders(HttpServletRequest request) {
        return orderService.getAllOrders(request);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id, HttpServletRequest request) {
        return orderService.getOrderById(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user")
    public List<Order> getOrdersByUser(HttpServletRequest request) {
        return orderService.getOrdersByUser(request);
    }
    
    @PostMapping
    public Order createOrder(@RequestBody Order order, HttpServletRequest request) {
        return orderService.createOrder(order, request);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails, HttpServletRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderDetails, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id, HttpServletRequest request) {
        orderService.deleteOrder(id, request);
        return ResponseEntity.noContent().build();
    }
}