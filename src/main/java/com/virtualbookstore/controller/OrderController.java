package com.virtualbookstore.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.virtualbookstore.dto.CreateOrderRequestDTO;
import com.virtualbookstore.dto.CreateOrderRequestDTO.OrderItemRequest;
import com.virtualbookstore.dto.OrderResponseDTO;
import com.virtualbookstore.entity.Order;
import com.virtualbookstore.service.OrderService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 📌 Place Order
    @PostMapping("/placeOrder")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestBody @Valid CreateOrderRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        OrderResponseDTO response = orderService.createOrder(
            userDetails.getUsername(), 
            request.getItems()
        );
        return ResponseEntity.ok(response);
    }
    
    // 📌 Get All Orders (Admin Only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    
    // 📌 Get Orders for Logged-in User
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDTO>> getUserOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            orderService.getOrdersByUser(userDetails.getUsername())
        );
    }
    
    // 📌 Get Specific Order Details (User or Admin)
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderDetails(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        return ResponseEntity.ok(
            orderService.getOrderDetails(userDetails.getUsername(), orderId)
        );
    }

    // 📌 Update Order Status (Admin Only)
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam @NotBlank String status) throws Exception {
        return ResponseEntity.ok(
            orderService.updateOrderStatus(orderId, Order.OrderStatus.valueOf(status))
        );
    }

    // 📌 Cancel Order (Only for the User who Placed it)
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        orderService.cancelOrder(userDetails.getUsername(), orderId);
        return ResponseEntity.noContent().build();
    }

    // 📌 Estimate Order Total
    @PostMapping("/estimate")
    public ResponseEntity<BigDecimal> estimateOrderTotal(
            @Valid @RequestBody List<OrderItemRequest> items) {
        return ResponseEntity.ok(orderService.estimateOrderTotal(items));
    }
}
