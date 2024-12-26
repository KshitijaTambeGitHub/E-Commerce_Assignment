package com.example.ecommerce.service;

import org.springframework.http.ResponseEntity;

public interface CartService {

	ResponseEntity<Object> addItemToCart(Long userId,Long itemId);
	ResponseEntity<Object> removeItemFromCart(Long userId,Long itemId);
	ResponseEntity<Object> removeAllItemsFromCart(Long userId);
	ResponseEntity<Object> getCart(Long userId);
}
