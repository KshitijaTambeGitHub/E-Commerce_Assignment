package com.example.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertSame;
	import static org.junit.jupiter.api.Assertions.assertThrows;
	import static org.junit.jupiter.api.Assertions.assertTrue;
	import static org.mockito.ArgumentMatchers.any;
	import static org.mockito.ArgumentMatchers.anyLong;
	import static org.mockito.Mockito.times;
	import static org.mockito.Mockito.verify;
	import static org.mockito.Mockito.when;

	import java.util.ArrayList;
	import java.util.List;
	import java.util.Optional;

	import org.junit.jupiter.api.BeforeEach;
	import org.junit.jupiter.api.Test;
	import org.mockito.InjectMocks;
	import org.mockito.Mock;
	import org.mockito.MockitoAnnotations;
	import org.springframework.boot.test.context.SpringBootTest;

	import com.example.ecommerce.entity.Order;
	import com.example.ecommerce.repository.OrderRepository;
	import com.example.ecommerce.serviceImpl.OrderServiceImpl;

		@SpringBootTest
	public class OrderServiceTest {

	    @Mock
	    private OrderRepository orderRepo;

	    @InjectMocks
	    private OrderServiceImpl orderService;

	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testGetAllOrders() {
	        List<Order> orders = new ArrayList<>();
	        when(orderRepo.findAll()).thenReturn(orders);
	        
	        List<Order> result = (List<Order>) orderService.getAllOrders();
	        verify(orderRepo, times(1)).findAll();
	        assertSame(orders, result);
	    }

	    @Test
	    public void testGetOrderById() {
	        Order order = new Order();
	        when(orderRepo.findById(anyLong())).thenReturn(Optional.of(order));
	        
	        Optional<Order> result = Optional.empty();
	        verify(orderRepo, times(1)).findById(1L);
	        assertTrue(result.isPresent());
	        assertSame(order, result.get());
	    }
	    @Test
	    public void testCreateOrder() {
	        Order order = new Order();
	        when(orderRepo.save(any(Order.class))).thenReturn(order);
	        
	        Order result = orderService.createOrder(order);
	        verify(orderRepo, times(1)).save(order);
	        assertSame(order, result);
	   
	    @Test
	    public void testDeleteOrder() {
	        when(orderRepo.existsById(anyLong())).thenReturn(true);
	        
	        orderService.cancelOrder(1L);
	        verify(orderRepo, times(1)).existsById(1L);
	        verify(orderRepo, times(1)).deleteById(1L);
	    }

	   
	}


