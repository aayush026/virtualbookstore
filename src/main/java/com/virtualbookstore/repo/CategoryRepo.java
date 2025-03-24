package com.virtualbookstore.repo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.virtualbookstore.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
	Optional<Category>  findById(Long id);
    Optional<Category> findByName(String name);
}
