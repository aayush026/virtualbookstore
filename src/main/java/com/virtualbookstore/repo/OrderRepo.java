package com.virtualbookstore.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.virtualbookstore.entity.Order;
import com.virtualbookstore.entity.Order.OrderStatus;
import com.virtualbookstore.entity.User;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
	List<Order> findByUserId(Long userId);

	// List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
	List<Order> findByUser(User user);

	List<Order> findByStatus(OrderStatus status);

	// List<Order> findByUserIdOrderByOrderDateDesc(@Param("userId") Long userId);
	List<Order> findByUser_IdOrderByOrderDateDesc(Long userId);

	@EntityGraph(attributePaths = { "orderItems", "orderItems.book" })
	List<Order> findAllWithItems();

	@EntityGraph(attributePaths = { "orderItems", "orderItems.book" })
	List<Order> findByUserEmailWithItems(String email);

	@EntityGraph(attributePaths = { "orderItems", "orderItems.book" })
	Optional<Order> findByIdAndUserEmailWithItems(Long id, String email);

	List<Order> findByUserEmail(String email);

	Optional<Order> findByIdAndUserEmail(Long id, String email);
}
