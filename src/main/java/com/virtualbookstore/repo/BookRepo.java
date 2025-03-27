package com.virtualbookstore.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.virtualbookstore.entity.Book;
import com.virtualbookstore.entity.Order;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByCategoryId(Long categoryId);
	Optional<Order> findByTitle(String bookTitle);

}