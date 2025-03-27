package com.virtualbookstore.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.virtualbookstore.dto.CreateOrderRequestDTO;
import com.virtualbookstore.dto.OrderResponseDTO;
import com.virtualbookstore.dto.exception.InsufficientStockException;
import com.virtualbookstore.dto.exception.OrderCancellationException;
import com.virtualbookstore.dto.exception.OrderNotFoundException;
import com.virtualbookstore.entity.Book;
import com.virtualbookstore.entity.Order;
import com.virtualbookstore.entity.OrderItem;
import com.virtualbookstore.entity.User;
import com.virtualbookstore.repo.BookRepo;
import com.virtualbookstore.repo.OrderRepo;
import com.virtualbookstore.repo.UserRepo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepo orderRepository;
    private final UserRepo userRepository;
    private final BookRepo bookRepository;

    @Transactional
    public OrderResponseDTO createOrder(String email, List<CreateOrderRequestDTO.OrderItemRequest> items) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CreateOrderRequestDTO.OrderItemRequest item : items) {
            Book book = bookRepository.findById(item.getBookId())
                    .orElseThrow(() -> new EntityNotFoundException("Book not found"));

            if (book.getStock() < item.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for book: " + book.getTitle());
            }

            book.setStock(book.getStock() - item.getQuantity());
            bookRepository.save(book);

            BigDecimal itemPrice = BigDecimal.valueOf(book.getPrice())
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemPrice);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .book(book)
                    .quantity(item.getQuantity())
                    .pricePerUnit(BigDecimal.valueOf(book.getPrice()))
                    .build();

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);

        return convertToDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAllWithItems().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByUser(String email) {
        return orderRepository.findByUserEmailWithItems(email).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderDetails(String email, Long orderId) throws Exception {
        Order order = orderRepository.findByIdAndUserEmailWithItems(orderId, email)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return convertToDto(order);
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, Order.OrderStatus status) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setStatus(status);
        return convertToDto(orderRepository.save(order));
    }

    @Transactional
    public void cancelOrder(String email, Long orderId) throws Exception {
        Order order = orderRepository.findByIdAndUserEmailWithItems(orderId, email)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new OrderCancellationException("Only PENDING orders can be cancelled");
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        
        order.getOrderItems().forEach(item -> {
            Book book = item.getBook();
            book.setStock(book.getStock() + item.getQuantity());
            bookRepository.save(book);
        });
        
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public BigDecimal estimateOrderTotal(List<CreateOrderRequestDTO.OrderItemRequest> items) {
        return items.stream()
                .map(item -> {
                    Book book = bookRepository.findById(item.getBookId())
                            .orElseThrow(() -> new EntityNotFoundException("Book not found"));
                    return BigDecimal.valueOf(book.getPrice())
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderResponseDTO convertToDto(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .items(new ArrayList<>()) 
                .build();
    }


    private OrderResponseDTO.OrderItemResponse convertOrderItemToDto(OrderItem orderItem) {
        return OrderResponseDTO.OrderItemResponse.builder()
                .bookTitle(orderItem.getBook().getTitle())
                .bookIsbn(orderItem.getBook().getIsbn())
                .quantity(orderItem.getQuantity())
                .pricePerUnit(orderItem.getPricePerUnit())
                .build();
    }
}