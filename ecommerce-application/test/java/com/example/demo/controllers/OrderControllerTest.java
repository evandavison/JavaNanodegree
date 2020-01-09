package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setUp() {

        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void order_controller_happy_path() throws Exception {

        //Establishing test entities from functions below.
        User mockUser = mockUser();
        Cart mockCart = mockCart(mockUser);
        mockUser.setCart(mockCart);

        // properly find "test" user from repository
        when(userRepository.findByUsername("test")).thenReturn(mockUser);

        //Submitting an order.
        final ResponseEntity<UserOrder> response = orderController.submit(mockUser.getUsername());

        //Making sure the reponse is valid
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        //Ensuring the info from response aligns.
        UserOrder r = response.getBody();
        Assert.assertEquals("test", r.getUser().getUsername());
        Assert.assertEquals(new BigDecimal(9.99).setScale(2, RoundingMode.HALF_UP), r.getTotal().setScale(2, RoundingMode.HALF_UP));

        //"test" user's list of orders.
        List<UserOrder> mockList = new ArrayList<>();
        mockList.add(r);

        //Properly return order for "test" user
        when(orderRepository.findByUser(mockUser)).thenReturn(mockList);
        //finding order history for "test" user
        final ResponseEntity<List<UserOrder>> orderhistory = orderController.getOrdersForUser(mockUser.getUsername());

        Assert.assertNotNull(orderhistory);
        Assert.assertEquals(200, orderhistory.getStatusCodeValue());

        List<UserOrder> rr = orderhistory.getBody();
        Assert.assertEquals(1, rr.size());
        Assert.assertEquals("test", rr.get(0).getUser().getUsername());
    }

    public User mockUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
//        user.setCart(mockCart());
        userRepository.save(user);
        return user;
    }

    public Item mockItem(){
        Item item = new Item();
        item.setName("Waffle");
        item.setDescription("The tastiest of the bunch.");
        item.setPrice(new BigDecimal(9.99));
        itemRepository.save(item);
        return item;
    }
//
    public Cart mockCart(User user){
        Cart cart = new Cart();
        List items = new ArrayList<>();
        cart.setItems(items);
        cart.addItem(mockItem());
        cart.setUser(user);
        cartRepository.save(cart);
        return cart;
    }
}