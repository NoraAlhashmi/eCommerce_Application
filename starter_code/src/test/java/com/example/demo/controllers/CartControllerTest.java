package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository  = mock(ItemRepository.class);


    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void verifyAddToCart(){

        long itemId = 1;
        Item item = new Item();
        item.setId(itemId);
        item.setName("item");
        item.setDescription("description");
        item.setPrice(BigDecimal.valueOf(100));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

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

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setQuantity(5);
        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart returnedCart = response.getBody();
        assertNotNull(returnedCart);
        assertEquals(6,returnedCart.getItems().size());
    }

    @Test
    public void verifyAddToCart_UserNotFound(){

        long itemId = 1;
        Item item = new Item();
        item.setId(itemId);
        item.setName("item");
        item.setDescription("description");
        item.setPrice(BigDecimal.valueOf(100));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

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

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setQuantity(5);
        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void verifyAddToCart_ItemNotFound(){

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

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setQuantity(5);
        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void verifyRemoveFromCart(){
        long itemId = 1;
        Item item = new Item();
        item.setId(itemId);
        item.setName("item");
        item.setDescription("description");
        item.setPrice(BigDecimal.valueOf(100));

        long itemId2 = 2;
        Item removedItem = new Item();
        removedItem.setId(itemId2);
        removedItem.setName("item1");
        removedItem.setDescription("description1");
        removedItem.setPrice(BigDecimal.valueOf(200));
        when(itemRepository.findById(itemId2)).thenReturn(Optional.of(removedItem));

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(item.getPrice());
        cart.addItem(item);
        cart.addItem(removedItem);

        long userId = 1;
        User user = new User();
        user.setUsername("nora");
        user.setPassword("1234567");
        user.setId(userId);
        user.setCart(cart);
        cart.setUser(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);


        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setItemId(removedItem.getId());
        modifyCartRequest.setQuantity(1);
        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart returnedCart = response.getBody();
        assertNotNull(returnedCart);
        assertEquals(1,returnedCart.getItems().size());
    }


}
