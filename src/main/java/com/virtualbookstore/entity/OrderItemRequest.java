package com.virtualbookstore.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    private String bookTitle; // Instead of book_id, we use title
    private int quantity;
    private double price;
}
