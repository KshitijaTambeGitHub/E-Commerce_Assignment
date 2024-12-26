package com.example.ecommerce.service;

import org.springframework.http.ResponseEntity;

import com.example.ecommerce.dto.ItemDto;

public interface ItemService {
	
	ResponseEntity<Object> addItem(ItemDto item);

	ResponseEntity<Object> getAllItems();

	ResponseEntity<Object> getItemById(Long itemId);

	ResponseEntity<Object> updateItem(ItemDto item,Long itemId);

	ResponseEntity<Object> deleteItem(Long itemId);

}
