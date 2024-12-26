package com.example.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.CategoryDto;
import com.example.ecommerce.dto.ItemDto;
import com.example.ecommerce.service.ItemService;

@RestController
@RequestMapping("items")
public class ItemController {

	private ItemService itemService;

	public ItemController(ItemService itemService) {
		this.itemService = itemService;

	}

	@PostMapping("api/Items")
	public ResponseEntity<Object> addItem(@RequestBody ItemDto item) {

		return itemService.addItem(item);

	}

	@GetMapping("api/getAllItems")
	public ResponseEntity<Object> getAllItems() {
		return itemService.getAllItems();
	}

	@GetMapping("api/getSingleItem/{itemId}")
	public ResponseEntity<Object> getItemById(@PathVariable Long itemId) {
		return itemService.getItemById(itemId);
	}

	@PutMapping("api/updateItem/{itemId}")
	public ResponseEntity<Object> updateItem(@RequestBody ItemDto item,@PathVariable Long itemId) {
		return itemService.updateItem(item,itemId);
	}

	@DeleteMapping("api/deleteItem/{itemId}")
	public ResponseEntity<Object> disableItem(@PathVariable Long itemId) {
		return itemService.deleteItem(itemId);
	}

}
