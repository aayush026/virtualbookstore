package com.virtualbookstore.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtualbookstore.entity.Order;
import com.virtualbookstore.entity.User;
import com.virtualbookstore.repo.OrderRepo;
import com.virtualbookstore.repo.UserRepo;
import com.virtualbookstore.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepo orderRepository;
    
    @Autowired
    private UserRepo userRepository;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    private String extractUsernameFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.extractUserName(token);
    }
    
    public List<Order> getAllOrders(HttpServletRequest request) {
        extractUsernameFromRequest(request);
        return orderRepository.findAll();
    }
    
    public Optional<Order> getOrderById(Long id, HttpServletRequest request) {
        String username = extractUsernameFromRequest(request);
        return orderRepository.findById(id)
                .filter(order -> order.getUser().getUsername().equals(username));
    }
    
    public List<Order> getOrdersByUser(HttpServletRequest request) {
        String username = extractUsernameFromRequest(request);
        return orderRepository.findByUser_Email(username);
    }
    
    public Order createOrder(Order order, HttpServletRequest request) {
        String username = extractUsernameFromRequest(request);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);
        return orderRepository.save(order);
    }

    
    public Order updateOrder(Long id, Order orderDetails, HttpServletRequest request) {
        String username = extractUsernameFromRequest(request);
        return orderRepository.findById(id)
                .filter(order -> order.getUser().getUsername().equals(username))
                .map(order -> {
                    order.setStatus(orderDetails.getStatus());
                    order.setTotalAmount(orderDetails.getTotalAmount());
                    return orderRepository.save(order);
                }).orElseThrow(() -> new RuntimeException("Order not found or unauthorized"));
    }
    
    public void deleteOrder(Long id, HttpServletRequest request) {
        String username = extractUsernameFromRequest(request);
        Order order = orderRepository.findById(id)
                .filter(o -> o.getUser().getUsername().equals(username))
                .orElseThrow(() -> new RuntimeException("Order not found or unauthorized"));
        orderRepository.delete(order);
    }
}