package com.example.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ItemDto {
	
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private List<Long> categories;
	private Integer quantity;

}
