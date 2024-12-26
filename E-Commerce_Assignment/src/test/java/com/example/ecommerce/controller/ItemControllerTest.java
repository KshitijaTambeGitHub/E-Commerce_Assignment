package com.example.ecommerce.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.serviceImpl.ItemServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(OrderController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllItems() throws Exception {
        List<Order> orders = new ArrayList<>();
        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    public void testGetOrderById() throws Exception {
        Order order = new Order();
        when(orderService.getOrderById(anyLong())).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    public void testUpdateOrder() throws Exception {
        Order order = new Order();
        when(orderService.updateOrder(anyLong(), any(Order.class))).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService, times(1)).updateOrder(eq(1L), any(Order.class));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(orderService, times(1)).deleteOrder(1L);
    }
    }

