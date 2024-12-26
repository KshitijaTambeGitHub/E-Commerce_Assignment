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

import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.service.OrderService;

@RestController
@RequestMapping("orders")
public class OrderController {

	private OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("api/order/{userId}")
	public ResponseEntity<Object> createOrder(@PathVariable Long userId) {
		return orderService.createOrder(userId);
	}

	@GetMapping("api/getAllOrders/{orderId}")
	public ResponseEntity<Object> getAllOrders() {
		return orderService.getAllOrders();

	}

	@GetMapping("api/orders/{orderId}")
	public ResponseEntity<Object> getOrderById(@PathVariable Long orderId) {
		return orderService.getOrderById(orderId);
	}

	@PutMapping("api/updateOrder/{orderId}")
	public ResponseEntity<Object> updateOrder(@PathVariable Long orderId, @RequestBody OrderDto orderDetails) {
		return orderService.updateOrder(orderId, orderDetails);

	}

	@DeleteMapping("api/cancelOrder/{orderId}")
	public ResponseEntity<Object> cancelOrder(@PathVariable Long orderId) {
		ResponseEntity<Object> response = orderService.cancelOrder(orderId);
		return response;
	}

	@GetMapping("api/getAllOrders/user/{userId}")
	public ResponseEntity<Object> getAllOrdersByUserId(@PathVariable Long userId) {
		ResponseEntity<Object> response = orderService.getAllOrdersByUserId(userId);
		return response;
	}
}
