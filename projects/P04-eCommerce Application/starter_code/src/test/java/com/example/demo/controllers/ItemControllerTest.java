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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void Setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void get_items_happy_path() {

        List<Item> stompedItemList = getItemsTest();

        when(itemRepo.findAll()).thenReturn(stompedItemList);

        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item > items = response.getBody();

        for (Item element: items) {
            assertNotNull(element);
        }

        assertEquals(stompedItemList.get(0).getId(), items.get(0).getId());
        assertEquals(stompedItemList.get(1).getId(), items.get(1).getId());
    }

    @Test
    public void get_item_by_id_happy_path() {

        List<Item> stompedItemList = getItemsTest();

        when(itemRepo.findById(0L)).thenReturn(java.util.Optional.ofNullable(stompedItemList.get(0)));

        final ResponseEntity<Item> response = itemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item i = response.getBody();
        assertNotNull(i);
        assertEquals("Round Widget", i.getName());
        assertEquals(BigDecimal.valueOf(2.99), i.getPrice());
        assertEquals("A widget that is round", i.getDescription());
    }

    @Test
    public void get_item_by_name_happy_path() {

        List<Item> stompedItemList = getItemsTest();

        List<Item> stompedItemList2 = new ArrayList<Item>();
        stompedItemList2.add(stompedItemList.get(0));

        when(itemRepo.findByName("Square Widget")).thenReturn((stompedItemList2));

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Square Widget");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item > items = response.getBody();

        for (Item element: items) {
            assertNotNull(element);
        }

        assertEquals(stompedItemList2.get(0).getId(), items.get(0).getId());
    }

    private List<Item> getItemsTest() {

        Item stompedItem1 = new Item();
        Item stompedItem2 = new Item();

        stompedItem1.setId(0L);
        stompedItem1.setName("Round Widget");
        stompedItem1.setPrice(BigDecimal.valueOf(2.99));
        stompedItem1.setDescription("A widget that is round");

        stompedItem2.setId(1L);
        stompedItem2.setName("Square Widget");
        stompedItem2.setPrice(BigDecimal.valueOf(1.99));
        stompedItem2.setDescription("A widget that is square");

        List<Item> stompedItemList = new ArrayList<Item>();
        stompedItemList.add(stompedItem1);
        stompedItemList.add(stompedItem2);

        return stompedItemList;

    }
}
