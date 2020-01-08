package com.example.demo;


import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject){

        Boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);

            if(!f.isAccessible()){
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);
            if(wasPrivate){
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


//    public User mockUser(UserRepository userRepository){
//        User user = new User();
//        user.setUsername("test");
//        user.setPassword("testPassword");
////        user.setCart(mockCart());
//        userRepository.save(user);
//        return user;
//    }
//
//    public Item mockItem(ItemRepository itemRepository){
//        Item item = new Item();
//        item.setName("Waffle");
//        item.setDescription("The tastiest of the bunch.");
//        item.setPrice(new BigDecimal(9.99));
//        itemRepository.save(item);
//        return item;
//    }
//
//    public Cart mockCart(User user, CartRepository cartRepository, ItemRepository itemRepository){
//        Cart cart = new Cart();
//        List items = new ArrayList<>();
//        items.add(mockItem(itemRepository));
//        cart.setItems(items);
//        cart.setUser(user);
//        cartRepository.save(cart);
//        return cart;
//    }
}
