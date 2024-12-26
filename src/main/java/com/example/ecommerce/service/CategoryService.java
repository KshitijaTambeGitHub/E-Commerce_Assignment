package com.example.ecommerce.service;

import org.springframework.http.ResponseEntity;

import com.example.ecommerce.dto.CategoryDto;

public interface CategoryService {
	
	ResponseEntity<Object> addCategory(CategoryDto category);  
	ResponseEntity<Object> getAllCategories();              
	ResponseEntity<Object> getCategoryById(Long categoryId); 
	ResponseEntity<Object> updateCategory(CategoryDto category,Long categoryId);
	ResponseEntity<Object> disableCategory(Long categoryId);


}
