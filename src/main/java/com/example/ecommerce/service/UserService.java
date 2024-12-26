package com.example.ecommerce.service;

import org.springframework.http.ResponseEntity;

import com.example.ecommerce.dto.UserDto;

public interface UserService {
	
	ResponseEntity<Object> addUser(UserDto userDto);

}
