package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {

        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

    }

    @Test
    public void item_controller_happy_path() throws Exception {

        Item mockItem = mockItem();
        itemRepository.save(mockItem);
        System.out.print(mockItem.getId());
        List<Item> mockList = new ArrayList<>();
        mockList.add(mockItem);

        //making sure a list is actually returned from mock repository.
        when(itemRepository.findAll()).thenReturn(mockList);
        //finding all items, in this case just one item
        final ResponseEntity<List<Item>> listResponse = itemController.getItems();

        Assert.assertNotNull(listResponse);
        Assert.assertEquals(200, listResponse.getStatusCodeValue());

        List<Item> list = listResponse.getBody();
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("Waffle", list.get(0).getName());

        //Making sure item is actually returned.
        when(itemRepository.findById((long) 0)).thenReturn(Optional.of(mockItem));
        //Testing findById method
        final ResponseEntity<Item> foundById = itemController.getItemById((long) 0);

        Assert.assertNotNull(foundById);
        Assert.assertEquals(200, foundById.getStatusCodeValue());

        Item i = foundById.getBody();
        itemRepository.save(i);
        Assert.assertEquals("Waffle", i.getName());
//        Assert.assertEquals(0, valueOf(i.getId()));

        when(itemRepository.findByName("Waffle")).thenReturn(mockList);
        final ResponseEntity<List<Item>> foundByName = itemController.getItemsByName("Waffle");

        Assert.assertNotNull(foundByName);
        Assert.assertEquals(200, foundByName.getStatusCodeValue());

        List<Item> listByName = foundByName.getBody();
        Assert.assertEquals(1, listByName.size());

    }

    public Item mockItem(){
        Item item = new Item();
        item.setName("Waffle");
        item.setDescription("The tastiest of the bunch.");
        item.setPrice(new BigDecimal(9.99));
        itemRepository.save(item);
        return item;
    }
}