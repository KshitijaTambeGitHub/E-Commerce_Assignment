package com.example.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByIdAndStatus(Long id, Character status);

	Boolean existsByUsernameAndStatus(String username, Character status);

	Boolean existsByEmailAndStatus(String email, Character status);
}
