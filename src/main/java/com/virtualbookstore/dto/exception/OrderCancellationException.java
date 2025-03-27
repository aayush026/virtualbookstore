package com.virtualbookstore.dto.exception;

public class OrderCancellationException extends Exception {
    public OrderCancellationException() {
        super("Order cannot be cancelled");
    }

    public OrderCancellationException(String message) {
        super(message);
    }
}