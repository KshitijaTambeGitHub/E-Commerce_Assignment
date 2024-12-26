package com.example.ecommerce.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.CartDto;
import com.example.ecommerce.dto.ItemDto;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.utility.AppConstants;

@Service
public class CartServiceImpl implements CartService {

	private CartRepository cartRepository;
	private UserRepository userRepository;
	private ItemRepository itemRepository;
	private CartItemRepository cartItemRepository;

	public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ItemRepository itemRepository,
			CartItemRepository cartItemRepository) {
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
		this.itemRepository = itemRepository;
		this.cartItemRepository = cartItemRepository;
	}

	@Override
	public ResponseEntity<Object> addItemToCart(Long userId, Long itemId) {

		Optional<User> optionalUser = userRepository.findByIdAndStatus(userId, AppConstants.ACTIVE);
		if (optionalUser.isEmpty()) {
			return new ResponseEntity<>(AppConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}

		Optional<Item> optionalItem = itemRepository.findByIdAndStatus(itemId, AppConstants.ACTIVE);
		if (optionalItem.isEmpty()) {
			return new ResponseEntity<>(AppConstants.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		User user = optionalUser.get();
		Item item = optionalItem.get();
		Optional<Cart> optionalCart = cartRepository.findByUserAndStatus(user, AppConstants.ACTIVE);
		Cart cart;

		if (optionalCart.isPresent()) {
			cart = optionalCart.get();
		} else {
			cart = new Cart();
			cart.setUser(user);
			cart.setStatus(AppConstants.ACTIVE);
			cart = cartRepository.save(cart);
		}

		Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndItemAndStatus(cart, item,
				AppConstants.ACTIVE);
		CartItem cartItem;

		if (optionalCartItem.isPresent()) {
			cartItem = optionalCartItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + 1);
		} else {
			cartItem = new CartItem();
			cartItem.setQuantity(1);
			cartItem.setCart(cart);
			cartItem.setItem(item);
			cartItem.setStatus(AppConstants.ACTIVE);
		}

		cartItemRepository.save(cartItem);

		return new ResponseEntity<Object>(AppConstants.ITEM_ADDED_CART_SUCCESS, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Object> removeItemFromCart(Long userId, Long itemId) {
		Optional<User> optionalUser = userRepository.findByIdAndStatus(userId, AppConstants.ACTIVE);
		if (optionalUser.isEmpty()) {
			return new ResponseEntity<>(AppConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		Optional<Item> optionalItem = itemRepository.findByIdAndStatus(itemId, AppConstants.ACTIVE);
		if (optionalItem.isEmpty()) {
			return new ResponseEntity<>(AppConstants.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		User user = optionalUser.get();
		Item item = optionalItem.get();
		Optional<Cart> optionalCart = cartRepository.findByUserAndStatus(user, AppConstants.ACTIVE);
		if (optionalCart.isEmpty()) {
			return new ResponseEntity<>(AppConstants.CART_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		Cart cart = optionalCart.get();

		Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndItemAndStatus(cart, item,
				AppConstants.ACTIVE);
		if (optionalCartItem.isEmpty()) {
			return new ResponseEntity<>(AppConstants.ITEM_NOT_IN_CART, HttpStatus.NOT_FOUND);
		}
		CartItem cartItem = optionalCartItem.get();
		if (cartItem.getQuantity() > 1) {
			cartItem.setQuantity(cartItem.getQuantity() - 1);
		} else {
			cartItem.setStatus(AppConstants.DE_ACTIVE);
		}
		cartItemRepository.save(cartItem);

		return new ResponseEntity<>(AppConstants.ITEM_REMOVED_CART_SUCCESS, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> removeAllItemsFromCart(Long userId) {
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
		List<CartItem> cartItems = cartItemRepository.findByCartAndStatus(cart, AppConstants.ACTIVE);

		for (CartItem cartItem : cartItems) {
			cartItem.setStatus(AppConstants.DE_ACTIVE);
			cartItemRepository.save(cartItem);
		}

		return new ResponseEntity<>(AppConstants.ALL_ITEMS_REMOVED_CART_SUCCESS, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> getCart(Long userId) {
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
		CartDto cartDto = new CartDto();
		cartDto.setId(cart.getId());
		cartDto.setItems(cartItemRepository.findByCartAndStatus(cart, AppConstants.ACTIVE).stream().map(i -> {
			ItemDto itemDto = new ItemDto();
			itemDto.setId(i.getId());
			itemDto.setPrice(i.getItem().getPrice());
			itemDto.setDescription(i.getItem().getDescription());
			return itemDto;
		}).collect(Collectors.toList()));

		return new ResponseEntity<>(cart, HttpStatus.OK);
	}

}
