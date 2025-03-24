package com.virtualbookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtualbookstore.entity.Book;
import com.virtualbookstore.entity.Category;
import com.virtualbookstore.entity.User;
import com.virtualbookstore.repo.BookRepo;
import com.virtualbookstore.repo.CategoryRepo;
import com.virtualbookstore.repo.UserRepo;
import com.virtualbookstore.util.JWTUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	@Autowired
    private final BookRepo bookRepository;
    @Autowired
	private final UserRepo userRepository;
    @Autowired
    private final CategoryRepo categoryRepository;
    @Autowired
    private final JWTUtil jwtUtil;

    public Book addBook(String token, Book book) {
        String username = jwtUtil.extractUserName(token);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("Only admins can add books");
        }

        if (book.getCategory() == null || book.getCategory().getName() == null) {
            throw new IllegalArgumentException("Category must be provided for a book");
        }

        Category category = categoryRepository.findByName(book.getCategory().getName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(book.getCategory().getName());
                    return categoryRepository.save(newCategory); 
                });

        book.setCategory(category);
        book.setAddedBy(user);
        return bookRepository.save(book);
    }




    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooksByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
}