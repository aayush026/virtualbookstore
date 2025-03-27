package com.virtualbookstore.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO	 {
    private Long id;

    @NotNull
    @Positive
    private Long bookId;

    private String bookTitle;

    @Positive
    private int quantity;

    @NotNull
    private BigDecimal pricePerUnit;

    @NotNull
    private BigDecimal totalPrice;
}