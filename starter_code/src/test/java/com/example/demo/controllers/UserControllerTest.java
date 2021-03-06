package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    public void verifyCreateUser(){
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);

        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void verifyFindUserById(){
        long id = 1;
        User user = new User();
        user.setUsername("nora");
        user.setPassword("1234567");
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User returnedUser = response.getBody();
        assertNotNull(returnedUser);
        assertEquals(id, returnedUser.getId());
        assertEquals("nora", returnedUser.getUsername());
        assertEquals("1234567", returnedUser.getPassword());

    }

    @Test
    public void verifyFindUserByUsername(){
        long id = 1;
        User user = new User();
        user.setUsername("nora");
        user.setPassword("1234567");
        user.setId(id);

        when(userRepository.findByUsername("nora")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("nora");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User returnedUser = response.getBody();
        assertNotNull(returnedUser);
        assertEquals(id, returnedUser.getId());
        assertEquals("nora", returnedUser.getUsername());
        assertEquals("1234567", returnedUser.getPassword());
    }

}
