package com.virtualbookstore.dto.exception;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(Long id) {
        super("Order not found :  "+id);
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}