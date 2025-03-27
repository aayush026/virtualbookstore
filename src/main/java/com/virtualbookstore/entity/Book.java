package com.virtualbookstore.entity;


import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "books")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String author;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String imageUrl;
    
    @Column(nullable = false, unique = true, length = 17) // ISBN-13 max length
    private String isbn;

    // Linking Books to Users (Only Admins can create books)
    @ManyToOne
    @JoinColumn(name = "added_by", nullable = false)
    private User addedBy;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
