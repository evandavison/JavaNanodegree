package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){

        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

    }

    @Test
    public void user_controller_happy_path() throws Exception{

        //Accounting for hashing of password for
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        //Creating a dummy user request
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        CreateUserRequest bad = new CreateUserRequest();
        bad.setUsername("test1");
        bad.setPassword("passwords");
        bad.setConfirmPassword("password");

        //Testing CreateUser Method from controller with proper Request.
        final ResponseEntity<User> response = userController.createUser(r);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNotNull(u);
        Assert.assertEquals(0, u.getId());
        Assert.assertEquals("test", u.getUsername());
        Assert.assertEquals("thisIsHashed",u.getPassword());

        //testing bad request
        final ResponseEntity<User> nope = userController.createUser(bad);

        Assert.assertNotNull(nope);
        Assert.assertEquals(400, nope.getStatusCodeValue());

        //Any time findById is run, return the recently created user request.
        when(userRepository.findById((long) 0)).thenReturn(Optional.of(u));

        //Testing FindById method from controller.
        final ResponseEntity<User> responseById = userController.findById((long) 0);

        Assert.assertNotNull(responseById);
        Assert.assertEquals(200, responseById.getStatusCodeValue());

        User foundById = responseById.getBody();
        Assert.assertNotNull(foundById);
        Assert.assertEquals(0, foundById.getId());
        Assert.assertEquals("test", foundById.getUsername());
        Assert.assertEquals("thisIsHashed",foundById.getPassword());

        //Any time findByUsername is run, return the recently created user request.
        when(userRepository.findByUsername("test")).thenReturn((u));

        final ResponseEntity<User> responseByName = userController.findByUserName("test");

        Assert.assertNotNull(responseByName);
        Assert.assertEquals(200, responseByName.getStatusCodeValue());

        User foundByName = responseById.getBody();
        Assert.assertNotNull(foundByName);
        Assert.assertEquals(0, foundByName.getId());
        Assert.assertEquals("test", foundByName.getUsername());
        Assert.assertEquals("thisIsHashed",foundByName.getPassword());
    }

}
