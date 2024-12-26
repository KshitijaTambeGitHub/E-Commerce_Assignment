package com.example.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ecommerce.entity.Item;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.serviceImpl.ItemServiceImpl;



public class ItemServiceTest {

	
	
	@SpringBootTest
	public class ItemServiceImplTest {

	    @Mock
	    private ItemRepository itemRepo;

	    @InjectMocks
	    private ItemServiceImpl itemService;

	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testGetAllItems() {
	        List<Item> items = new ArrayList<>();
	        when(itemRepo.findAll()).thenReturn(items);
	        
	        List<Item> result = (List<Item>) itemService.getAllItems();
	        verify(itemRepo, times(1)).findAll();
	        assertSame(items, result);
	    }

	    @Test
	    public void testGetItemById() {
	        Item item = new Item();
	        when(itemRepo.findById(anyLong())).thenReturn(Optional.of(item));
	        
	        Optional<Item> result = Optional.empty();
	        verify(itemRepo, times(1)).findById(1L);
	        assertTrue(result.isPresent());
	        assertSame(item, result.get());
	    }

	    @Test
	    public void testDeleteItem() {
	        when(itemRepo.existsById(anyLong())).thenReturn(true);
	        
	        itemService.deleteItem(1L);
	        verify(itemRepo, times(1)).existsById(1L);
	        verify(itemRepo, times(1)).deleteById(1L);
	    }

	    @Test
	    public void testDeleteItemNotFound() {
	        when(itemRepo.existsById(anyLong())).thenReturn(false);

	        assertThrows(IllegalArgumentException.class, () -> itemService.deleteItem(1L));
	        verify(itemRepo, times(1)).existsById(1L);
	    }
	}
}
