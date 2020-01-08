package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){

        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void cart_controller_happy_path() throws Exception{
        //Creating a dummy user request
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("test");
        r.setItemId(0);
        r.setQuantity(10);

        User mockUser = mockUser();
        Item mockItem = mockItem();
        Cart mockCart = mockCart(mockUser);
        mockUser.setCart(mockCart);

        when(userRepository.findByUsername(r.getUsername())).thenReturn((mockUser));
        when(itemRepository.findById(r.getItemId())).thenReturn(Optional.of(mockItem));

        //Testing addToCart Method from controller.
        final ResponseEntity<Cart> response = cartController.addTocart(r);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Cart c = response.getBody();
        Assert.assertNotNull(c);
        Assert.assertEquals(new BigDecimal(99.90).setScale(2, RoundingMode.HALF_UP), c.getTotal().setScale(2, RoundingMode.HALF_UP));
        Assert.assertEquals("test", c.getUser().getUsername());

        //Testing removeFromCart method
        //Using the same cart from above, the cart is now removing 5 items from before....
        r.setQuantity(5);
        final ResponseEntity<Cart> remove = cartController.removeFromCart(r);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Cart re = response.getBody();
        Assert.assertNotNull(re);
        //Checking the username of the cart + the totals for 5 "waffles"
        Assert.assertEquals(new BigDecimal(49.95).setScale(2, RoundingMode.HALF_UP), re.getTotal().setScale(2, RoundingMode.HALF_UP));
        Assert.assertEquals("test", re.getUser().getUsername());
    }

    public User mockUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
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

    public Cart mockCart(User user){
        Cart cart = new Cart();
        List items = new ArrayList<>();
        items.add(mockItem());
        cart.setItems(items);
        cart.setUser(user);
        cartRepository.save(cart);
        return cart;
    }
}
