package com.virtualbookstore.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateOrderRequestDTO {
    private List<OrderItemRequest> items;

    // Required no-arg constructor
    public CreateOrderRequestDTO() {}

    // Full constructor with @JsonCreator
    @JsonCreator
    public CreateOrderRequestDTO(@JsonProperty("items") List<OrderItemRequest> items) {
        this.items = items;
    }

    // Getters and setters
    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public static class OrderItemRequest {
        private Long bookId;
        private int quantity;

        // Required no-arg constructor
        public OrderItemRequest() {}

        // Full constructor with @JsonCreator
        @JsonCreator
        public OrderItemRequest(
                @JsonProperty("bookId") Long bookId,
                @JsonProperty("quantity") int quantity) {
            this.bookId = bookId;
            this.quantity = quantity;
        }

        // Getters and setters
        public Long getBookId() {
            return bookId;
        }

        public void setBookId(Long bookId) {
            this.bookId = bookId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
//@Data
//@Builder
//@NoArgsConstructor  // Add this
//@AllArgsConstructor // Add this
//public class CreateOrderRequestDTO {
//    @NotNull
//    private List<OrderItemRequest> items;
//
//    @Data
//    @Builder
//    public static class OrderItemRequest {
//        @NotNull
//        private Long bookId;
//        
//        @Positive
//        private int quantity;
//    }
//}