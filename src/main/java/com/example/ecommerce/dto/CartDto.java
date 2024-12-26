package com.example.ecommerce.dto;

import java.util.List;

import lombok.Data;

@Data
public class CartDto {

	private Long id;
	private List<ItemDto> items;
	
}
