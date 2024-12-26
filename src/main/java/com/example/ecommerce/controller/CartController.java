package com.example.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.service.CartService;

@RestController
@RequestMapping("cart")
public class CartController {

	private CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping("api/addItem/{userId}/{itemId}")
	public ResponseEntity<Object> addItemToCart(@PathVariable Long userId, @PathVariable Long itemId) {
		return cartService.addItemToCart(userId, itemId);
	}

	@DeleteMapping("api/deletesingleItem{userId}/{itemId}")
	public ResponseEntity<Object> removeItemFromCart(@PathVariable Long userId, @PathVariable Long itemId) {
		return cartService.removeItemFromCart(userId, itemId);
	}

	@DeleteMapping("api/deleteAllItems/{userId}")
	public ResponseEntity<Object> removeAllItemsFromCart(@PathVariable Long userId) {
		return cartService.removeAllItemsFromCart(userId);
	}

	@GetMapping("api/getAllCartData/{userId}")
	public ResponseEntity<Object> getCart(@PathVariable Long userId) {
		return cartService.getCart(userId);
	}
}
