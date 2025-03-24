package com.virtualbookstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.virtualbookstore.entity.Book;
import com.virtualbookstore.service.BookService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

	@Autowired
    private final BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<?> addBook(HttpServletRequest request, @RequestBody Book book) {
        String token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(bookService.addBook(token, book));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/category")
    public List<Book> getBooksByCategory(@RequestParam("categoryId") Long categoryId) {
        return bookService.searchBooksByCategory(categoryId);
    }



    @GetMapping("/author/{author}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(bookService.searchBooksByAuthor(author));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Book>> getBooksByTitle(@PathVariable String title) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }
}
