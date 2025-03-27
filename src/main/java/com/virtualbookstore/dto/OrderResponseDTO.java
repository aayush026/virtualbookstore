package com.virtualbookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.virtualbookstore.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private BigDecimal totalPrice;
    private Order.OrderStatus status;
    private LocalDateTime orderDate;  // Changed from createdAt to match entity
    private List<OrderItemResponse> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private String bookTitle;
        private String bookIsbn;  // Added this field
        private int quantity;
        private BigDecimal pricePerUnit;
    }
}