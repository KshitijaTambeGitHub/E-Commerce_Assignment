package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class UserDto {
	private String name;
	private Long id;
	private String password;
	private String email;

}
