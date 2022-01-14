package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        this.itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }


    @Test
    public void verifyGetItems() {

        long id = 1;
        Item item = new Item();
        item.setId(id);
        item.setName("item");
        item.setDescription("description");
        item.setPrice(BigDecimal.valueOf(100));

        long id2 = 2;
        Item item2 = new Item();
        item2.setId(id);
        item2.setName("item2");
        item2.setDescription("description2");
        item2.setPrice(BigDecimal.valueOf(200));

        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);


        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> returnedItems = response.getBody();
        assertNotNull(returnedItems);

        assertEquals(2, returnedItems.size());

        assertEquals("item", returnedItems.get(0).getName());
        assertEquals("description", returnedItems.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(100), returnedItems.get(0).getPrice());

        assertEquals("item2", returnedItems.get(1).getName());
        assertEquals("description2", returnedItems.get(1).getDescription());
        assertEquals(BigDecimal.valueOf(200), returnedItems.get(1).getPrice());
    }

    @Test
    public void verifyGetItemById() {

        long id = 1;
        Item item = new Item();
        item.setId(id);
        item.setName("item");
        item.setDescription("description");
        item.setPrice(BigDecimal.valueOf(100));

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item returnedItem = response.getBody();
        assertNotNull(returnedItem);
        long returnedItemId = returnedItem.getId();

        assertEquals(id, returnedItemId);
        assertEquals("item", returnedItem.getName());
        assertEquals("description", returnedItem.getDescription());
        assertEquals(BigDecimal.valueOf(100), returnedItem.getPrice());
    }

    @Test
    public void verifyGetItemsByName() {

        long id = 1;
        Item item = new Item();
        item.setId(id);
        item.setName("item");
        item.setDescription("description");
        item.setPrice(BigDecimal.valueOf(100));

        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findByName(item.getName())).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName(item.getName());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> returnedItems = response.getBody();
        assertNotNull(returnedItems);

        assertEquals(1, returnedItems.size());
        assertEquals("item", returnedItems.get(0).getName());
        assertEquals("description", returnedItems.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(100), returnedItems.get(0).getPrice());
    }


    }
