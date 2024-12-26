package com.example.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.ecommerce.entity.User;

import lombok.Data;

@Data
public class OrderDto {

	private Long id;
	
	private List<ItemDto> items;
	
	private BigDecimal totalAmount;
	
	private LocalDateTime orderDateTime;
	
	private  User user;
	
}
