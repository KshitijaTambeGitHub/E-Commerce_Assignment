package com.example.ecommerce.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.ItemDto;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.service.ItemService;
import com.example.ecommerce.utility.AppConstants;

@Service
public class ItemServiceImpl implements ItemService {

	private ItemRepository itemRepository;
	private CategoryRepository categoryRepository;

	public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository) {
		this.itemRepository = itemRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public ResponseEntity<Object> addItem(ItemDto itemDto) {
		Item item = new Item();
		item.setName(itemDto.getName());
		item.setDescription(itemDto.getDescription());
		item.setStatus(AppConstants.ACTIVE);
		item.setPrice(itemDto.getPrice());
		Set<Category> categories = categoryRepository.findByIdInAndStatus(itemDto.getCategories(),
				AppConstants.ACTIVE);
		if (categories == null) {
			return new ResponseEntity<>(AppConstants.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		item.setCategories(categories);
		itemRepository.save(item);
		return new ResponseEntity<>(AppConstants.ITEM_ADDED_SUCCESS, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Object> getAllItems() {
		List<ItemDto> itemList = itemRepository.findByStatus(AppConstants.ACTIVE).stream().map(item -> {
			ItemDto dto = new ItemDto();
			dto.setId(item.getId());
			dto.setName(item.getName());
			dto.setPrice(item.getPrice());
			dto.setDescription(item.getDescription());
			dto.setCategories(item.getCategories().stream().map(Category::getId).collect(Collectors.toList()));
			return dto;
		}).collect(Collectors.toList());
		return new ResponseEntity<>(itemList, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> getItemById(Long itemId) {
		Optional<Item> optionalItem = itemRepository.findByIdAndStatus(itemId, AppConstants.ACTIVE);
		if (optionalItem.isEmpty()) {
			return new ResponseEntity<>(AppConstants.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		Item item = optionalItem.get();
		ItemDto dto = new ItemDto();
		dto.setId(item.getId());
		dto.setName(item.getName());
		dto.setPrice(item.getPrice());
		dto.setDescription(item.getDescription());
		dto.setCategories(item.getCategories().stream().map(Category::getId).collect(Collectors.toList()));
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> updateItem(ItemDto itemDto,Long itemId) {
		Optional<Item> optionalItem = itemRepository.findByIdAndStatus(itemId, AppConstants.ACTIVE);
		if (optionalItem.isEmpty()) {
			return new ResponseEntity<>(AppConstants.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		Item item = optionalItem.get();
		item.setName(itemDto.getName());
		item.setDescription(itemDto.getDescription());
		item.setPrice(itemDto.getPrice());

		Set<Category> categories = categoryRepository.findByIdInAndStatus(itemDto.getCategories(),
				AppConstants.ACTIVE);
		if (categories.isEmpty()) {
			return new ResponseEntity<>(AppConstants.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		item.getCategories().clear();
		item.setCategories(categories);
		itemRepository.save(item);

		return new ResponseEntity<>(AppConstants.ITEM_UPDATED_SUCCESS, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> deleteItem(Long itemId) {
		Optional<Item> optionalItem = itemRepository.findByIdAndStatus(itemId, AppConstants.ACTIVE);
		if (optionalItem.isEmpty()) {
			return new ResponseEntity<>(AppConstants.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		Item item = optionalItem.get();
		item.setStatus(AppConstants.DE_ACTIVE);
		itemRepository.save(item);
		return new ResponseEntity<>(AppConstants.ITEM_DELETE_SUCCESS, HttpStatus.OK);
	}

}
