package com.example.ecommerce.serviceImpl;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utility.AppConstants;



@Service
public class UserServiceImpl implements UserService {

	private BCryptPasswordEncoder passwordEncoder;
	private UserRepository userRepository;

	public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;

	}

	public ResponseEntity<Object> addUser(UserDto userDto) {

		if (userRepository.existsByUsernameAndStatus(userDto.getName(), AppConstants.ACTIVE)) {
			return new ResponseEntity<>(AppConstants.USER_NAME_ALREADY_EXIST, HttpStatus.CONFLICT);
		}
		if (userRepository.existsByEmailAndStatus(userDto.getEmail(), AppConstants.ACTIVE)) {
			return new ResponseEntity<>(AppConstants.USER_EMAIL_ALREADY_EXIST, HttpStatus.CONFLICT);
		}
		User user = new User();
		user.setUsername(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setStatus(AppConstants.ACTIVE);
		userRepository.save(user);
		return new ResponseEntity<>(AppConstants.USER_ACCOUNT_CREATED, HttpStatus.CREATED);
	}
}
