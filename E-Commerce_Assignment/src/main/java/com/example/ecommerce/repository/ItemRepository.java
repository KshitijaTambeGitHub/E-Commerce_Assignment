package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

	List<Item> findByStatus(Character status);

	Optional<Item> findByIdAndStatus(Long id, Character status);
}
