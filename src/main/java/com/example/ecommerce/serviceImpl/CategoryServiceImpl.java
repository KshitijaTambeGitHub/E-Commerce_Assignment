package com.example.ecommerce.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.CategoryDto;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.utility.AppConstants;

@Service
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public ResponseEntity<Object> addCategory(CategoryDto category) {

		Category cat = new Category();
		cat.setName(category.getName());
		cat.setDescription(category.getDescription());
		cat.setStatus(AppConstants.ACTIVE);
		if (category.getParentCategoryId() != null) {
			Optional<Category> parentCategory = categoryRepository.findById(category.getParentCategoryId());
			if (parentCategory.isPresent()) {
				cat.setParentCategory(parentCategory.get());
			} else {
				return new ResponseEntity<>(AppConstants.PARENT_CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
			}
		}
		categoryRepository.save(cat);
		return new ResponseEntity<>(AppConstants.CATEGORY_ADDED_SUCCESS, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Object> getAllCategories() {
		List<CategoryDto> Categorylist = categoryRepository.findByStatus(AppConstants.ACTIVE).stream().map(cat -> {
			CategoryDto dto = new CategoryDto();
			dto.setId(cat.getId());
			dto.setName(cat.getName());
			dto.setDescription(cat.getDescription());
			dto.setParentCategoryId(cat.getParentCategory() != null ? cat.getParentCategory().getId() : null);
			return dto;
		}).collect(Collectors.toList());
		return new ResponseEntity<>(Categorylist, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> getCategoryById(Long categoryId) {
		Category category = categoryRepository.findByIdAndStatus(categoryId, AppConstants.ACTIVE);
		if (category == null) {
			return new ResponseEntity<>(AppConstants.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		CategoryDto dto = new CategoryDto();
		dto.setId(category.getId());
		dto.setName(category.getName());
		dto.setDescription(category.getDescription());
		dto.setParentCategoryId(category.getParentCategory() != null ? category.getParentCategory().getId() : null);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> updateCategory(CategoryDto category,Long itemId) {
		Category cat = categoryRepository.findByIdAndStatus(itemId, AppConstants.ACTIVE);
		if (cat == null) {
			return new ResponseEntity<>(AppConstants.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		cat.setName(category.getName());
		cat.setDescription(category.getDescription());
		if (category.getParentCategoryId() != null) {
			Optional<Category> parentCategory = categoryRepository.findById(category.getParentCategoryId());
			if (parentCategory.isPresent()) {
				cat.setParentCategory(parentCategory.get());
			} else {
				return new ResponseEntity<>(AppConstants.PARENT_CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
			}
		}
		categoryRepository.save(cat);
		return new ResponseEntity<>(AppConstants.CATEGORY_UPDATED_SUCCESS, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<Object> disableCategory(Long categoryId) {
		Category cat = categoryRepository.findByIdAndStatus(categoryId, AppConstants.ACTIVE);
		if (cat == null) {
			return new ResponseEntity<>(AppConstants.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		cat.setStatus(AppConstants.DE_ACTIVE);
		categoryRepository.save(cat);
		return new ResponseEntity<>(AppConstants.CATEGORY_DISABLED_SUCCESS, HttpStatus.OK);

	}

}
