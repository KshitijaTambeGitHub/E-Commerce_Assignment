package com.example.ecommerce.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.ItemDto;
import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.utility.AppConstants;

@Service
public class OrderServiceImpl implements OrderService {

	private OrderRepository orderRepository;
	private UserRepository userRepository;
	private CartRepository cartRepository;
	private OrderItemRepository orderitemRepository;
	private ItemRepository itemRepository;
	private CartItemRepository cartItemRepository;

	public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
			CartRepository cartRepository, ItemRepository itemRepository, CartItemRepository cartItemRepository) {
		this.orderRepository = orderRepository;

		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.itemRepository = itemRepository;
		this.cartItemRepository = cartItemRepository;
	}

	@Override
	public ResponseEntity<Object> createOrder(Long userId) {
		Optional<User> optionalUser = userRepository.findByIdAndStatus(userId, AppConstants.ACTIVE);
		if (optionalUser.isEmpty()) {
			return new ResponseEntity<>(AppConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		User user = optionalUser.get();
		Optional<Cart> optionalCart = cartRepository.findByUserAndStatus(user, AppConstants.ACTIVE);
		if (optionalCart.isEmpty()) {
			return new ResponseEntity<>(AppConstants.CART_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		Cart cart = optionalCart.get();
		Order order = new Order();

		Set<OrderItem> orderItems = cartItemRepository.findByCartAndStatus(cart, AppConstants.ACTIVE).stream()
				.map(item -> {
					OrderItem orderItem = new OrderItem();
					orderItem.setItem(item.getItem());
					orderItem.setPrice(item.getItem().getPrice());
					orderItem.setQuantity(item.getQuantity());
					return orderItem;
				}).collect(Collectors.toSet());
		order.setOrderItems(orderItems);
		orderRepository.save(order);
		return new ResponseEntity<>(AppConstants.CART_NOT_FOUND, HttpStatus.CREATED);

	}

	@Override
	public ResponseEntity<Object> getAllOrders() {

		List<OrderDto> orders = orderRepository.findByStatus(AppConstants.ACTIVE).stream().map(order -> {
			OrderDto orderDto = new OrderDto();
			setOrderDetails(order, orderDto);
			setOrderItems(order, orderDto);
			orderDto.setUser(order.getUser());
			return orderDto;
		}).collect(Collectors.toList());
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> getOrderById(Long orderId) {
		Optional<Order> optionalOrder = orderRepository.findByIdAndStatus(orderId, AppConstants.ACTIVE);
		if (optionalOrder.isEmpty()) {
			return new ResponseEntity<>(AppConstants.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		Order order = optionalOrder.get();
		OrderDto orderDto = new OrderDto();
		orderDto.setUser(order.getUser());
		setOrderDetails(order, orderDto);
		setOrderItems(order, orderDto);
		return new ResponseEntity<>(orderDto, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<Object> updateOrder(Long orderId, OrderDto orderDetails) {

		Optional<Order> optionalOrder = orderRepository.findByIdAndStatus(orderId, AppConstants.ACTIVE);
		if (optionalOrder.isEmpty()) {
			return new ResponseEntity<>(AppConstants.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}

		Order order = optionalOrder.get();

		Set<OrderItem> updatedOrderItems = orderDetails.getItems().stream().map(itemDto -> {

			OrderItem existingOrderItem = new OrderItem();

			Optional<Item> optionalItem = itemRepository.findByIdAndStatus(itemDto.getId(), AppConstants.ACTIVE);
			if (optionalItem.isEmpty()) {
				return null;
			}
			Item item = optionalItem.get();

			existingOrderItem.setItem(item);
			existingOrderItem.setPrice(itemDto.getPrice() != null ? itemDto.getPrice() : item.getPrice());
			existingOrderItem.setQuantity(itemDto.getQuantity() != null ? itemDto.getQuantity() : 1);
			return existingOrderItem;
		}).collect(Collectors.toSet());

		order.setOrderItems(updatedOrderItems);
		orderRepository.save(order);
		return new ResponseEntity<>(AppConstants.ORDER_UPDATED_SUCCESS, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<Object> cancelOrder(Long orderId) {
		Optional<Order> optionalOrder = orderRepository.findByIdAndStatus(orderId, AppConstants.ACTIVE);
		if (optionalOrder.isEmpty()) {
			return new ResponseEntity<>(AppConstants.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		Order order = optionalOrder.get();
		order.setStatus(AppConstants.DE_ACTIVE);
		orderRepository.save(order);
		return new ResponseEntity<>(AppConstants.ORDER_CANCELED_SUCCESS, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> getAllOrdersByUserId(Long userId) {
		Optional<User> optionalUser = userRepository.findByIdAndStatus(userId, AppConstants.ACTIVE);
		if (optionalUser.isEmpty()) {
			return new ResponseEntity<>(AppConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		User user = optionalUser.get();
		List<OrderDto> orders = orderRepository.findByUserAndStatus(user, AppConstants.ACTIVE).stream().map(order -> {
			OrderDto orderDto = new OrderDto();
			setOrderDetails(order, orderDto);
			setOrderItems(order, orderDto);
			orderDto.setUser(order.getUser());
			return orderDto;
		}).collect(Collectors.toList());
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	private void setOrderItems(Order order, OrderDto orderDto) {

		orderDto.setItems(order.getOrderItems().stream().map(orderItem -> {
			ItemDto item = new ItemDto();
			item.setCategories(orderItem.getItem() != null && orderItem.getItem().getCategories() != null
					? orderItem.getItem().getCategories().stream().map(Category::getId).collect(Collectors.toList())
					: new ArrayList<>());
			item.setName(orderItem.getItem() != null ? orderItem.getItem().getName() : null);
			item.setPrice(orderItem.getPrice() != null ? orderItem.getPrice() : null);
			item.setDescription(orderItem.getItem() != null ? orderItem.getItem().getDescription() : null);
			return item;

		}).collect(Collectors.toList()));
	}

	private void setOrderDetails(Order order, OrderDto orderDto) {
		orderDto.setId(order.getId());
		orderDto.setOrderDateTime(order.getCreatedAt());
		orderDto.setTotalAmount(order.getTotalAmount());
	}

}
