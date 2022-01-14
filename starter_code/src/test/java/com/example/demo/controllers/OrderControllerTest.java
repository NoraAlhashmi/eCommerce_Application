package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        this.orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void submitOrder() {
        long itemId = 1;
        Item item = new Item();
        item.setId(itemId);
        item.setName("item");
        item.setDescription("description");
        item.setPrice(BigDecimal.valueOf(100));
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(item.getPrice());
        cart.addItem(item);
        long userId = 1;
        User user = new User();
        user.setUsername("nora");
        user.setPassword("1234567");
        user.setId(userId);
        user.setCart(cart);
        cart.setUser(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder returnedOrder = response.getBody();
        assertNotNull(returnedOrder);
        assertEquals(1,returnedOrder.getItems().size());
    }

    @Test
    public void verifyGetOrdersForUser() {

        long itemId = 1;
        Item item = new Item();
        item.setId(itemId);
        item.setName("item");
        item.setDescription("description");
        item.setPrice(BigDecimal.valueOf(100));
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(item.getPrice());
        cart.addItem(item);
        long userId = 1;
        User user = new User();
        user.setUsername("nora");
        user.setPassword("1234567");
        user.setId(userId);
        user.setCart(cart);
        cart.setUser(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        orderController.submit(user.getUsername());
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

    }

}
