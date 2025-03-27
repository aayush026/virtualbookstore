package com.virtualbookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


import com.virtualbookstore.entity.Order.OrderStatus;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO{
    private Long id;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @NotEmpty(message = "Order must contain at least one item")
    @Size(max = 50, message = "Maximum 50 items per order")
    private List<OrderItemDTO> orderItems;

    @NotNull(message = "Total price is required")
    @PositiveOrZero(message = "Total price must be non-negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid price format")
    private BigDecimal totalPrice;

    private LocalDateTime orderDate;

    @NotNull(message = "Order status is required")
    private OrderStatus status;
}