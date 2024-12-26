package com.example.ecommerce.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByStatus(Character status);
	
    Category findByIdAndStatus(Long id,Character status);

    Set<Category> findByIdInAndStatus(List<Long> ids,Character status);
	
}
